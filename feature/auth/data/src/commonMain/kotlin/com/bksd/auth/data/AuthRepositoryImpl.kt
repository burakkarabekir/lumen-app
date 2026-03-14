package com.bksd.auth.data

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.SessionStorage
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit, AppError> {
        val result = firebaseAuthDataSource.signIn(email, password)
        if (result is Result.Success) {
            sessionStorage.set(true)
        }
        return result
    }

    override suspend fun signUp(
        email: String,
        password: String,
        fullName: String
    ): Result<Unit, AppError> {
        val signUpResult = firebaseAuthDataSource.signUp(email, password)
        if (signUpResult is Result.Error) return signUpResult

        val updateResult = firebaseAuthDataSource.updateDisplayName(fullName)
        if (updateResult is Result.Success) {
            sessionStorage.set(true)
        }
        return updateResult
    }

    override suspend fun resetPassword(email: String): Result<Unit, AppError> {
        return firebaseAuthDataSource.resetPassword(email)
    }

    override suspend fun signOut(): Result<Unit, AppError> {
        val result = firebaseAuthDataSource.signOut()
        if (result is Result.Success) {
            sessionStorage.set(false)
        }
        return result
    }

    override fun getSignedInUserId(): String? {
        return firebaseAuthDataSource.getSignedInUserId()
    }

    override val authState: Flow<Boolean>
        get() = firebaseAuthDataSource.authState
}
