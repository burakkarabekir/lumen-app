package com.bksd.core.data.remote.firebase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirebaseAuthDataSource {
    private val auth by lazy { Firebase.auth }

    val authState: Flow<Boolean> = flow {
        auth.authStateChanged.collect { user ->
            val isLoggedIn = user != null
            println("[FirebaseAuth] authStateChanged: isLoggedIn=$isLoggedIn, uid=${user?.uid}")
            emit(isLoggedIn)
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit, AppError> {
        println("[FirebaseAuth] signIn: email=$email")
        return try {
            auth.signInWithEmailAndPassword(email, password)
            println("[FirebaseAuth] signIn: success")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("[FirebaseAuth] signIn: error=${e.message}")
            Result.Error(mapFirebaseError(e))
        }
    }

    suspend fun signUp(email: String, password: String): Result<Unit, AppError> {
        println("[FirebaseAuth] signUp: email=$email")
        return try {
            auth.createUserWithEmailAndPassword(email, password)
            println("[FirebaseAuth] signUp: success")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("[FirebaseAuth] signUp: error=${e.message}")
            Result.Error(mapFirebaseError(e))
        }
    }

    suspend fun signOut(): Result<Unit, AppError> {
        println("[FirebaseAuth] signOut")
        return try {
            auth.signOut()
            println("[FirebaseAuth] signOut: success")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("[FirebaseAuth] signOut: error=${e.message}")
            Result.Error(mapFirebaseError(e))
        }
    }

    suspend fun resetPassword(email: String): Result<Unit, AppError> {
        println("[FirebaseAuth] resetPassword: email=$email")
        return try {
            auth.sendPasswordResetEmail(email)
            println("[FirebaseAuth] resetPassword: success")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("[FirebaseAuth] resetPassword: error=${e.message}")
            Result.Error(mapFirebaseError(e))
        }
    }

    fun getSignedInUserId(): String? {
        val uid = auth.currentUser?.uid
        println("[FirebaseAuth] getSignedInUserId: $uid")
        return uid
    }

    fun getDisplayName(): String? {
        val name = auth.currentUser?.displayName
        println("[FirebaseAuth] getDisplayName: $name")
        return name
    }

    fun getPhotoUrl(): String? {
        val url = auth.currentUser?.photoURL
        println("[FirebaseAuth] getPhotoUrl: $url")
        return url
    }

    suspend fun updateDisplayName(name: String): Result<Unit, AppError> {
        println("[FirebaseAuth] updateDisplayName: $name")
        return try {
            auth.currentUser?.updateProfile(displayName = name)
            println("[FirebaseAuth] updateDisplayName: success")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("[FirebaseAuth] updateDisplayName: error=${e.message}")
            Result.Error(mapFirebaseError(e))
        }
    }

    suspend fun updatePhotoUrl(url: String): Result<Unit, AppError> {
        println("[FirebaseAuth] updatePhotoUrl: $url")
        return try {
            auth.currentUser?.updateProfile(photoUrl = url)
            println("[FirebaseAuth] updatePhotoUrl: success")
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(mapFirebaseError(e))
        }
    }
    private fun mapFirebaseError(e: Exception): AppError {
        val errorMessage = e.message ?: "Unknown authentication error"
        println("[FirebaseAuth] updatePhotoUrl: error=$errorMessage")
        return AppError.Unknown(errorMessage)
    }
}
