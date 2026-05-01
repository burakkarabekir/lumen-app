package com.bksd.auth.domain

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit, AppError>
    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit, AppError>
    suspend fun resetPassword(email: String): Result<Unit, AppError>
    suspend fun signOut(): Result<Unit, AppError>
    suspend fun clearUserData()
    fun getSignedInUserId(): String?
    val authState: Flow<Boolean>
}
