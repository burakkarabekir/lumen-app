package com.bksd.journal.data

import co.touchlab.kermit.Logger
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.data.local.DomainToEntityMapper
import com.bksd.journal.data.local.EntityToDomainMapper
import com.bksd.journal.data.local.MomentDao
import com.bksd.journal.data.remote.FirestoreMomentRemoteDataSource
import com.bksd.journal.data.remote.MomentDtoMapper
import com.bksd.journal.data.remote.MomentToDtoMapper
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Duration.Companion.days

class MediatorMomentRepository(
    private val momentDao: MomentDao,
    private val remoteDataSource: FirestoreMomentRemoteDataSource,
    private val timeZone: TimeZone,
    private val dtoToDomain: MomentDtoMapper,
    private val domainToDto: MomentToDtoMapper,
    private val entityToDomain: EntityToDomainMapper,
    private val domainToEntity: DomainToEntityMapper
) : MomentRepository {

    override fun observeMoments(date: LocalDate): Flow<List<Moment>> {
        val startMs = date.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val endMs = date.atStartOfDayIn(timeZone).plus(1.days).toEpochMilliseconds()

        return momentDao.observeByDateRange(startMs, endMs)
            .map { entities -> entities.map { entityToDomain.map(it) } }
    }

    override fun observeMomentsByRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Moment>> {
        val startMs = startDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val endMs = endDate.atStartOfDayIn(timeZone).plus(1.days).toEpochMilliseconds()

        return momentDao.observeByDateRange(startMs, endMs)
            .map { entities -> entities.map { entityToDomain.map(it) } }
    }

    override suspend fun syncMoments(date: LocalDate): Result<Unit, AppError> {
        return when (val result = remoteDataSource.fetchMoments(date)) {
            is Result.Success -> {
                val entities = result.data.map { dto ->
                    domainToEntity.map(dtoToDomain.map(dto))
                }
                momentDao.upsertAll(entities)
                Result.Success(Unit)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getMoment(id: String): Result<Moment, AppError> {
        // Local first
        val cached = momentDao.getById(id)
        if (cached != null) {
            return Result.Success(entityToDomain.map(cached))
        }

        // Fallback to remote
        return when (val result = remoteDataSource.fetchMoment(id)) {
            is Result.Success -> {
                val dto = result.data
                if (dto != null) {
                    val moment = dtoToDomain.map(dto)
                    momentDao.upsert(domainToEntity.map(moment))
                    Result.Success(moment)
                } else {
                    Result.Error(AppError.Unknown("Moment not found"))
                }
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun saveMoment(moment: Moment): Result<Unit, AppError> {
        // Write to local immediately (offline-first)
        momentDao.upsert(domainToEntity.map(moment.copy(pendingSync = true)))

        // Attempt remote sync
        return when (val result = remoteDataSource.saveMoment(domainToDto.map(moment))) {
            is Result.Success -> {
                // Mark as synced
                momentDao.upsert(domainToEntity.map(moment.copy(pendingSync = false)))
                Result.Success(Unit)
            }

            is Result.Error -> {
                // Local write persists, remote sync failed — data is still saved locally
                Logger.w(TAG) { "Remote sync failed for moment ${moment.id}: ${result.error}" }
                Result.Success(Unit)
            }
        }
    }

    companion object {
        private const val TAG = "MediatorMomentRepository"
    }
}
