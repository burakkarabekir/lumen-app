package com.bksd.core.domain.model

import kotlin.jvm.JvmInline

sealed interface Attachment {
    val id: AttachmentId
}

@JvmInline
value class AttachmentId(val value: String)