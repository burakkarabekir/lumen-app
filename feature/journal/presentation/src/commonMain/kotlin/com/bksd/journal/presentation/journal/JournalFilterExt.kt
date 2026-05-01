package com.bksd.journal.presentation.journal

import com.bksd.journal.domain.model.JournalFilter
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.all_entries
import com.bksd.journal.presentation.links
import com.bksd.journal.presentation.photos
import com.bksd.journal.presentation.videos
import com.bksd.journal.presentation.voice_notes
import org.jetbrains.compose.resources.StringResource

val JournalFilter.labelRes: StringResource
    get() = when (this) {
        JournalFilter.ALL -> Res.string.all_entries
        JournalFilter.PHOTOS -> Res.string.photos
        JournalFilter.VIDEOS -> Res.string.videos
        JournalFilter.VOICE_NOTES -> Res.string.voice_notes
        JournalFilter.LINKS -> Res.string.links
    }
