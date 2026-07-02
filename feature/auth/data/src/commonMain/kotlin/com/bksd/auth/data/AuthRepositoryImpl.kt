package com.bksd.auth.data

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.data.remote.supabase.SupabaseAuthDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authDataSource: SupabaseAuthDataSource,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit, AppError> {
        return authDataSource.signIn(email, password)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        fullName: String
    ): Result<Unit, AppError> {
        return authDataSource.signUp(email, password, fullName)
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit, AppError> {
        return authDataSource.signInWithGoogle(idToken)
    }

    override suspend fun resetPassword(email: String): Result<Unit, AppError> {
        return authDataSource.resetPassword(email)
    }

    override suspend fun signOut(): Result<Unit, AppError> {
        return authDataSource.signOut()
    }

    override fun getSignedInUserId(): String? {
        return authDataSource.getSignedInUserId()
    }

    override val authState: Flow<Boolean>
        get() = authDataSource.authState
}
