package com.bksd.profile.data.repository

import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.data.remote.firebase.FirebaseFirestoreDataSource
import com.bksd.core.data.remote.firebase.FirebaseStorageDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import com.bksd.profile.data.dto.UserProfileDto
import com.bksd.profile.domain.model.UserProfile
import com.bksd.profile.domain.repository.ProfileRepository
import kotlin.random.Random

class ProfileRepositoryImpl(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val firestoreDataSource: FirebaseFirestoreDataSource,
    private val storageDataSource: FirebaseStorageDataSource,
) : ProfileRepository {

    override suspend fun getUserProfile(): Result<UserProfile, AppError> {
        val uid = firebaseAuthDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        val displayName = firebaseAuthDataSource.getDisplayName().orEmpty()
        val photoUrl = firebaseAuthDataSource.getPhotoUrl()

        val dto = when (val result = firestoreDataSource.getDocument(
            collectionPath = USERS_COLLECTION,
            documentId = uid,
            deserializer = UserProfileDto.serializer()
        )) {
            is Result.Success -> result.data ?: UserProfileDto()
            is Result.Error -> UserProfileDto()
        }

        return Result.Success(
            UserProfile(
                displayName = displayName,
                photoUrl = photoUrl,
                jobTitle = dto.jobTitle,
                joinYear = dto.joinYear,
                isPremium = dto.isPremium
            )
        )
    }

    override suspend fun uploadAvatar(bytes: ByteArray, mimeType: String?): Result<String, AppError> {
        val uid = firebaseAuthDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        val extension = when (mimeType) {
            "image/png" -> "png"
            "image/webp" -> "webp"
            else -> "jpg"
        }
        val uniqueSuffix = Random.nextLong(0, Long.MAX_VALUE)
        val fileName = "avatar_$uniqueSuffix.$extension"

        val contentType = mimeType ?: "image/jpeg"
        val remotePath = "$AVATARS_PATH/$uid/$fileName"
        return when (val result = storageDataSource.uploadBytes(bytes, remotePath, contentType)) {
            is Result.Success -> {
                val downloadUrl = result.data
                firebaseAuthDataSource.updatePhotoUrl(downloadUrl)
                Result.Success(downloadUrl)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun clearUserData() {
    }
}

private const val USERS_COLLECTION = "users"
private const val AVATARS_PATH = "avatars"
