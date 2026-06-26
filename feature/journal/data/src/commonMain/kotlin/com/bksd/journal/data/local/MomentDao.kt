package com.bksd.journal.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MomentDao {

    @Query("SELECT * FROM moments WHERE createdAtMs >= :startMs AND createdAtMs < :endMs AND pendingDelete = 0 ORDER BY createdAtMs DESC")
    fun observeByDateRange(startMs: Long, endMs: Long): Flow<List<MomentEntity>>

    @Query("SELECT * FROM moments WHERE pendingDelete = 0 ORDER BY createdAtMs DESC LIMIT :limit OFFSET :offset")
    fun observePaged(limit: Int, offset: Int): Flow<List<MomentEntity>>

    @Query("SELECT * FROM moments WHERE id = :id AND pendingDelete = 0")
    suspend fun getById(id: String): MomentEntity?

    @Upsert
    suspend fun upsert(moment: MomentEntity)

    @Upsert
    suspend fun upsertAll(moments: List<MomentEntity>)

    @Query("UPDATE moments SET pendingSync = 0 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("UPDATE moments SET pendingDelete = 1 WHERE id = :id")
    suspend fun markPendingDelete(id: String)

    @Query("DELETE FROM moments WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM moments")
    suspend fun deleteAll()

    @Query("SELECT * FROM moments WHERE pendingSync = 1 AND pendingDelete = 0")
    suspend fun getPendingSync(): List<MomentEntity>

    @Query("SELECT * FROM moments WHERE pendingDelete = 1")
    suspend fun getPendingDelete(): List<MomentEntity>
}
