package com.bksd.journal.data

import co.touchlab.kermit.Logger
import com.bksd.core.data.remote.supabase.SupabaseAuthDataSource
import com.bksd.core.data.remote.supabase.SupabaseBuckets
import com.bksd.core.data.remote.supabase.SupabaseStorageDataSource
import com.bksd.core.domain.cleanup.LocalDataCleaner
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.domain.repository.MomentRepository
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.journal.data.local.DomainToEntityMapper
import com.bksd.journal.data.local.EntityToDomainMapper
import com.bksd.journal.data.local.MomentDao
import com.bksd.journal.data.remote.MomentDtoMapper
import com.bksd.journal.data.remote.MomentToDtoMapper
import com.bksd.journal.data.remote.SupabaseMomentRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class MediatorMomentRepository(
    private val momentDao: MomentDao,
    private val remoteDataSource: SupabaseMomentRemoteDataSource,
    private val storageDataSource: SupabaseStorageDataSource,
    private val authDataSource: SupabaseAuthDataSource,
    private val sessionStorage: SessionStorage,
    private val dtoToDomain: MomentDtoMapper,
    private val domainToDto: MomentToDtoMapper,
    private val entityToDomain: EntityToDomainMapper,
    private val domainToEntity: DomainToEntityMapper,
    private val clock: Clock
) : MomentRepository, LocalDataCleaner {

    private val signedUrlCache = mutableMapOf<String, CachedUrl>()
    private val cacheMutex = Mutex()

    override fun observeMomentsPaged(limit: Int, offset: Int): Flow<List<Moment>> {
        return momentDao.observePaged(limit, offset)
            .map { entities -> entities.map { entityToDomain.map(it) } }
            .map { moments -> moments.map { it.withSignedMedia() } }
    }

    /**
     * Fetches [limit] moments from Supabase starting at [offset],
     * ordered by createdAtMs descending, and upserts them into Room.
     */
    override suspend fun syncMomentsPaged(limit: Int, offset: Int): Result<Unit, AppError> {
        ensureLocalOwner()
        flushPendingSync()
        return when (val result = remoteDataSource.fetchMomentsPaged(limit, offset)) {
            is Result.Success -> {
                val pendingIds = (momentDao.getPendingSync().map { it.id } +
                        momentDao.getPendingDelete().map { it.id }).toSet()
                val entities = result.data
                    .filterNot { it.id in pendingIds }
                    .map { dto -> domainToEntity.map(dtoToDomain.map(dto)) }
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
            return Result.Success(entityToDomain.map(cached).withSignedMedia())
        }

        // Fallback to remote
        return when (val result = remoteDataSource.fetchMoment(id)) {
            is Result.Success -> {
                val dto = result.data
                if (dto != null) {
                    val moment = dtoToDomain.map(dto)
                    momentDao.upsert(domainToEntity.map(moment))
                    Result.Success(moment.withSignedMedia())
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
                Logger.w(tag = TAG) { "Remote sync failed for moment ${moment.id}: ${result.error}" }
                Result.Success(Unit)
            }
        }
    }

    override suspend fun deleteMoment(id: String): Result<Unit, AppError> {
        momentDao.markPendingDelete(id)
        return when (val result = remoteDataSource.deleteMoment(id)) {
            is Result.Success -> {
                momentDao.deleteById(id)
                Result.Success(Unit)
            }

            is Result.Error -> {
                Logger.w(tag = TAG) { "Remote delete failed for moment $id; kept as tombstone: ${result.error}" }
                Result.Success(Unit)
            }
        }
    }

    private suspend fun flushPendingSync() {
        momentDao.getPendingSync().forEach { entity ->
            val moment = entityToDomain.map(entity)
            if (remoteDataSource.saveMoment(domainToDto.map(moment)) is Result.Success) {
                momentDao.markSynced(entity.id)
            }
        }
        momentDao.getPendingDelete().forEach { entity ->
            if (remoteDataSource.deleteMoment(entity.id) is Result.Success) {
                momentDao.deleteById(entity.id)
            }
        }
    }

    private suspend fun Moment.withSignedMedia(): Moment {
        if (attachments.isEmpty()) return this
        ensureSignedUrls(attachments.mapNotNull { it.storagePath() })
        return copy(attachments = attachments.map { it.withSignedUrl() })
    }

    private fun Attachment.storagePath(): String? {
        val raw = when (this) {
            is PhotoAttachment -> remoteUrl.value
            is VideoAttachment -> remoteUrl.value
            is AudioAttachment -> remoteUrl.value
            is LinkAttachment -> null
        }
        return raw?.takeUnless { it.startsWith("http") }
    }

    private suspend fun ensureSignedUrls(paths: List<String>) {
        if (paths.isEmpty()) return
        val nowMs = clock.now().toEpochMilliseconds()
        val missing = cacheMutex.withLock {
            paths.distinct().filter { path ->
                val cached = signedUrlCache[path]
                cached == null || nowMs + REFRESH_MARGIN_MS >= cached.expiresAtMs
            }
        }
        if (missing.isEmpty()) return

        val result = storageDataSource.signedUrls(SupabaseBuckets.MEDIA, missing, SIGNED_URL_TTL)
        if (result is Result.Success) {
            val expiresAtMs = nowMs + SIGNED_URL_TTL.inWholeMilliseconds
            cacheMutex.withLock {
                result.data.forEach { signed ->
                    signedUrlCache[signed.path] = CachedUrl(signed.signedURL, expiresAtMs)
                }
            }
        }
    }

    private suspend fun Attachment.withSignedUrl(): Attachment = when (this) {
        is PhotoAttachment -> copy(remoteUrl = signedUrlFor(remoteUrl))
        is VideoAttachment -> copy(remoteUrl = signedUrlFor(remoteUrl))
        is AudioAttachment -> copy(remoteUrl = signedUrlFor(remoteUrl))
        is LinkAttachment -> this
    }

    private suspend fun signedUrlFor(stored: Url): Url {
        if (stored.value.startsWith("http")) return stored
        val cached = cacheMutex.withLock { signedUrlCache[stored.value] }
        return cached?.let { Url(it.url) } ?: stored
    }

    override suspend fun clearLocalData() {
        momentDao.deleteAll()
        cacheMutex.withLock { signedUrlCache.clear() }
    }

    private suspend fun ensureLocalOwner() {
        val uid = authDataSource.getSignedInUserId() ?: return
        if (sessionStorage.getLocalDataOwner() != uid) {
            clearLocalData()
            sessionStorage.setLocalDataOwner(uid)
        }
    }

    private data class CachedUrl(val url: String, val expiresAtMs: Long)

    companion object {
        private const val TAG = "MediatorMomentRepository"
        private val SIGNED_URL_TTL = 7.days
        private val REFRESH_MARGIN_MS = 1.hours.inWholeMilliseconds
    }
}
