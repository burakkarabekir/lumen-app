package com.bksd.profile.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.profile_photo_content_desc
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfilePhotoViewer(
    visible: Boolean,
    photoUrl: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(180)),
        exit = fadeOut(tween(180)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.82f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = scaleIn(tween(300), initialScale = 0.35f) + fadeIn(tween(220)),
                exit = scaleOut(tween(220), targetScale = 0.35f) + fadeOut(tween(160))
            ) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = stringResource(Res.string.profile_photo_content_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfilePhotoViewerPreview() {
    PreviewAppTheme(darkTheme = true) {
        ProfilePhotoViewer(
            visible = true,
            photoUrl = null,
            onDismiss = {}
        )
    }
}
