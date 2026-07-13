// Lumen — weekly reflection for the Insights "Weekly Reflection" card + screen.
// One warm generative pass over the past week's journal entries.
//
// Deploy:
//   supabase secrets set GEMINI_API_KEY=...        (shared with analyze-entry)
//   supabase functions deploy weekly-reflection
// JWT verification is ON by default — only signed-in users can call this.

import { createClient } from "jsr:@supabase/supabase-js@2"
import { captureError, withSentry } from "../_shared/sentry.ts"

const GEMINI_API_KEY = Deno.env.get("GEMINI_API_KEY")
const GEMINI_MODEL = Deno.env.get("GEMINI_MODEL") ?? "gemini-2.5-flash"
const GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models"

const SUPABASE_URL = Deno.env.get("SUPABASE_URL")
const SERVICE_ROLE_KEY = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")

const corsHeaders: Record<string, string> = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
}

// Keep this shape in sync with the Kotlin WeeklyReflectionRequest / WeeklyReflectionDto.
interface WeeklyRequest {
  entries: string[]
  language?: string | null
}

interface WeeklyTheme {
  label: string
  count: number
}

interface WeeklyResponse {
  narrative: string
  summary: string
  themes: WeeklyTheme[]
  questions: string[]
}

function languageDirective(language?: string | null): string {
  return language && language.trim()
    ? `Write every text field (narrative, summary, theme labels, questions) entirely in ${language.trim()}, regardless of the language of the entries.`
    : `Match the language of the entries (default to the writer's own language).`
}

function weeklySystem(language?: string | null): string {
  return `
You are Lumen's reflective journaling companion. You receive several short daily
journal entries from one person's past week. Write a warm WEEKLY reflection.
Voice: warm, plain, grounded — like a perceptive friend, not a therapist, coach, or
chatbot persona. ${languageDirective(language)}
- narrative: 3–5 sentences. Reflect the week's patterns and texture — what kept
  recurring, the overall tenor, where the writing felt most alive, and any shift
  across the week. Acknowledge difficulty without dwelling. No advice lists, no
  diagnosis, no certainty about their inner state (hedge), no empty praise, and
  never ask them to keep journaling or talking to you.
- summary: 1–2 words naming the week's overall tenor, shown as a small label
  (e.g. "Calm week", "Full week", "Tender week", "Heavy week", "Bright week").
- themes: 2–5 recurring topics. Each has a short lowercase label and a count =
  how many of the provided entries genuinely touch that theme (never more than
  the number of entries given).
- questions: 1–3 short, open, optional questions to sit with — never advice,
  never pressure, not yes/no questions.
If there is little to work with, keep it light rather than forcing insight.
Output ONLY a JSON object:
{"narrative": string, "summary": string,
 "themes": [{"label": string, "count": number}], "questions": string[]}
No prose, no code fences.
`.trim()
}

function stripFences(s: string): string {
  return s.replace(/```json/gi, "").replace(/```/g, "").trim()
}

async function callGemini(
  system: string,
  user: string,
  maxTokens: number,
  temperature: number,
): Promise<string> {
  const res = await fetch(`${GEMINI_URL}/${GEMINI_MODEL}:generateContent`, {
    method: "POST",
    headers: {
      "content-type": "application/json",
      "x-goog-api-key": GEMINI_API_KEY!,
    },
    body: JSON.stringify({
      systemInstruction: { parts: [{ text: system }] },
      contents: [{ role: "user", parts: [{ text: user }] }],
      generationConfig: {
        responseMimeType: "application/json",
        temperature,
        maxOutputTokens: maxTokens,
        thinkingConfig: { thinkingBudget: 0 },
      },
    }),
  })
  if (!res.ok) throw new Error(`gemini ${res.status}: ${await res.text()}`)
  const data = await res.json()
  const text = data?.candidates?.[0]?.content?.parts?.[0]?.text
  if (typeof text !== "string") throw new Error("gemini: empty response")
  return stripFences(text)
}

