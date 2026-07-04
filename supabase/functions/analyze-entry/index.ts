// Lumen — daily-moments two-pass reflection.
// Pass 1: analyze the entry into a typed EntryAnalysis (JSON).
// Pass 2: generate a warm reflection — ONLY when distress is NONE/MILD.
// The distress gate lives here, in code, not in the model.
//
// Pass 3 (best-effort): generate an abstract AI cover image from the analysis
// and upload it to the private "entry-covers" Storage bucket; a long-lived
// signed URL is returned as coverImageUrl. Cover failures never fail the reflection.
//
// Deploy:
//   supabase secrets set GEMINI_API_KEY=...        (key from aistudio.google.com; must have image-model access)
//   # create the private bucket once (Studio > Storage, or SQL):
//   #   insert into storage.buckets (id, name, public) values ('entry-covers','entry-covers',false);
//   supabase functions deploy analyze-entry
// Optional model overrides:
//   supabase secrets set GEMINI_MODEL=gemini-2.5-flash-lite
//   supabase secrets set GEMINI_IMAGE_MODEL=gemini-2.5-flash-image
// SUPABASE_URL / SUPABASE_SERVICE_ROLE_KEY are injected automatically at runtime.
// JWT verification is ON by default, so only signed-in Lumen users can call this.

import { createClient } from "jsr:@supabase/supabase-js@2"

const GEMINI_API_KEY = Deno.env.get("GEMINI_API_KEY")
const GEMINI_MODEL = Deno.env.get("GEMINI_MODEL") ?? "gemini-2.5-flash"
const GEMINI_IMAGE_MODEL = Deno.env.get("GEMINI_IMAGE_MODEL") ?? "gemini-2.5-flash-image"
const GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models"

const SUPABASE_URL = Deno.env.get("SUPABASE_URL")
const SERVICE_ROLE_KEY = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")
const COVER_BUCKET = "entry-covers"
const COVER_URL_TTL_SECONDS = 31_536_000

const corsHeaders: Record<string, string> = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
}

type MoodValence = "VERY_LOW" | "LOW" | "NEUTRAL" | "POSITIVE" | "VERY_POSITIVE"
type DistressLevel = "NONE" | "MILD" | "ELEVATED" | "CRISIS"

// Keep these field sets in sync with the Kotlin EntryAnalysis / AnalyzeRequest / AnalyzeResponse.
interface EntryAnalysis {
  summary: string
  moodValence: MoodValence
  moodConfidence: number
  dominantEmotions: string[]
  themes: string[]
  distress: DistressLevel
  distressRationale: string
}

interface AnalyzeRequest {
  text: string
  mood?: string | null
  trend?: string | null
}

interface AnalyzeResponse {
  analysis: EntryAnalysis
  feedback: string | null
  question: string | null
  coverImageUrl: string | null
}

const ANALYSIS_SYSTEM = `
You analyze a single daily journal entry and output ONLY a JSON object
matching the EntryAnalysis schema.
- Ground every field strictly in the entry text. Do not infer beyond it.
- summary: 1–2 neutral, factual sentences. No interpretation, no advice.
- moodValence/moodConfidence: confidence = how strongly the text supports
  the valence. Use NEUTRAL + low confidence when unclear.
- dominantEmotions: at most 3, only emotions evident in the text.
- themes: at most 5 short topic tags.
- distress: set CONSERVATIVELY. NONE/MILD for ordinary sadness, stress, or a
  bad day. ELEVATED only for sustained hopelessness, severe distress, or
  inability to cope. CRISIS only for explicit self-harm, suicidal intent, or
  immediate danger. When genuinely uncertain between two levels for real
  risk signals, choose the higher.
- distressRationale: one short internal sentence, never shown to the user.
Output JSON only. No prose, no code fences.
The JSON shape is:
{"summary": string, "moodValence": "VERY_LOW"|"LOW"|"NEUTRAL"|"POSITIVE"|"VERY_POSITIVE",
 "moodConfidence": number, "dominantEmotions": string[], "themes": string[],
 "distress": "NONE"|"MILD"|"ELEVATED"|"CRISIS", "distressRationale": string}
`.trim()

