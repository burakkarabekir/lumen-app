// Lumen — RevenueCat webhook → Supabase subscriptions sync.
// RevenueCat POSTs subscription lifecycle events here; we mirror them into
// public.subscriptions (the server's source of truth for entitlement) and
// public.profiles.is_premium (denormalized convenience mirror).
//
// Auth: RevenueCat sends a FIXED Authorization header value that you set in the
// RevenueCat dashboard (Integrations > Webhooks > Authorization header). We
// compare it to REVENUECAT_WEBHOOK_SECRET. verify_jwt MUST be off — RC does not
// send a Supabase JWT.
//
// Deploy:
//   supabase secrets set REVENUECAT_WEBHOOK_SECRET=<paste the same value you put in the RC dashboard>
//   supabase functions deploy revenuecat-webhook --no-verify-jwt
// SUPABASE_URL / SUPABASE_SERVICE_ROLE_KEY are injected automatically at runtime.

import { createClient } from "jsr:@supabase/supabase-js@2"
import { withSentry } from "../_shared/sentry.ts"

const SECRET = Deno.env.get("REVENUECAT_WEBHOOK_SECRET")
const SUPABASE_URL = Deno.env.get("SUPABASE_URL")
const SERVICE_ROLE_KEY = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")

const UUID_RE = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i

interface RcEvent {
  type?: string
  app_user_id?: string
  original_app_user_id?: string
  entitlement_ids?: string[] | null
  entitlement_id?: string | null
  product_id?: string | null
  store?: string | null
  period_type?: string | null
  expiration_at_ms?: number | null
  environment?: string | null
}

function json(body: unknown, status: number): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json" } })
}

function mapStore(s?: string | null): string | null {
  if (!s) return null
  switch (s.toUpperCase()) {
    case "APP_STORE":
    case "MAC_APP_STORE": return "app_store"
    case "PLAY_STORE": return "play_store"
    case "STRIPE": return "stripe"
    case "PROMOTIONAL": return "promotional"
    case "AMAZON": return "amazon"
    default: return s.toLowerCase()
  }
}

interface SubRow {
  entitlement: string | null
  status: string
  product_id: string | null
  store: string | null
  period_end: string | null
}

// Returns the row fields to upsert, or null to ignore the event.
function rowFor(ev: RcEvent): SubRow | null {
  const type = (ev.type ?? "").toUpperCase()
  const entitlement = ev.entitlement_ids?.[0] ?? ev.entitlement_id ?? "plus"
  const periodEnd = typeof ev.expiration_at_ms === "number"
    ? new Date(ev.expiration_at_ms).toISOString()
    : null
  const isTrial = (ev.period_type ?? "").toUpperCase() === "TRIAL"

  let status: string
  switch (type) {
    case "TEST":
      return null // dashboard "send test event" ping
    case "EXPIRATION":
      status = "expired"; break
    case "BILLING_ISSUE":
      status = "in_grace_period"; break
    case "SUBSCRIPTION_PAUSED":
      status = "paused"; break
    case "CANCELLATION":
      // auto-renew turned off; access continues until period_end
      status = "active"; break
    case "INITIAL_PURCHASE":
    case "RENEWAL":
    case "UNCANCELLATION":
    case "PRODUCT_CHANGE":
    case "NON_RENEWING_PURCHASE":
    case "SUBSCRIPTION_EXTENDED":
    case "TRANSFER":
    default:
      status = isTrial ? "trialing" : "active"; break
  }

  return {
    entitlement: status === "expired" ? null : entitlement,
    status,
    product_id: ev.product_id ?? null,
    store: mapStore(ev.store),
    period_end: periodEnd,
  }
}

function isActive(status: string, periodEnd: string | null): boolean {
  if (!["active", "trialing", "in_grace_period"].includes(status)) return false
  if (periodEnd && new Date(periodEnd).getTime() <= Date.now()) return false
  return true
}

Deno.serve(withSentry("revenuecat-webhook", async (req: Request) => {
  if (req.method !== "POST") return json({ error: "method not allowed" }, 405)
  if (!SECRET) return json({ error: "server misconfigured: missing REVENUECAT_WEBHOOK_SECRET" }, 500)
  if (req.headers.get("Authorization") !== SECRET) return json({ error: "unauthorized" }, 401)
  if (!SUPABASE_URL || !SERVICE_ROLE_KEY) return json({ error: "server misconfigured" }, 500)

  let body: { event?: RcEvent }
  try {
    body = await req.json()
  } catch {
    return json({ error: "invalid json" }, 400)
  }
  const ev = body?.event
  if (!ev || typeof ev !== "object") return json({ error: "missing event" }, 400)

  const appUserId = ev.app_user_id ?? ""
  // We set the RevenueCat appUserID = Supabase auth uid. Ignore anonymous / non-uuid ids.
  if (!UUID_RE.test(appUserId)) {
    console.log(`skip: non-uuid app_user_id "${appUserId}" (type=${ev.type})`)
    return json({ ok: true, skipped: "non_uuid_app_user_id" }, 200)
  }

  const row = rowFor(ev)
  if (!row) return json({ ok: true, skipped: "ignored_event" }, 200)

  const supabase = createClient(SUPABASE_URL, SERVICE_ROLE_KEY, { auth: { persistSession: false } })

  const { error: upErr } = await supabase.from("subscriptions").upsert({
    user_id: appUserId,
    rc_app_user_id: appUserId,
    entitlement: row.entitlement,
    status: row.status,
    product_id: row.product_id,
    store: row.store,
    period_end: row.period_end,
    updated_at: new Date().toISOString(),
  }, { onConflict: "user_id" })
  if (upErr) {
    console.error("subscriptions upsert failed:", upErr.message)
    return json({ error: "db upsert failed" }, 500)
  }

  const premium = isActive(row.status, row.period_end)
  const { error: profErr } = await supabase.from("profiles").update({ is_premium: premium }).eq("id", appUserId)
  if (profErr) console.error("profiles mirror failed:", profErr.message)

  return json({ ok: true, user_id: appUserId, status: row.status, premium }, 200)
}))
