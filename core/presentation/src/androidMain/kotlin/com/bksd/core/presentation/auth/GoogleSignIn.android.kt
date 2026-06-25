package com.bksd.core.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

private const val WEB_CLIENT_ID =
    "139243147783-ed26stullk4stb6405ivu5pq3ua1t3t1.apps.googleusercontent.com"

@Composable
actual fun rememberGoogleSignInLauncher(
    onResult: (GoogleSignInResult) -> Unit
): GoogleSignInLauncher {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    return remember(context) {
        GoogleSignInLauncher {
            scope.launch {
                try {
                    val credentialManager = CredentialManager.create(context)
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(WEB_CLIENT_ID)
                        .build()
                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()
                    val response = credentialManager.getCredential(context, request)
                    val credential = response.credential
                    if (credential is CustomCredential &&
                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    ) {
                        val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        onResult(GoogleSignInResult.Success(googleCredential.idToken))
                    } else {
                        onResult(GoogleSignInResult.Failed("Unexpected credential type"))
                    }
                } catch (e: GetCredentialCancellationException) {
                    onResult(GoogleSignInResult.Cancelled)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    onResult(GoogleSignInResult.Failed(e.message ?: "Google sign-in failed"))
                }
            }
        }
    }
}
