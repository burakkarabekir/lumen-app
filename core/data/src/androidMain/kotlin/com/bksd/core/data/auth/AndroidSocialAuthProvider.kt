package com.bksd.core.data.auth

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.AuthErrorType
import com.bksd.core.domain.error.Result
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await

class AndroidSocialAuthProvider(
    private val webClientId: String,
) : SocialAuthProvider {

    override suspend fun signInWithGoogle(platformContext: Any?): Result<Unit, AppError> {
        val activity = platformContext as? Activity
            ?: return Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))

        return try {
            val idToken = getGoogleIdToken(activity)
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential).await()
            Result.Success(Unit)
        } catch (e: GetCredentialCancellationException) {
            println("[SocialAuth] Google sign-in cancelled: ${e.message}")
            Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_CANCELLED))
        } catch (e: Exception) {
            println("[SocialAuth] Google sign-in error: ${e.message}")
            Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))
        }
    }

    override suspend fun signInWithApple(platformContext: Any?): Result<Unit, AppError> {
        val activity = platformContext as? Activity
            ?: return Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))

        return try {
            val nativeAuth = FirebaseAuth.getInstance()

            val pending = nativeAuth.pendingAuthResult
            if (pending != null) {
                pending.await()
                return Result.Success(Unit)
            }

            val provider = OAuthProvider.newBuilder("apple.com")
                .setScopes(arrayOf("email", "name").toList())
                .build()

            nativeAuth.startActivityForSignInWithProvider(activity, provider).await()
            Result.Success(Unit)
        } catch (e: com.google.firebase.auth.FirebaseAuthException) {
            println("[SocialAuth] Apple sign-in FirebaseAuthException: ${e.errorCode} ${e.message}")
            Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))
        } catch (e: Exception) {
            val message = e.message.orEmpty()
            println("[SocialAuth] Apple sign-in error: $message")
            if (message.contains("canceled", ignoreCase = true) ||
                message.contains("cancelled", ignoreCase = true) ||
                message.contains("web context cancelled", ignoreCase = true)
            ) {
                Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_CANCELLED))
            } else {
                Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))
            }
        }
    }

    private suspend fun getGoogleIdToken(activity: Activity): String {
        val credentialManager = CredentialManager.create(activity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = try {
            credentialManager.getCredential(activity, request)
        } catch (e: NoCredentialException) {
            println("[SocialAuth] No credential found, falling back to Sign In With Google")
            val signInOption = GetSignInWithGoogleOption.Builder(webClientId)
                .build()

            val fallbackRequest = GetCredentialRequest.Builder()
                .addCredentialOption(signInOption)
                .build()

            credentialManager.getCredential(activity, fallbackRequest)
        }

        val credential = result.credential

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential.createFrom(credential.data).idToken
        }

        throw IllegalStateException("Unexpected credential type")
    }
}
