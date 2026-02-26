package com.bksd.journal.domain.repository

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment

interface MomentRepository {
    suspend fun getMoments(): Result<List<Moment>, AppError>
    suspend fun getMoment(id: String): Result<Moment, AppError>
    suspend fun saveMoment(moment: Moment): Result<Unit, AppError>
}
