package com.bksd.core.design_system.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens

/**
 * A reusable circular avatar component that:
 * - Shows the user's photo from a URL via Coil
 * - Falls back to a Person icon when no URL is available
 * - Optionally shows a loading indicator
 * - Supports click-to-navigate (onClick) and an edit badge overlay (onEditClick)
 *
 * @param photoUrl The URL of the avatar image (nullable).
 * @param modifier External modifier.
 * @param size The diameter of the avatar circle.
 * @param isLoading Whether to show a loading indicator instead of the image.
 * @param contentDescription Accessibility description for the avatar.
 * @param showBorder Whether to show a border around the avatar.
 * @param onClick Called when the avatar circle is tapped (e.g. navigate to profile).
 * @param onEditClick When non-null, shows a small edit badge at bottom-end.
 *   Called when that badge is tapped (e.g. pick a new photo).
 */
@Composable
fun AppAvatar(
    photoUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    isLoading: Boolean = false,
    contentDescription: String = "Avatar",
    showBorder: Boolean = false,
    onClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null,
) {
    val borderModifier = if (showBorder) {
        Modifier.border(2.dp, MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
    } else {
        Modifier
    }

    val clickModifier = if (onClick != null) {
        Modifier.clickable(role = Role.Button, onClick = onClick)
    } else {
        Modifier
    }

    // Outer box holds the avatar circle + optional edit badge
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        // Avatar circle
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .then(borderModifier)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .then(clickModifier),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = Pair(isLoading, photoUrl),
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                        animationSpec = tween(
                            300
                        )
                    )
                },
                label = "AvatarTransition"
            ) { (loading, url) ->
                if (loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(size * 0.35f),
                        strokeWidth = 2.dp
                    )
                } else if (!url.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = url,
                        contentDescription = contentDescription,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(size).clip(CircleShape),
                        error = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = contentDescription,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(size * 0.55f)
                            )
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = contentDescription,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(size * 0.55f)
                    )
                }
            }
        }

        // Edit badge (only shown when onEditClick is provided)
        if (onEditClick != null) {
            val badgeSize = (size.value * 0.32f).dp
            val iconSize = (size.value * 0.16f).dp

            Box(
                modifier = Modifier
                    .size(badgeSize)
                    .offset(x = MaterialTheme.dimens.spacing.xxs, y = MaterialTheme.dimens.spacing.xxs)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(role = Role.Button, onClick = onEditClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit avatar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewJournalTopBar() {
    AppTheme {
        AppAvatar(
            photoUrl = ""
        )
    }
}