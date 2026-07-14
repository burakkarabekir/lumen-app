package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.attachmentVoice
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.Res
import com.bksd.core.presentation.attachment_label_voice
import org.jetbrains.compose.resources.stringResource

@Composable
fun VoiceAttachmentCard(
    playbackState: PlaybackState,
    durationFormatted: String,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null
) {
    val palette = rememberNewEntryPalette()
    AttachmentCardChrome(
        badgeColor = MaterialTheme.colorScheme.extended.attachmentVoice,
        badgeIcon = Icons.Default.Mic,
        title = stringResource(Res.string.attachment_label_voice),
        onRemove = onRemove,
        modifier = modifier
    ) {
        VoicePlayerPill(
            playbackState = playbackState,
            durationFormatted = durationFormatted,
            accentColor = palette.saveBg,
            onPlay = onPlay,
            onPause = onPause,
            modifier = Modifier.padding(
                start = MaterialTheme.dimens.spacing.lg,
                end = MaterialTheme.dimens.spacing.lg,
                bottom = MaterialTheme.dimens.spacing.lg
            )
        )
    }
}

@Preview
@Composable
private fun VoiceAttachmentCardPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            VoiceAttachmentCard(
                playbackState = PlaybackState.STOPPED,
                durationFormatted = "0:42",
                onPlay = {},
                onPause = {},
                onRemove = {}
            )
        }
    }
}
