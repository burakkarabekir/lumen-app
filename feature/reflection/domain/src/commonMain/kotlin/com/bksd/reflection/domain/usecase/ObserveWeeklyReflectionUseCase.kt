package com.bksd.reflection.domain.usecase

import com.bksd.reflection.domain.model.WeeklyReflection
import com.bksd.reflection.domain.repository.WeeklyReflectionStore
import kotlinx.coroutines.flow.Flow

class ObserveWeeklyReflectionUseCase(
    private val store: WeeklyReflectionStore
) {
    operator fun invoke(): Flow<WeeklyReflection?> = store.observe()
}
