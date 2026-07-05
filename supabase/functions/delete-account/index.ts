import { createClient, SupabaseClient } from "jsr:@supabase/supabase-js@2"
import { captureError, withSentry } from "../_shared/sentry.ts"

const SUPABASE_URL = Deno.env.get("SUPABASE_URL")
const SERVICE_ROLE_KEY = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")
const USER_BUCKETS = ["media", "avatars", "entry-covers"]

function json(body: unknown, status: number): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { "content-type": "application/json" },
  })
}

function uidFromJwt(req: Request): string | null {
  const token = (req.headers.get("Authorization") ?? "").replace(/^Bearer\s+/i, "")
  const parts = token.split(".")
  if (parts.length !== 3) return null
  try {
    const payload = JSON.parse(atob(parts[1].replace(/-/g, "+").replace(/_/g, "/")))
    return typeof payload.sub === "string" ? payload.sub : null
  } catch {
    return null
  }
}

async function removeFolder(supabase: SupabaseClient, bucket: string, prefix: string): Promise<void> {
  const { data: entries } = await supabase.storage.from(bucket).list(prefix, { limit: 1000 })
  if (!entries?.length) return
  const files: string[] = []
  for (const entry of entries) {
    const path = `${prefix}/${entry.name}`
    if (entry.id === null) {
      await removeFolder(supabase, bucket, path)
    } else {
      files.push(path)
    }
  }
  if (files.length) await supabase.storage.from(bucket).remove(files)
}

Deno.serve(withSentry("delete-account", async (req: Request) => {
  if (req.method !== "POST") return json({ error: "method_not_allowed" }, 405)
  if (!SUPABASE_URL || !SERVICE_ROLE_KEY) return json({ error: "server_misconfigured" }, 500)

  const uid = uidFromJwt(req)
  if (!uid) return json({ error: "unauthorized" }, 401)

  const supabase = createClient(SUPABASE_URL, SERVICE_ROLE_KEY, { auth: { persistSession: false } })

  for (const bucket of USER_BUCKETS) {
    try {
      await removeFolder(supabase, bucket, uid)
    } catch (_) {
      continue
    }
  }

  const { error } = await supabase.auth.admin.deleteUser(uid)
  if (error) {
    await captureError(error, { function: "delete-account" })
    return json({ error: "delete_failed", detail: error.message }, 500)
  }

  return json({ success: true }, 200)
}))
