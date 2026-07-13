package com.bksd.reflection.data.remote

import com.bksd.core.data.remote.supabase.supabaseCall
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.language.LanguageRepository
import com.bksd.reflection.domain.analysis.WeeklyReflectionContent
import com.bksd.reflection.domain.analysis.WeeklyReflector
import com.bksd.reflection.domain.analysis.WeeklyThemeContent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.functions.functions
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first

class SupabaseWeeklyReflector(
    private val client: SupabaseClient,
    private val languageRepository: LanguageRepository
) : WeeklyReflector {

    override suspend fun reflectWeek(entries: List<String>): Result<WeeklyReflectionContent, AppError> =
        supabaseCall {
            val language = languageRepository.observeLanguage().first().promptLanguageName()
            val dto = client.functions.invoke(FUNCTION) {
                contentType(ContentType.Application.Json)
                setBody(WeeklyReflectionRequest(entries = entries, language = language))
            }.body<WeeklyReflectionDto>()
            WeeklyReflectionContent(
                narrative = dto.narrative,
                summary = dto.summary,
                themes = dto.themes.map { WeeklyThemeContent(label = it.label, count = it.count) },
                questions = dto.questions
            )
        }

    private companion object {
        const val FUNCTION = "weekly-reflection"
    }
}
