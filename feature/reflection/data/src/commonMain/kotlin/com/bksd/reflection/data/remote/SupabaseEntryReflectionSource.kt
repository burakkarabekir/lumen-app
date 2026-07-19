package com.bksd.reflection.data.remote

import com.bksd.core.data.remote.supabase.SupabaseAuthDataSource
import com.bksd.core.data.remote.supabase.supabaseCall
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.analysis.EntryReflectionSource
import com.bksd.reflection.domain.analysis.ReflectionResponse
import com.bksd.reflection.domain.analysis.StoredReflection
import com.bksd.reflection.domain.analysis.toMomentReflection
import com.bksd.reflection.domain.model.DistressLevel
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MoodValence
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class SupabaseEntryReflectionSource(
    private val client: SupabaseClient,
    private val authDataSource: SupabaseAuthDataSource,
) : EntryReflectionSource {

    private val reflections get() = client.postgrest["entry_reflections"]

    override suspend fun fetchAll(): Result<List<StoredReflection>, AppError> {
        val uid = authDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))
        return supabaseCall {
            reflections.select {
                filter { eq("user_id", uid) }
            }.decodeList<EntryReflectionDto>().map { it.toStoredReflection() }
        }
    }
}

private fun EntryReflectionDto.toStoredReflection(): StoredReflection {
    val response = ReflectionResponse(
        analysis = EntryAnalysis(
            summary = summary,
            moodValence = MoodValence.valueOf(moodValence),
            moodConfidence = moodConfidence,
            dominantEmotions = dominantEmotions,
            themes = themes,
            distress = DistressLevel.valueOf(distress),
            distressRationale = ""
        ),
        feedback = feedback,
        question = question,
        coverImageUrl = coverImageUrl
    )
    return StoredReflection(momentId = momentId, reflection = response.toMomentReflection())
}
