package com.bksd.core.data.auth

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.AuthErrorType
import com.bksd.core.domain.error.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.OAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AuthenticationServices.ASAuthorization
import platform.AuthenticationServices.ASAuthorizationAppleIDCredential
import platform.AuthenticationServices.ASAuthorizationAppleIDProvider
import platform.AuthenticationServices.ASAuthorizationController
import platform.AuthenticationServices.ASAuthorizationControllerDelegateProtocol
import platform.AuthenticationServices.ASAuthorizationControllerPresentationContextProvidingProtocol
import platform.AuthenticationServices.ASAuthorizationScopeEmail
import platform.AuthenticationServices.ASAuthorizationScopeFullName
import platform.AuthenticationServices.ASPresentationAnchor
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.posix.uint8_tVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
class IosSocialAuthProvider : SocialAuthProvider {

    override suspend fun signInWithGoogle(platformContext: Any?): Result<Unit, AppError> {
        return Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))
    }

    override suspend fun signInWithApple(platformContext: Any?): Result<Unit, AppError> {
        return try {
            val credential = getAppleIdToken()
            when (credential) {
                is Result.Success -> {
                    val (idToken, nonce) = credential.data
                    val oauthCredential = OAuthProvider.credential(
                        providerId = "apple.com",
                        idToken = idToken,
                        rawNonce = nonce
                    )
                    Firebase.auth.signInWithCredential(oauthCredential)
                    Result.Success(Unit)
                }
                is Result.Error -> credential
            }
        } catch (e: Exception) {
            println("[SocialAuth] Apple sign-in Firebase error: ${e.message}")
            Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))
        }
    }

    private suspend fun getAppleIdToken(): Result<Pair<String, String>, AppError> {
        return suspendCoroutine { continuation ->
            val nonce = generateNonce()
            val hashedNonce = sha256(nonce)

            val appleIDProvider = ASAuthorizationAppleIDProvider()
            val request = appleIDProvider.createRequest().apply {
                requestedScopes = listOf(ASAuthorizationScopeFullName, ASAuthorizationScopeEmail)
                this.nonce = hashedNonce
            }

            val controller = ASAuthorizationController(authorizationRequests = listOf(request))

            val delegate = object : NSObject(),
                ASAuthorizationControllerDelegateProtocol,
                ASAuthorizationControllerPresentationContextProvidingProtocol {

                override fun authorizationController(
                    controller: ASAuthorizationController,
                    didCompleteWithAuthorization: ASAuthorization
                ) {
                    val appleCredential =
                        didCompleteWithAuthorization.credential as? ASAuthorizationAppleIDCredential
                    if (appleCredential == null) {
                        continuation.resume(
                            Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))
                        )
                        return
                    }

                    val identityTokenData = appleCredential.identityToken
                    if (identityTokenData == null) {
                        continuation.resume(
                            Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))
                        )
                        return
                    }

                    val idToken = NSString.create(
                        data = identityTokenData,
                        encoding = NSUTF8StringEncoding
                    ) as? String

                    if (idToken == null) {
                        continuation.resume(
                            Result.Error(AppError.Auth(AuthErrorType.SOCIAL_LOGIN_FAILED))
                        )
                        return
                    }

                    continuation.resume(Result.Success(Pair(idToken, nonce)))
                }

                override fun authorizationController(
                    controller: ASAuthorizationController,
                    didCompleteWithError: NSError
                ) {
                    val errorType = if (didCompleteWithError.code == 1001L) {
                        AuthErrorType.SOCIAL_LOGIN_CANCELLED
                    } else {
                        AuthErrorType.SOCIAL_LOGIN_FAILED
                    }
                    continuation.resume(Result.Error(AppError.Auth(errorType)))
                }

                override fun presentationAnchorForAuthorizationController(
                    controller: ASAuthorizationController
                ): ASPresentationAnchor {
                    return UIApplication.sharedApplication.keyWindow!!
                }
            }

            controller.delegate = delegate
            controller.presentationContextProvider = delegate
            controller.performRequests()
        }
    }

    private fun generateNonce(length: Int = 32): String {
        val charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._"
        return memScoped {
            val bytes = allocArray<uint8_tVar>(length)
            platform.Security.SecRandomCopyBytes(
                platform.Security.kSecRandomDefault,
                length.toULong(),
                bytes
            )
            (0 until length).map { charset[bytes[it].toInt() % charset.length] }.joinToString("")
        }
    }

    private fun sha256(input: String): String {
        val data = input.encodeToByteArray()
        val digest = ByteArray(32)
        data.usePinned { pinnedData ->
            digest.usePinned { pinnedDigest ->
                platform.CommonCrypto.CC_SHA256(
                    pinnedData.addressOf(0),
                    data.size.toUInt(),
                    pinnedDigest.addressOf(0)
                )
            }
        }
        return digest.joinToString("") { "%02x".format(it) }
    }
}
