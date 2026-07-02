package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.entryToolbarLink
import com.bksd.core.design_system.theme.entryToolbarNote
import com.bksd.core.design_system.theme.entryToolbarPhoto
import com.bksd.core.design_system.theme.entryToolbarVideo
import com.bksd.core.design_system.theme.entryToolbarVoice
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.label_camera
import com.bksd.moment.presentation.label_link
import com.bksd.moment.presentation.label_photo
import com.bksd.moment.presentation.label_place
import com.bksd.moment.presentation.label_voice
import org.jetbrains.compose.resources.stringResource

@Composable
fun EntryToolbar(
    onPhotoClick: () -> Unit,
    onCameraClick: () -> Unit,
    onPlaceClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onLinkClick: () -> Unit,
    isRecording: Boolean,
    modifier: Modifier = Modifier,
) {
    val palette = rememberNewEntryPalette()
    val extended = MaterialTheme.colorScheme.extended
    Column(modifier = modifier.fillMaxWidth().background(palette.pageBg)) {
        HorizontalDivider(thickness = 1.dp, color = palette.hairline)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = MaterialTheme.dimens.spacing.md)
                .padding(top = MaterialTheme.dimens.spacing.lg, bottom = MaterialTheme.dimens.spacing.xl)
        ) {
            ToolbarTile(
                icon = Icons.Filled.Image,
                label = stringResource(Res.string.label_photo),
                tint = extended.entryToolbarPhoto,
                onClick = onPhotoClick,
                modifier = Modifier.weight(1f)
            )
            ToolbarTile(
                icon = Icons.Filled.CameraAlt,
                label = stringResource(Res.string.label_camera),
                tint = extended.entryToolbarVideo,
                onClick = onCameraClick,
                modifier = Modifier.weight(1f)
            )
            ToolbarTile(
                icon = Icons.Filled.Place,
                label = stringResource(Res.string.label_place),
                tint = extended.entryToolbarNote,
                onClick = onPlaceClick,
                modifier = Modifier.weight(1f)
            )
            ToolbarTile(
                icon = Icons.Filled.Mic,
                label = stringResource(Res.string.label_voice),
                tint = extended.entryToolbarVoice,
                onClick = onVoiceClick,
                modifier = Modifier.weight(1f),
                isActive = isRecording
            )
            ToolbarTile(
                icon = Icons.Filled.Link,
                label = stringResource(Res.string.label_link),
                tint = extended.entryToolbarLink,
                onClick = onLinkClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun EntryToolbarPreview() {
    AppTheme(darkTheme = true) {
        EntryToolbar(
            onPhotoClick = {},
            onCameraClick = {},
            onPlaceClick = {},
            onVoiceClick = {},
            onLinkClick = {},
            isRecording = false
        )
    }
}
