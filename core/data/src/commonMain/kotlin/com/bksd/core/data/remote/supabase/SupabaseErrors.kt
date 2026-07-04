package com.bksd.core.data.remote.supabase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.AuthErrorType
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> supabaseCall(block: suspend () -> T): Result<T, AppError> = try {
    Result.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    Result.Error(mapSupabaseError(e))
}

fun mapSupabaseError(e: Throwable): AppError = when (e) {
    is AuthRestException -> mapAuthError(e)
    is RestException -> mapHttpStatus(e.statusCode)
    is HttpRequestTimeoutException -> AppError.Network(NetworkErrorType.REQUEST_TIMEOUT)
    is HttpRequestException -> AppError.Network(NetworkErrorType.NO_INTERNET)
    is SerializationException -> AppError.Network(NetworkErrorType.SERIALIZATION)
    else -> mapByMessage(e)
}

private fun mapAuthError(e: AuthRestException): AppError = when (e.errorCode) {
    AuthErrorCode.InvalidCredentials,
    AuthErrorCode.EmailNotConfirmed,
    AuthErrorCode.BadCodeVerifier -> AppError.Auth(AuthErrorType.INVALID_CREDENTIALS)

    AuthErrorCode.UserNotFound -> AppError.Auth(AuthErrorType.USER_NOT_FOUND)

    AuthErrorCode.UserAlreadyExists,
    AuthErrorCode.EmailExists,
    AuthErrorCode.PhoneExists,
    AuthErrorCode.IdentityAlreadyExists -> AppError.Auth(AuthErrorType.ACCOUNT_EXISTS)

    AuthErrorCode.WeakPassword,
    AuthErrorCode.SamePassword -> AppError.Auth(AuthErrorType.WEAK_PASSWORD)

    AuthErrorCode.OverRequestRateLimit,
    AuthErrorCode.OverEmailSendRateLimit,
    AuthErrorCode.OverSmsSendRateLimit -> AppError.Network(NetworkErrorType.TOO_MANY_REQUESTS)

    AuthErrorCode.RequestTimeout -> AppError.Network(NetworkErrorType.REQUEST_TIMEOUT)

    AuthErrorCode.NoAuthorization,
    AuthErrorCode.BadJwt,
    AuthErrorCode.SessionExpired,
    AuthErrorCode.SessionNotFound -> AppError.Network(NetworkErrorType.UNAUTHORIZED)

    else -> mapHttpStatus(e.statusCode)
}

private fun mapHttpStatus(status: Int): AppError = when (status) {
    401, 403 -> AppError.Network(NetworkErrorType.UNAUTHORIZED)
    408, 504 -> AppError.Network(NetworkErrorType.REQUEST_TIMEOUT)
    402 -> AppError.Network(NetworkErrorType.QUOTA_EXCEEDED)
    409 -> AppError.Network(NetworkErrorType.CONFLICT)
    429 -> AppError.Network(NetworkErrorType.TOO_MANY_REQUESTS)
    in 500..599 -> AppError.Network(NetworkErrorType.SERVER_ERROR)
    else -> AppError.Unknown("HTTP $status")
}

private fun mapByMessage(e: Throwable): AppError {
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
