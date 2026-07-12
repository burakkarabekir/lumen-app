package com.bksd.auth.data

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.data.remote.supabase.SupabaseAuthDataSource
import com.bksd.core.domain.applock.AppLockRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authDataSource: SupabaseAuthDataSource,
    private val appLockRepository: AppLockRepository,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit, AppError> =
        authDataSource.signIn(email, password).resetAppLockOnSuccess()

    override suspend fun signUp(
        email: String,
        password: String,
        fullName: String
    ): Result<Unit, AppError> =
        authDataSource.signUp(email, password, fullName).resetAppLockOnSuccess()

    override suspend fun signInWithGoogle(idToken: String): Result<Unit, AppError> =
        authDataSource.signInWithGoogle(idToken).resetAppLockOnSuccess()

    override suspend fun signInWithApple(idToken: String, nonce: String?): Result<Unit, AppError> =
        authDataSource.signInWithApple(idToken, nonce).resetAppLockOnSuccess()

    override suspend fun resetPassword(email: String): Result<Unit, AppError> {
        return authDataSource.resetPassword(email)
    }

    override suspend fun signOut(): Result<Unit, AppError> {
        return authDataSource.signOut()
    }

    override suspend fun deleteAccount(): Result<Unit, AppError> {
        return authDataSource.deleteAccount()
    }

    override fun getSignedInUserId(): String? {
        return authDataSource.getSignedInUserId()
    }

    override fun getDisplayName(): String? {
        return authDataSource.getDisplayName()
    }

    override val authState: Flow<Boolean>
        get() = authDataSource.authState

    private suspend fun Result<Unit, AppError>.resetAppLockOnSuccess(): Result<Unit, AppError> =
        also { if (it is Result.Success) appLockRepository.setAppLockEnabled(false) }
}
