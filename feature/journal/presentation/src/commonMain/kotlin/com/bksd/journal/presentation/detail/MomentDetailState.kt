package com.bksd.journal.presentation.detail

import androidx.compose.runtime.Immutable
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.util.UiText
import com.bksd.reflection.domain.model.MomentAnalysisState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlin.time.Instant

@Immutable
data class MomentDetailState(
    val isLoading: Boolean = false,
    val moment: Moment? = null,
    val error: UiText? = null,
    val isEditing: Boolean = false,
    val isBodyExpanded: Boolean = false,
    val editTitle: String = "",
    val editBody: String = "",
    val editMoods: ImmutableSet<Mood> = persistentSetOf(),
    val editTags: ImmutableList<String> = persistentListOf(),
    val editLocation: LocationData? = null,
    val editCreatedAt: Instant? = null,
    val editAttachments: ImmutableList<Attachment> = persistentListOf(),
    val isSaving: Boolean = false,
    val playingAudioUrl: String? = null,
    val audioPlaybackState: PlaybackState = PlaybackState.STOPPED,
    val audioPositionFormatted: String = "0:00",
    val audioDurationFormatted: String = "0:00",
    val audioAmplitudes: ImmutableList<Float> = persistentListOf(),
    val analysis: MomentAnalysisState = MomentAnalysisState.None,
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false
)
