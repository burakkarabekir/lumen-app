package com.bksd.auth.data

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

/**
 * Implementation of [AuthRepository] using the generic [FirebaseAuthDataSource].
 * This acts as a pure boundary preventing Firebase SDKs from leaking into the feature layer.
 */
class AuthRepositoryImpl(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
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

        // After successful sign up, update the profile name immediately
        return firebaseAuthDataSource.updateDisplayName(fullName)
    }

    override suspend fun resetPassword(email: String): Result<Unit, AppError> {
        return firebaseAuthDataSource.resetPassword(email)
    }

    override suspend fun signOut(): Result<Unit, AppError> {
        return firebaseAuthDataSource.signOut()
    }

    override fun getSignedInUserId(): String? {
        return firebaseAuthDataSource.getSignedInUserId()
    }
}
