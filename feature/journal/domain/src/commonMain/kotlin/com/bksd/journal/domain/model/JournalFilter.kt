package com.bksd.journal.domain.model

import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.VideoAttachment

enum class JournalFilter {
    ALL,
    PHOTOS,
    VIDEOS,
    VOICE_NOTES,
    LINKS
}

fun List<Moment>.applyFilter(filter: JournalFilter): List<Moment> {
    return when (filter) {
        JournalFilter.ALL -> this
        JournalFilter.PHOTOS -> filter { moment ->
            moment.attachments.any { it is PhotoAttachment }
        }

        JournalFilter.VIDEOS -> filter { moment ->
            moment.attachments.any { it is VideoAttachment }
        }

        JournalFilter.VOICE_NOTES -> filter { moment ->
            moment.attachments.any { it is AudioAttachment }
        }

        JournalFilter.LINKS -> filter { moment ->
            moment.attachments.any { it is LinkAttachment }
        }
    }
}
