package com.bksd.journal.domain.repository

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface MomentRepository {
    fun observeMoments(date: LocalDate): Flow<List<Moment>>
    suspend fun syncMoments(date: LocalDate): Result<Unit, AppError>
    suspend fun getMoment(id: String): Result<Moment, AppError>
    suspend fun saveMoment(moment: Moment): Result<Unit, AppError>
}
