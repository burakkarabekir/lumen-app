package com.bksd.auth.data

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val profileRepository: ProfileRepository,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit, AppError> {
        return firebaseAuthDataSource.signIn(email, password)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        fullName: String
    ): Result<Unit, AppError> {
        val signUpResult = firebaseAuthDataSource.signUp(email, password)
        if (signUpResult is Result.Error) return signUpResult
        return firebaseAuthDataSource.updateDisplayName(fullName)
    }

    override suspend fun resetPassword(email: String): Result<Unit, AppError> {
        return firebaseAuthDataSource.resetPassword(email)
    }

    override suspend fun signInWithGoogle(platformContext: Any?): Result<Unit, AppError> {
        return firebaseAuthDataSource.signInWithGoogle(platformContext)
    }

    override suspend fun signInWithApple(platformContext: Any?): Result<Unit, AppError> {
        return firebaseAuthDataSource.signInWithApple(platformContext)
    }

    override suspend fun signOut(): Result<Unit, AppError> {
        return firebaseAuthDataSource.signOut()
    }

    override suspend fun clearUserData() {
        profileRepository.clearUserData()
    }

    override fun getSignedInUserId(): String? {
        return firebaseAuthDataSource.getSignedInUserId()
    }

    override val authState: Flow<Boolean>
        get() = firebaseAuthDataSource.authState
}
