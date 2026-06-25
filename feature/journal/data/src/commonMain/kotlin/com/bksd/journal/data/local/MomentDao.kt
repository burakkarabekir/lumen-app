package com.bksd.journal.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MomentDao {

    @Query("SELECT * FROM moments WHERE createdAtMs >= :startMs AND createdAtMs < :endMs ORDER BY createdAtMs DESC")
    fun observeByDateRange(startMs: Long, endMs: Long): Flow<List<MomentEntity>>

    @Query("SELECT * FROM moments ORDER BY createdAtMs DESC LIMIT :limit OFFSET :offset")
    fun observePaged(limit: Int, offset: Int): Flow<List<MomentEntity>>

    @Query("SELECT * FROM moments WHERE id = :id")
    suspend fun getById(id: String): MomentEntity?

    @Upsert
    suspend fun upsert(moment: MomentEntity)

    @Upsert
    suspend fun upsertAll(moments: List<MomentEntity>)

    @Query("DELETE FROM moments WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM moments")
    suspend fun deleteAll()

    @Query("SELECT * FROM moments WHERE pendingSync = 1")
    suspend fun getPendingSync(): List<MomentEntity>
}
