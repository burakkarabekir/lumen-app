// Lumen — weekly reflection for the Insights "Weekly Reflection" card + screen.
// One warm generative pass over the past week's journal entries.
//
// Deploy:
//   supabase secrets set GEMINI_API_KEY=...        (shared with analyze-entry)
//   supabase functions deploy weekly-reflection
// JWT verification is ON by default — only signed-in users can call this.

const GEMINI_API_KEY = Deno.env.get("GEMINI_API_KEY")
const GEMINI_MODEL = Deno.env.get("GEMINI_MODEL") ?? "gemini-2.5-flash"
const GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models"

const corsHeaders: Record<string, string> = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
}

// Keep this shape in sync with the Kotlin WeeklyReflectionRequest / WeeklyReflectionDto.
interface WeeklyRequest {
  entries: string[]
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

const WEEKLY_SYSTEM = `
You are Lumen's reflective journaling companion. You receive several short daily
journal entries from one person's past week. Write a warm WEEKLY reflection.
Voice: warm, plain, grounded — like a perceptive friend, not a therapist, coach, or
chatbot persona. Match the language of the entries (Turkish or English).
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

Deno.serve(async (req: Request) => {
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

  const user = entries.map((e, i) => `Entry ${i + 1}:\n${e}`).join("\n\n")

  try {
    const raw = await callGemini(WEEKLY_SYSTEM, user, 700, 0.8)
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
    return json({ error: `weekly reflection failed: ${e instanceof Error ? e.message : String(e)}` }, 502)
  }
})
