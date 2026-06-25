package com.bksd.core.data.remote.supabase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.AuthErrorType
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> supabaseCall(block: suspend () -> T): Result<T, AppError> = try {
    Result.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    Result.Error(mapSupabaseError(e))
}

fun mapSupabaseError(e: Throwable): AppError {
    val msg = e.message?.lowercase().orEmpty()
    return when {
        "invalid login" in msg || "invalid credentials" in msg ||
            "invalid email or password" in msg -> AppError.Auth(AuthErrorType.INVALID_CREDENTIALS)

        "already registered" in msg || "already exists" in msg ||
            "user already" in msg -> AppError.Auth(AuthErrorType.ACCOUNT_EXISTS)

        "password" in msg && ("weak" in msg || "at least" in msg || "should be" in msg) ->
            AppError.Auth(AuthErrorType.WEAK_PASSWORD)

        "email not confirmed" in msg -> AppError.Auth(AuthErrorType.INVALID_CREDENTIALS)
        "user not found" in msg || "no user" in msg -> AppError.Auth(AuthErrorType.USER_NOT_FOUND)

        "unauthor" in msg || "permission" in msg || "denied" in msg ||
            "jwt" in msg || "row-level security" in msg -> AppError.Network(NetworkErrorType.UNAUTHORIZED)

        "timeout" in msg || "timed out" in msg -> AppError.Network(NetworkErrorType.REQUEST_TIMEOUT)
        "conflict" in msg || "duplicate" in msg -> AppError.Network(NetworkErrorType.CONFLICT)
        "rate" in msg || "too many" in msg -> AppError.Network(NetworkErrorType.TOO_MANY_REQUESTS)

        "network" in msg || "unable to resolve" in msg || "failed to connect" in msg ||
            "connection" in msg || "host" in msg -> AppError.Network(NetworkErrorType.NO_INTERNET)

        else -> AppError.Unknown(e.message ?: "Unknown error")
    }
}
