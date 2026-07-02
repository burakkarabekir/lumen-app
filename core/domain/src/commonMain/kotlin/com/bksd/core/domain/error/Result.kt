package com.bksd.core.domain.error

sealed class Result<out D, out E : AppError> {
    data class Success<out D, out E : AppError>(val data: D) : Result<D, E>()
    data class Error<out D, out E : AppError>(val error: E) : Result<D, E>()
}

inline fun <T, E : AppError, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

inline fun <T, E : AppError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T, E : AppError> Result<T, E>.onFailure(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) action(error)
    return this
}

inline fun <T, E : AppError, R> Result<T, E>.fold(
    onSuccess: (T) -> R,
    onFailure: (E) -> R,
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onFailure(error)
}
