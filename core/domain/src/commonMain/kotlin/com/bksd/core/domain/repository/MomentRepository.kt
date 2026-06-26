package com.bksd.core.domain.repository

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.Moment
import kotlinx.coroutines.flow.Flow

interface MomentRepository {
    fun observeMomentsPaged(limit: Int, offset: Int): Flow<List<Moment>>
    suspend fun syncMomentsPaged(limit: Int, offset: Int): Result<Unit, AppError>
    suspend fun getMoment(id: String): Result<Moment, AppError>
    suspend fun saveMoment(moment: Moment): Result<Unit, AppError>
    suspend fun deleteMoment(id: String): Result<Unit, AppError>
}
