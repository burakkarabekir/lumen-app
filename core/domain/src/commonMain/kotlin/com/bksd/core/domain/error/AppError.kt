package com.bksd.core.domain.error

sealed interface AppError {
    data class Network(val type: NetworkErrorType) : AppError
    data class Auth(val type: AuthErrorType) : AppError
    data class Media(val type: MediaErrorType) : AppError
    data class Unknown(val message: String) : AppError
}

enum class MediaErrorType {
    PERMISSION_DENIED,
    FILE_TOO_LARGE,
    UPLOAD_FAILED,
    RECORDING_FAILED,
    UNSUPPORTED_FORMAT
}

enum class NetworkErrorType {
    REQUEST_TIMEOUT,
    UNAUTHORIZED,
    CONFLICT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION
}

enum class AuthErrorType {
    INVALID_CREDENTIALS,
    USER_NOT_FOUND,
    ACCOUNT_EXISTS,
    WEAK_PASSWORD
}
