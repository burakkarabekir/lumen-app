package com.bksd.journal.data.remote

import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.data.remote.firebase.FirebaseFirestoreDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Duration.Companion.days

class FirestoreMomentRepository(
    private val firestoreDataSource: FirebaseFirestoreDataSource,
    private val authDataSource: FirebaseAuthDataSource
) : MomentRepository {

    private fun momentsCollectionPath(): String? {
        val userId = authDataSource.getSignedInUserId() ?: return null
        return "users/$userId/moments"
    }

    override suspend fun getMoments(date: LocalDate): Result<List<Moment>, AppError> {
        val collectionPath = momentsCollectionPath()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        val tz = TimeZone.currentSystemDefault()
        val startMs = date.atStartOfDayIn(tz).toEpochMilliseconds()
        val endMs = date.atStartOfDayIn(tz).plus(1.days).toEpochMilliseconds()

        return when (
            val result = firestoreDataSource.queryDocuments(
                collectionPath = collectionPath,
                field = "createdAtMs",
                greaterThanOrEqual = startMs,
                lessThan = endMs,
                deserializer = MomentDto.serializer()
            )
        ) {
            is Result.Success -> Result.Success(result.data.map { it.toDomain() })
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getMoment(id: String): Result<Moment, AppError> {
        val collectionPath = momentsCollectionPath()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        return when (
            val result = firestoreDataSource.getDocument(
                collectionPath = collectionPath,
                documentId = id,
                deserializer = MomentDto.serializer()
            )
        ) {
            is Result.Success -> {
                val dto = result.data
                if (dto != null) {
                    Result.Success(dto.toDomain())
                } else {
                    Result.Error(AppError.Unknown("Moment not found"))
                }
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun saveMoment(moment: Moment): Result<Unit, AppError> {
        val collectionPath = momentsCollectionPath()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        val dto = moment.toDto()
        return firestoreDataSource.setDocument(
            collectionPath = collectionPath,
            documentId = moment.id,
            data = dto,
            serializer = MomentDto.serializer()
        )
    }
}