const FEEDBACK_SYSTEM = `
You are a reflective journaling companion inside the app. You receive a
STRUCTURED ANALYSIS of one entry (and optionally a short trend summary of
recent entries) — not the raw entry. Write one brief, warm reflection and one
gentle question for the writer to sit with.
Voice: warm, plain, grounded — like a perceptive friend, not a therapist,
coach, or chatbot persona. Match the language of the summary (Turkish or
English).
reflection: 2–4 sentences. Reflect back the patterns you were given; offer at
most ONE gentle reframe; acknowledge difficulty without dwelling. If the
analysis is thin or neutral, a short light acknowledgement beats forcing insight.
question: ONE short, open, optional question that invites reflection — never
advice, never pressure, not a yes/no question.
Never: diagnose or name conditions (describe patterns, not states); claim
certainty about the writer's inner state (hedge); use empty praise; encourage
dependence on the app or ask the writer to keep journaling/talking to you;
invent anything not in the analysis; give medical/clinical/crisis advice.
Output ONLY a JSON object: {"reflection": string, "question": string}.
No prose, no code fences.
`.trim()

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

async function analyze(text: string, mood?: string | null): Promise<EntryAnalysis> {
  const user = mood
    ? `Self-reported mood: ${mood}\n\nEntry:\n${text}`
    : `Entry:\n${text}`
  const raw = await callGemini(ANALYSIS_SYSTEM, user, 800, 0.3)
  return JSON.parse(raw) as EntryAnalysis
}

function digest(analysis: EntryAnalysis, trend?: string | null): string {
  const lines = [
    `Summary: ${analysis.summary}`,
    `Mood: ${analysis.moodValence.toLowerCase().replace(/_/g, " ")} (confidence ${analysis.moodConfidence})`,
  ]
  if (analysis.dominantEmotions.length) lines.push(`Emotions: ${analysis.dominantEmotions.join(", ")}`)
  if (analysis.themes.length) lines.push(`Themes: ${analysis.themes.join(", ")}`)
  if (trend) lines.push(`Recent trend: ${trend}`)
  return lines.join("\n")
}

interface Feedback {
  reflection: string
  question: string
}

async function feedback(analysis: EntryAnalysis, trend?: string | null): Promise<Feedback> {
  const raw = await callGemini(FEEDBACK_SYSTEM, digest(analysis, trend), 500, 0.8)
  const parsed = JSON.parse(raw) as Feedback
  return {
    reflection: String(parsed.reflection ?? "").trim(),
    question: String(parsed.question ?? "").trim(),
  }
}

function buildCoverPrompt(analysis: EntryAnalysis): string {
  const mood = analysis.moodValence.toLowerCase().replace(/_/g, " ")
  const themes = analysis.themes.slice(0, 3).join(", ") || "a quiet everyday moment"
  const emotions = analysis.dominantEmotions.slice(0, 2).join(", ")
  return [
    "A soft, abstract, painterly illustration for a private journal entry's header banner.",
    `Evoke the feeling of: ${themes}${emotions ? ` (emotional tone: ${emotions})` : ""}.`,
    `Overall mood: ${mood}.`,
    "Atmospheric, calm, dreamlike, muted and harmonious colors, gentle gradients and light.",
    "Wide 3:2 landscape composition suitable for a banner.",
    "Absolutely no text, no words, no letters, no logos, no people, no faces, no recognizable figures.",
  ].join(" ")
}

