package com.bksd.reflection.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.repository.MomentRepository
import com.bksd.reflection.domain.analysis.WeeklyReflector
import com.bksd.reflection.domain.model.ReflectionTheme
import com.bksd.reflection.domain.model.WeeklyReflection
import com.bksd.reflection.domain.repository.WeeklyReflectionStore
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class GenerateWeeklyReflectionUseCase(
    private val momentRepository: MomentRepository,
    private val reflector: WeeklyReflector,
    private val store: WeeklyReflectionStore,
    private val clock: Clock,
    private val timeZone: TimeZone,
) {
    suspend operator fun invoke(): Result<WeeklyReflection?, AppError> {
        val now = clock.now()
        val cutoff = now.minus(WINDOW)
        val moments = momentRepository.observeAllMoments().first()
            .filter { it.createdAt >= cutoff }
            .sortedBy { it.createdAt }
        if (moments.isEmpty()) return Result.Success(null)

        val texts = moments.mapNotNull { entryText(it) }
        if (texts.isEmpty()) return Result.Success(null)

        val content = when (val result = reflector.reflectWeek(texts)) {
            is Result.Success -> result.data
            is Result.Error -> return Result.Error(result.error)
        }

        val reflection = WeeklyReflection(
            narrative = content.narrative,
            summary = content.summary,
            themes = content.themes.mapIndexed { index, theme ->
                ReflectionTheme(
                    label = theme.label,
                    colorHex = THEME_COLORS[index % THEME_COLORS.size],
                    count = theme.count
                )
            },
            questions = content.questions,
            entryCount = moments.size,
            rangeLabel = rangeLabel(
                moments.first().createdAt.toLocalDateTime(timeZone).date,
                moments.last().createdAt.toLocalDateTime(timeZone).date
            ),
            generatedAtMs = now.toEpochMilliseconds()
        )
        store.save(reflection)
        return Result.Success(reflection)
    }

    private fun entryText(moment: Moment): String? {
        val text = listOfNotNull(
            moment.title.takeIf { it.isNotBlank() },
            moment.body?.takeIf { it.isNotBlank() }
        ).joinToString("\n").trim()
        return text.ifBlank { null }
    }

    private fun rangeLabel(start: LocalDate, end: LocalDate): String {
        val startMonth = monthAbbrev(start)
        return if (start.month == end.month) {
            "$startMonth ${start.day}–${end.day}"
        } else {
            "$startMonth ${start.day} – ${monthAbbrev(end)} ${end.day}"
        }
    }

    private fun monthAbbrev(date: LocalDate): String =
        date.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)

    private companion object {
        val WINDOW = 7.days
        val THEME_COLORS = listOf("#7682D6", "#CF6F64", "#2FA876", "#E0A33A", "#8A6FBF")
    }
}
