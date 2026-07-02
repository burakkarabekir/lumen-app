package com.bksd.journal.presentation.journal.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bksd.core.design_system.component.AppAvatar
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.content_desc_back
import com.bksd.journal.presentation.content_desc_clear_search
import com.bksd.journal.presentation.content_desc_profile
import com.bksd.journal.presentation.content_desc_search
import com.bksd.journal.presentation.search_moments_hint
import org.jetbrains.compose.resources.stringResource

@Composable
fun JournalTopBar(
    searchQuery: String,
    profilePhotoUrl: String?,
    onSearchQueryChange: (String) -> Unit,
    onProfileClick: () -> Unit,
    onSearchModeChanged: (Boolean) -> Unit
) {
    val totalDuration = 500
    val fadeOutDuration = 150
    val organicEasing = remember { CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f) }
    val headerHeight = MaterialTheme.dimens.size.topBar

    var isSearchMode by remember { mutableStateOf(false) }

    Column {
        AnimatedContent(
            targetState = isSearchMode,
            transitionSpec = {
                if (targetState) {
                    (fadeIn(tween(totalDuration, delayMillis = 50, easing = organicEasing)) +
                            expandHorizontally(
                                tween(totalDuration, easing = organicEasing),
                                expandFrom = Alignment.End
                            )).togetherWith(
                        fadeOut(tween(fadeOutDuration, easing = organicEasing)) +
                                shrinkHorizontally(
                                    tween(totalDuration, easing = organicEasing),
                                    shrinkTowards = Alignment.Start
                                )
                    )
                } else {
                    // Search → Header
                    (fadeIn(tween(totalDuration, delayMillis = 50, easing = organicEasing)) +
                            expandHorizontally(
                                tween(totalDuration, easing = organicEasing),
                                expandFrom = Alignment.Start
                            )).togetherWith(
                        fadeOut(tween(fadeOutDuration, easing = organicEasing)) +
                                shrinkHorizontally(
                                    tween(totalDuration, easing = organicEasing),
                                    shrinkTowards = Alignment.End
                                )
                    )
                }.using(SizeTransform(clip = false))
            },
            label = "LumenMorphingSearch"
        ) { searchActive ->
            if (searchActive) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    headerHeight = headerHeight,
                    onBack = {
                        isSearchMode = false
                        onSearchModeChanged(false)
                        onSearchQueryChange("")
                    }
                )
            } else {
                AppTopBar(
                    title = "Lumen",
                    style = AppBarStyle.Root,
                    actions = {
                        IconButton(
                            onClick = {
                                isSearchMode = true
                                onSearchModeChanged(true)
                            },
                            modifier = Modifier.graphicsLayer { clip = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(Res.string.content_desc_search),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        AppAvatar(
                            photoUrl = profilePhotoUrl,
                            size = MaterialTheme.dimens.icon.avatar,
                            contentDescription = stringResource(Res.string.content_desc_profile),
                            onClick = onProfileClick,
                            modifier = Modifier.graphicsLayer { clip = true }
                        )
                    }
                )
            }
        }
        AppDivider()
    }
}

@Composable
private fun SearchBar(
    headerHeight: Dp,
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = MaterialTheme.dimens.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.tile)
                .graphicsLayer { clip = true }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(Res.string.content_desc_back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.md)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.search_moments_hint),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        )

        // Clear button — only visible when there is text
        if (query.isNotEmpty()) {
            IconButton(
                onClick = { onQueryChange("") },
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.tile)
                    .graphicsLayer { clip = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.content_desc_clear_search),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewJournalTopBar() {
    AppTheme {
        JournalTopBar(
            searchQuery = "",
            profilePhotoUrl = "",
            onSearchQueryChange = {},
            onProfileClick = {},
            onSearchModeChanged = {}
        )
    }
}

@Preview
@Composable
private fun PreviewJournalTopBarDark() {
    AppTheme(darkTheme = true) {
        JournalTopBar(
            searchQuery = "morning walk",
            profilePhotoUrl = null,
            onSearchQueryChange = {},
            onProfileClick = {},
            onSearchModeChanged = {}
        )
    }
}
