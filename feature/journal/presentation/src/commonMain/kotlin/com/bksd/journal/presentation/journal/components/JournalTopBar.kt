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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.AppAvatar
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.theme.AppTheme

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
    val headerHeight = 52.dp

    var isSearchMode by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            Box(modifier = Modifier.fillMaxWidth().height(headerHeight)) {
                if (searchActive) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = onSearchQueryChange,
                        headerHeight = headerHeight,
                        onBack = {
                            isSearchMode = false
                            onSearchQueryChange("")
                        }
                    )
                } else {
                    DefaultHeader(
                        headerHeight = headerHeight,
                        profilePhotoUrl = profilePhotoUrl,
                        onProfileClick = onProfileClick,
                        onSearchClick = { isSearchMode = true }
                    )
                }
            }
        }
        AppDivider()
    }
}

@Composable
private fun DefaultHeader(
    headerHeight: androidx.compose.ui.unit.Dp,
    profilePhotoUrl: String?,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppAvatar(
            photoUrl = profilePhotoUrl,
            size = 36.dp,
            contentDescription = "Profile",
            onClick = onProfileClick,
            modifier = Modifier.graphicsLayer { clip = true }
        )

        Text(
            text = "Lumen",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.graphicsLayer { clip = true }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SearchBar(
    headerHeight: androidx.compose.ui.unit.Dp,
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    // Auto-focus when search bar appears
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(40.dp)
                .graphicsLayer { clip = true }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
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
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = "Search moments…",
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
                    .size(40.dp)
                    .graphicsLayer { clip = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
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