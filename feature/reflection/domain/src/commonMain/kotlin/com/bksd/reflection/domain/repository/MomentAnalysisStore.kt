package com.bksd.reflection.domain.repository

import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentAnalysisState
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.model.QuotaLimit
import kotlinx.coroutines.flow.Flow

interface MomentAnalysisStore {
    fun observe(momentId: String): Flow<MomentAnalysisState>
    suspend fun setPending(momentId: String)
    suspend fun setResult(momentId: String, reflection: MomentReflection)
    suspend fun setFailed(momentId: String)
    suspend fun setQuotaExceeded(momentId: String, limit: QuotaLimit)
    suspend fun setOffline(momentId: String)
    suspend fun recentAnalyses(limit: Int): List<EntryAnalysis>
    suspend fun deleteAll()
}
