import * as Sentry from "npm:@sentry/deno@8"

let started = false

function ensureInit(): boolean {
  const dsn = Deno.env.get("SENTRY_DSN")
  if (!dsn) return false
  if (!started) {
    Sentry.init({
      dsn,
      environment: Deno.env.get("SENTRY_ENVIRONMENT") ?? "production",
      tracesSampleRate: 0.1,
      beforeSend(event) {
        if (event.request) {
          delete event.request.data
          delete event.request.cookies
          delete event.request.headers
        }
        return event
      },
    })
    started = true
  }
  return true
}

export function withSentry(
  name: string,
  handler: (req: Request) => Promise<Response>,
): (req: Request) => Promise<Response> {
  const enabled = ensureInit()
  return async (req: Request) => {
    if (!enabled) return handler(req)
    try {
      const res = await handler(req)
      if (res.status >= 500) {
        Sentry.captureMessage(`${name} responded ${res.status}`, "error")
        await Sentry.flush(2000)
      }
      return res
    } catch (e) {
      Sentry.captureException(e, { tags: { function: name } })
      await Sentry.flush(2000)
      throw e
    }
  }
}
