package com.bksd.journal.presentation.journal.components

import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.journal.domain.model.Moment

internal enum class CardMediaType { PHOTO, VIDEO, AUDIO, LINK, TEXT }

internal fun determinePrimaryType(moment: Moment): CardMediaType {
    val first = moment.attachments.firstOrNull()
    return when (first) {
        is PhotoAttachment -> CardMediaType.PHOTO
        is VideoAttachment -> CardMediaType.VIDEO
        is AudioAttachment -> CardMediaType.AUDIO
        is LinkAttachment -> CardMediaType.LINK
        else -> CardMediaType.TEXT
    }
}
