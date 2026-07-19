package com.bksd.reflection.data.remote

import com.bksd.core.data.remote.supabase.supabaseCall
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.language.LanguageRepository
import com.bksd.reflection.domain.analysis.EntryReflector
import com.bksd.reflection.domain.analysis.ReflectionResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.functions.functions
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first

class SupabaseEntryReflector(
    private val client: SupabaseClient,
    private val languageRepository: LanguageRepository
) : EntryReflector {

    override suspend fun reflect(
        momentId: String,
        entryText: String,
        moods: List<String>,
        trend: String?
    ): Result<ReflectionResponse, AppError> = supabaseCall {
        val language = languageRepository.observeLanguage().first().promptLanguageName()
        val dto = client.functions.invoke(FUNCTION) {
            contentType(ContentType.Application.Json)
            setBody(
                AnalyzeRequest(
                    momentId = momentId,
                    text = entryText,
                    moods = moods.ifEmpty { null },
                    mood = moods.joinToString(", ").ifBlank { null },
                    trend = trend,
                    language = language
                )
            )
        }.body<AnalyzeResponseDto>()
        ReflectionResponse(
            analysis = dto.analysis,
            feedback = dto.feedback,
            question = dto.question,
            coverImageUrl = dto.coverImageUrl
        )
    }

    private companion object {
        const val FUNCTION = "analyze-entry"
    }
}
