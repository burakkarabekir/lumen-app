package com.bksd.journal.presentation.journal.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.component.AppAvatar
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.content_desc_profile
import com.bksd.journal.presentation.content_desc_search
import org.jetbrains.compose.resources.stringResource

@Composable
fun JournalTopBar(
    searchQuery: String,
    profilePhotoUrl: String?,
    isSearchActive: Boolean,
    searchIconAlpha: () -> Float,
    onSearchActivate: () -> Unit,
    onSearchClose: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val headerHeight = MaterialTheme.dimens.size.topBar
    Column(modifier.statusBarsPadding()) {
        AnimatedContent(
            targetState = isSearchActive,
            transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
            label = "LumenSearchMorph",
        ) { active ->
            if (active) {
                JournalActiveSearchBar(
                    query = searchQuery,
                    headerHeight = headerHeight,
                    onQueryChange = onSearchQueryChange,
                    onBack = onSearchClose,
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight)
                        .padding(horizontal = MaterialTheme.dimens.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LumenWordmark()
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = onSearchActivate,
                        modifier = Modifier.graphicsLayer { alpha = searchIconAlpha() },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(Res.string.content_desc_search),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    AppAvatar(
                        photoUrl = profilePhotoUrl,
                        size = MaterialTheme.dimens.icon.avatar,
                        contentDescription = stringResource(Res.string.content_desc_profile),
                        onClick = onProfileClick,
                    )
                }
            }
        }
        AppDivider()
    }
}

@Preview
@Composable
private fun PreviewJournalTopBar() {
    AppTheme {
        JournalTopBar(
            searchQuery = "",
            profilePhotoUrl = "",
            isSearchActive = false,
            searchIconAlpha = { 1f },
            onSearchActivate = {},
            onSearchClose = {},
            onSearchQueryChange = {},
            onProfileClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewJournalTopBarSearch() {
    AppTheme(darkTheme = true) {
        JournalTopBar(
            searchQuery = "morning walk",
            profilePhotoUrl = null,
            isSearchActive = true,
            searchIconAlpha = { 1f },
            onSearchActivate = {},
            onSearchClose = {},
            onSearchQueryChange = {},
            onProfileClick = {},
        )
    }
}
