package com.bksd.reflection.domain.repository

import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentAnalysisState
import com.bksd.reflection.domain.model.MomentReflection
import kotlinx.coroutines.flow.Flow

interface MomentAnalysisStore {
    fun observe(momentId: String): Flow<MomentAnalysisState>
    suspend fun setPending(momentId: String)
    suspend fun setResult(momentId: String, reflection: MomentReflection)
    suspend fun setFailed(momentId: String, quotaExceeded: Boolean = false)
    suspend fun setOffline(momentId: String)
    suspend fun recentAnalyses(limit: Int): List<EntryAnalysis>
    suspend fun deleteAll()
}
