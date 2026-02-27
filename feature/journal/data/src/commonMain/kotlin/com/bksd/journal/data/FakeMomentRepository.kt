package com.bksd.journal.data

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.MediaAttachment
import com.bksd.core.domain.model.MediaType
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class FakeMomentRepository : MomentRepository {

    private val now = Clock.System.now()
    private val fakeMoments = mutableListOf(
        Moment(
            id = "11",
            body = "Test Test Test Test ",
            createdAt = now,
            mood = Mood.CALM,
            tags = persistentListOf("herbal tea"),
            attachments = persistentListOf(
                MediaAttachment(
                    id = "a1",
                    type = MediaType.LINK,
                    remoteUrl = "audio_url_placeholder"
                )
            )
        ),
        Moment(
            id = "1",
            body = "Morning Coffee Run. Got my favorite oat milk latte from the corner shop.",
            createdAt = now.minus(1.days),
            mood = Mood.ENERGETIC,
            tags = persistentListOf("coffee", "morning")
        ),
        Moment(
            id = "2",
            body = "Project meeting. The design review went well, but there is still a lot of work to do.",
            createdAt = now.minus(2.days),
            mood = Mood.REFLECTIVE,
            tags = persistentListOf("work", "design")
        ),
        Moment(
            id = "3",
            body = "Evening walk",
            createdAt = now.minus(3.days),
            mood = Mood.CALM,
            tags = persistentListOf("health"),
            attachments = persistentListOf(
                MediaAttachment(
                    id = "p1",
                    type = MediaType.PHOTO,
                    remoteUrl = "photo_url_placeholder"
                )
            )
        ),
        Moment(
            id = "4",
            body = "Voice note update. Just recording some quick thoughts before bed.",
            createdAt = now.minus(4.days),
            mood = Mood.INSPIRED,
            tags = persistentListOf("voice", "update"),
            attachments = persistentListOf(
                MediaAttachment(
                    id = "a1",
                    type = MediaType.AUDIO,
                    remoteUrl = "audio_url_placeholder"
                )
            )
        )
    )

    override suspend fun getMoments(date: LocalDate): Result<List<Moment>, AppError> {
        delay(100) // Simulate network delay
        val filtered = fakeMoments.filter { moment ->
            val momentDate = moment.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
            momentDate == date
        }
        return Result.Success(filtered)
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
