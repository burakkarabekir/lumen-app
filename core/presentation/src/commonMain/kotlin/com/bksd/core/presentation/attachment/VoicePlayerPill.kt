package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.Res
import com.bksd.core.presentation.cd_pause
import com.bksd.core.presentation.cd_play
import org.jetbrains.compose.resources.stringResource

private val VoiceBarHeights = listOf(7, 12, 17, 10, 14, 8, 11, 16, 9, 7)

@Composable
fun VoicePlayerPill(
    playbackState: PlaybackState,
    durationFormatted: String,
    accentColor: Color,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val chipBg = lerp(palette.surface, palette.text, 0.06f)
    val idleBar = palette.sub.copy(alpha = 0.4f)
    val isPlaying = playbackState == PlaybackState.PLAYING
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
        modifier = modifier
            .height(MaterialTheme.dimens.size.fab)
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
            .background(chipBg)
            .padding(start = MaterialTheme.dimens.spacing.sm, end = MaterialTheme.dimens.spacing.lg)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.avatar)
                .clip(CircleShape)
                .background(accentColor)
                .clickable(role = Role.Button) { if (isPlaying) onPause() else onPlay() }
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = stringResource(if (isPlaying) Res.string.cd_pause else Res.string.cd_play),
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xxs)
        ) {
            VoiceBarHeights.forEachIndexed { index, barHeight ->
                Box(
                    modifier = Modifier
                        .width(MaterialTheme.dimens.icon.xs)
                        .height(barHeight.dp)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xs))
                        .background(if (index < 4) accentColor else idleBar)
                )
            }
        }
        Text(
            text = durationFormatted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = palette.sub
        )
    }
}

@Preview
@Composable
private fun VoicePlayerPillPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            VoicePlayerPill(
                playbackState = PlaybackState.STOPPED,
                durationFormatted = "0:42",
                accentColor = palette.saveBg,
                onPlay = {},
                onPause = {}
            )
        }
    }
}
