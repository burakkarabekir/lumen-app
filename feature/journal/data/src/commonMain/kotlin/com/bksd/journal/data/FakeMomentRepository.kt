package com.bksd.journal.data

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.MediaType
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.days

class FakeMomentRepository : MomentRepository {

    private val now = 1739462400000L // Hardcoded for fake mock data
    private val emptyMoments = mutableListOf<Moment>()
    private val fakeMoments = mutableListOf(
        Moment(
            id = "1",
            body = "Morning Coffee Run. Got my favorite oat milk latte from the corner shop.",
            timestamp = now - 1.days.inWholeMilliseconds,
            mood = Mood.ENERGETIC,
            tags = listOf("coffee", "morning")
        ),
        Moment(
            id = "2",
            body = "Project meeting. The design review went well, but there is still a lot of work to do.",
            timestamp = now - 2.days.inWholeMilliseconds,
            mood = Mood.REFLECTIVE,
            tags = listOf("work", "design")
        ),
        Moment(
            id = "3",
            body = "Evening walk",
            timestamp = now - 3.days.inWholeMilliseconds,
            mood = Mood.CALM,
            tags = listOf("health"),
            attachments = listOf(
                com.bksd.core.domain.model.MediaAttachment(
                    id = "p1",
                    type = MediaType.PHOTO,
                    remoteUrl = "photo_url_placeholder"
                )
            )
        ),
        Moment(
            id = "4",
            body = "Voice note update. Just recording some quick thoughts before bed.",
            timestamp = now - 4.days.inWholeMilliseconds,
            mood = Mood.INSPIRED,
            tags = listOf("voice", "update"),
            attachments = listOf(
                com.bksd.core.domain.model.MediaAttachment(
                    id = "a1",
                    type = MediaType.AUDIO,
                    remoteUrl = "audio_url_placeholder"
                )
            )
        )
    )

    override suspend fun getMoments(): Result<List<Moment>, AppError> {
        return Result.Success(emptyMoments)
    }

    override suspend fun getMoment(id: String): Result<Moment, AppError> {
        delay(500)
        val moment = fakeMoments.find { it.id == id }
        return if (moment != null) {
            Result.Success(moment)
        } else {
            Result.Error(AppError.Unknown("Moment not found"))
        }
    }

    override suspend fun saveMoment(moment: Moment): Result<Unit, AppError> {
        delay(1000)
        val existingIndex = fakeMoments.indexOfFirst { it.id == moment.id }
        if (existingIndex != -1) {
            fakeMoments[existingIndex] = moment
        } else {
            fakeMoments.add(0, moment) // Add to the top
        }
        return Result.Success(Unit)
    }
}