function json(body: unknown, status: number): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { ...corsHeaders, "content-type": "application/json" },
  })
}

// The Supabase gateway already verified the JWT (verify_jwt=on); read the user id from its `sub`.
function userIdFromJwt(req: Request): string | null {
  const token = (req.headers.get("Authorization") ?? "").replace(/^Bearer\s+/i, "")
  const parts = token.split(".")
  if (parts.length < 2) return null
  try {
    const b64 = parts[1].replace(/-/g, "+").replace(/_/g, "/")
    const pad = b64.length % 4 ? "=".repeat(4 - (b64.length % 4)) : ""
    const payload = JSON.parse(atob(b64 + pad))
    return typeof payload?.sub === "string" ? payload.sub : null
  } catch {
    return null
  }
}

// Weekly reflection is premium-only for free users (quota = 0); premium bypasses. Fails closed on error.
async function consumeCredit(userId: string, kind: "analyze" | "weekly"): Promise<{ allowed: boolean; info: Record<string, unknown> }> {
  const supabase = createClient(SUPABASE_URL!, SERVICE_ROLE_KEY!, { auth: { persistSession: false } })
  const { data, error } = await supabase.rpc("consume_ai_credit", { p_user_id: userId, p_kind: kind })
  if (error) {
    console.error("consume_ai_credit failed:", error.message)
    return { allowed: false, info: { error: "quota_check_failed" } }
  }
  const info = (data ?? {}) as Record<string, unknown>
  return { allowed: Boolean(info.allowed), info }
}

Deno.serve(withSentry("weekly-reflection", async (req: Request) => {
  if (req.method === "OPTIONS") return new Response("ok", { headers: corsHeaders })
  if (req.method !== "POST") return json({ error: "method not allowed" }, 405)
  if (!GEMINI_API_KEY) return json({ error: "server misconfigured: missing GEMINI_API_KEY" }, 500)

  let payload: WeeklyRequest
  try {
    payload = await req.json()
  } catch {
    return json({ error: "invalid json body" }, 400)
  }
  const entries = Array.isArray(payload?.entries)
    ? payload.entries.map((e) => String(e ?? "").trim()).filter((e) => e.length > 0)
    : []
  if (entries.length === 0) return json({ error: "no entries to reflect on" }, 400)

  if (!SUPABASE_URL || !SERVICE_ROLE_KEY) return json({ error: "server misconfigured" }, 500)
  const userId = userIdFromJwt(req)
  if (!userId) return json({ error: "unauthorized" }, 401)
  const gate = await consumeCredit(userId, "weekly")
  if (!gate.allowed) return json({ error: "quota_exceeded", ...gate.info }, 402)

  const user = entries.map((e, i) => `Entry ${i + 1}:\n${e}`).join("\n\n")

  try {
    const raw = await callGemini(weeklySystem(payload.language), user, 700, 0.8)
    const parsed = JSON.parse(raw) as Partial<WeeklyResponse>
    const themes: WeeklyTheme[] = Array.isArray(parsed.themes)
      ? parsed.themes
          .map((t) => ({
            label: String(t?.label ?? "").trim(),
            count: Math.max(1, Math.min(entries.length, Math.round(Number(t?.count ?? 1)))),
          }))
          .filter((t) => t.label.length > 0)
          .slice(0, 5)
      : []
    const questions: string[] = Array.isArray(parsed.questions)
      ? parsed.questions.map((q) => String(q ?? "").trim()).filter((q) => q.length > 0).slice(0, 3)
      : []
    const response: WeeklyResponse = {
      narrative: String(parsed.narrative ?? "").trim(),
      summary: String(parsed.summary ?? "").trim(),
      themes,
      questions,
    }
    return json(response, 200)
  } catch (e) {
    await captureError(e, { function: "weekly-reflection" })
    return json({ error: `weekly reflection failed: ${e instanceof Error ? e.message : String(e)}` }, 502)
  }
}))
