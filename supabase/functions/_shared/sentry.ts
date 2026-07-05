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

export async function captureError(
  error: unknown,
  tags: Record<string, string> = {},
): Promise<void> {
  if (!ensureInit()) return
  Sentry.captureException(error, { tags })
  await Sentry.flush(2000)
}

export function withSentry(
  name: string,
  handler: (req: Request) => Promise<Response>,
): (req: Request) => Promise<Response> {
  const enabled = ensureInit()
  return async (req: Request) => {
    if (!enabled) return handler(req)
    try {
      return await handler(req)
    } catch (e) {
      Sentry.captureException(e, { tags: { function: name } })
      await Sentry.flush(2000)
      throw e
    }
  }
}
