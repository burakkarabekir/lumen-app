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

class MediatorMomentRepository(
    private val momentDao: MomentDao,
    private val remoteDataSource: FirestoreMomentRemoteDataSource,
    private val dtoToDomain: MomentDtoMapper,
    private val domainToDto: MomentToDtoMapper,
    private val entityToDomain: EntityToDomainMapper,
    private val domainToEntity: DomainToEntityMapper
) : MomentRepository {

    override fun observeMomentsPaged(limit: Int, offset: Int): Flow<List<Moment>> {
        return momentDao.observePaged(limit, offset)
            .map { entities -> entities.map { entityToDomain.map(it) } }
    }

    /**
     * Fetches [limit] moments from Firestore starting at [offset],
     * ordered by createdAtMs descending, and upserts them into Room.
     */
    override suspend fun syncMomentsPaged(limit: Int, offset: Int): Result<Unit, AppError> {
        return when (val result = remoteDataSource.fetchMomentsPaged(limit, offset)) {
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
    override suspend fun deleteMoment(id: String): Result<Unit, AppError> {
        // Delete locally first (offline-first)
        momentDao.deleteById(id)

        // Attempt remote delete
        return when (val result = remoteDataSource.deleteMoment(id)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> {
                Logger.w(TAG) { "Remote delete failed for moment $id: ${result.error}" }
                Result.Success(Unit)
            }
        }
    }

    companion object {
        private const val TAG = "MediatorMomentRepository"
    }
}