async function generateCoverImage(prompt: string): Promise<Uint8Array> {
  const res = await fetch(`${GEMINI_URL}/${GEMINI_IMAGE_MODEL}:generateContent`, {
    method: "POST",
    headers: {
      "content-type": "application/json",
      "x-goog-api-key": GEMINI_API_KEY!,
    },
    body: JSON.stringify({
      contents: [{ role: "user", parts: [{ text: prompt }] }],
      generationConfig: { responseModalities: ["TEXT", "IMAGE"] },
    }),
  })
  if (!res.ok) throw new Error(`image ${res.status}: ${await res.text()}`)
  const data = await res.json()
  const parts = data?.candidates?.[0]?.content?.parts ?? []
  const inline = parts.find((p: { inlineData?: { data?: string } }) => p?.inlineData?.data)?.inlineData
  if (!inline?.data) throw new Error("image: no inline image data in response")
  const binary = atob(inline.data)
  const bytes = new Uint8Array(binary.length)
  for (let i = 0; i < binary.length; i++) bytes[i] = binary.charCodeAt(i)
  return bytes
}

async function uploadCover(bytes: Uint8Array, userId: string): Promise<string> {
  if (!SUPABASE_URL || !SERVICE_ROLE_KEY) throw new Error("storage: missing SUPABASE_URL / SERVICE_ROLE_KEY")
  const supabase = createClient(SUPABASE_URL, SERVICE_ROLE_KEY, { auth: { persistSession: false } })
  const path = `${userId}/${crypto.randomUUID()}.png`
  const { error: upErr } = await supabase.storage
    .from(COVER_BUCKET)
    .upload(path, bytes, { contentType: "image/png", upsert: true })
  if (upErr) throw new Error(`storage upload: ${upErr.message}`)
  const { data, error: signErr } = await supabase.storage
    .from(COVER_BUCKET)
    .createSignedUrl(path, COVER_URL_TTL_SECONDS)
  if (signErr || !data?.signedUrl) throw new Error(`storage sign: ${signErr?.message ?? "no url"}`)
  return data.signedUrl
}

async function generateCover(analysis: EntryAnalysis, userId: string): Promise<string | null> {
  try {
    const bytes = await generateCoverImage(buildCoverPrompt(analysis))
    return await uploadCover(bytes, userId)
  } catch (e) {
    console.error("cover generation failed:", e instanceof Error ? e.message : String(e))
    return null
  }
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

// Enforce the free-tier monthly quota (premium bypasses). Fails closed on error to protect cost.
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

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") return new Response("ok", { headers: corsHeaders })
  if (req.method !== "POST") return json({ error: "method not allowed" }, 405)
  if (!GEMINI_API_KEY) return json({ error: "server misconfigured: missing GEMINI_API_KEY" }, 500)

  let payload: AnalyzeRequest
  try {
    payload = await req.json()
  } catch {
    return json({ error: "invalid json body" }, 400)
  }
  if (!payload?.text || typeof payload.text !== "string" || !payload.text.trim()) {
    return json({ error: "missing 'text'" }, 400)
  }

  if (!SUPABASE_URL || !SERVICE_ROLE_KEY) return json({ error: "server misconfigured" }, 500)
  const userId = userIdFromJwt(req)
  if (!userId) return json({ error: "unauthorized" }, 401)
  const gate = await consumeCredit(userId, "analyze")
  if (!gate.allowed) return json({ error: "quota_exceeded", ...gate.info }, 402)

  try {
    const analysis = await analyze(payload.text, payload.mood)
    const needsSupport = analysis.distress === "ELEVATED" || analysis.distress === "CRISIS"
    const [fb, coverImageUrl] = await Promise.all([
      needsSupport ? Promise.resolve(null) : feedback(analysis, payload.trend),
      generateCover(analysis, userId),
    ])
    const response: AnalyzeResponse = {
      analysis,
      feedback: fb?.reflection ?? null,
      question: fb?.question ?? null,
      coverImageUrl,
    }
    return json(response, 200)
  } catch (e) {
    return json({ error: `analysis failed: ${e instanceof Error ? e.message : String(e)}` }, 502)
  }
})
