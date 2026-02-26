package com.bksd.core.data.remote.firebase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

class FirebaseAuthDataSource {
    private val auth by lazy { Firebase.auth }

    suspend fun signIn(email: String, password: String): Result<Unit, AppError> {
        return try {
            auth.signInWithEmailAndPassword(email, password)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapFirebaseError(e))
        }
    }

    suspend fun signUp(email: String, password: String): Result<Unit, AppError> {
        return try {
            auth.createUserWithEmailAndPassword(email, password)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapFirebaseError(e))
        }
    }

    suspend fun signOut(): Result<Unit, AppError> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapFirebaseError(e))
        }
    }

    suspend fun resetPassword(email: String): Result<Unit, AppError> {
        return try {
            auth.sendPasswordResetEmail(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapFirebaseError(e))
        }
    }

    fun getSignedInUserId(): String? {
        return auth.currentUser?.uid
    }

    suspend fun updateDisplayName(name: String): Result<Unit, AppError> {
        return try {
            auth.currentUser?.updateProfile(displayName = name)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapFirebaseError(e))
        }
    }

    private fun mapFirebaseError(e: Exception): AppError {
        val errorMessage = e.message ?: "Unknown authentication error"
        return AppError.Unknown(errorMessage)
    }
}
