package com.bksd.journal.data.remote

import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.data.remote.firebase.FirebaseFirestoreDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Duration.Companion.days

class FirestoreMomentRemoteDataSource(
    private val firestoreDataSource: FirebaseFirestoreDataSource,
    private val authDataSource: FirebaseAuthDataSource,
    private val timeZone: TimeZone
) {

    private fun momentsCollectionPath(): String? {
        val userId = authDataSource.getSignedInUserId() ?: return null
        return "users/$userId/moments"
    }

    suspend fun fetchMoments(date: LocalDate): Result<List<MomentDto>, AppError> {
        val collectionPath = momentsCollectionPath()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        val startMs = date.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val endMs = date.atStartOfDayIn(timeZone).plus(1.days).toEpochMilliseconds()

        return firestoreDataSource.queryDocuments(
            collectionPath = collectionPath,
            field = "createdAtMs",
            greaterThanOrEqual = startMs,
            lessThan = endMs,
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
}
