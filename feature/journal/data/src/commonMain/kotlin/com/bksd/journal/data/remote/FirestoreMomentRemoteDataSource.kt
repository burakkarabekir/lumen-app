package com.bksd.journal.data.remote

import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.data.remote.firebase.FirebaseFirestoreDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result

class FirestoreMomentRemoteDataSource(
    private val firestoreDataSource: FirebaseFirestoreDataSource,
    private val authDataSource: FirebaseAuthDataSource,
) {

    private fun momentsCollectionPath(): String? {
        val userId = authDataSource.getSignedInUserId() ?: return null
        return "users/$userId/moments"
    }

    /**
     * Fetches a page of moments ordered by createdAtMs descending.
     * @param limit Number of items to fetch.
     * @param offset Number of items to skip.
     */
    suspend fun fetchMomentsPaged(limit: Int, offset: Int): Result<List<MomentDto>, AppError> {
        val collectionPath = momentsCollectionPath()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        return firestoreDataSource.queryDocumentsPaged(
            collectionPath = collectionPath,
            orderByField = "createdAtMs",
            limit = limit,
            offset = offset,
            deserializer = MomentDto.serializer()
        )
    }

    suspend fun fetchMoment(id: String): Result<MomentDto?, AppError> {
        val collectionPath = momentsCollectionPath()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        return firestoreDataSource.getDocument(
            collectionPath = collectionPath,
            documentId = id,
            deserializer = MomentDto.serializer()
        )
    }

    suspend fun saveMoment(dto: MomentDto): Result<Unit, AppError> {
        val collectionPath = momentsCollectionPath()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        return firestoreDataSource.setDocument(
            collectionPath = collectionPath,
            documentId = dto.id,
            data = dto,
            serializer = MomentDto.serializer()
        )
    }

    suspend fun deleteMoment(id: String): Result<Unit, AppError> {
        val collectionPath = momentsCollectionPath()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        return firestoreDataSource.deleteDocument(
            collectionPath = collectionPath,
            documentId = id
        )
    }
}
