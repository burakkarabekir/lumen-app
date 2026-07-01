package com.bksd.reflection.domain.repository

import com.bksd.reflection.domain.model.WeeklyReflection
import kotlinx.coroutines.flow.Flow

interface WeeklyReflectionStore {
    fun observe(): Flow<WeeklyReflection?>
    suspend fun save(reflection: WeeklyReflection)
    suspend fun clear()
}
