package com.bksd.insights.presentation

import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

internal fun StreakUnit.pluralRes(): PluralStringResource = when (this) {
    StreakUnit.DAY -> Res.plurals.streak_unit_day
    StreakUnit.WEEK -> Res.plurals.streak_unit_week
}

internal fun StreakKind.titleRes(): StringResource = when (this) {
    StreakKind.LONGEST -> Res.string.streak_longest
    StreakKind.RECENT -> Res.string.streak_recent
}

internal fun EntryMediaKind.labelRes(): StringResource = when (this) {
    EntryMediaKind.PHOTOS -> Res.string.breakdown_photos
    EntryMediaKind.VIDEOS -> Res.string.breakdown_videos
    EntryMediaKind.VOICE_NOTES -> Res.string.breakdown_voice_notes
    EntryMediaKind.PLACES -> Res.string.breakdown_places
}
