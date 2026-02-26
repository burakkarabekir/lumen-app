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
