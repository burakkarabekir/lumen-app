package com.bksd.journal.domain.repository

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment
import kotlinx.datetime.LocalDate

interface MomentRepository {
    suspend fun getMoments(date: LocalDate): Result<List<Moment>, AppError>
    suspend fun getMoment(id: String): Result<Moment, AppError>
    suspend fun saveMoment(moment: Moment): Result<Unit, AppError>
}
