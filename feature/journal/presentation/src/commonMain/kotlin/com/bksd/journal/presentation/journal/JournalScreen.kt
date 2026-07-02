package com.bksd.journal.presentation.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.journal.presentation.model.MomentUi
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.link.LinkConfirmationDialog
import com.bksd.core.presentation.link.toOpenableWebUrl
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.journal.components.JournalSectionHeader
import com.bksd.journal.presentation.journal.components.JournalTopBar
import com.bksd.journal.presentation.journal.components.MomentCard
import com.bksd.journal.presentation.no_moments_day
import com.bksd.journal.presentation.util.DefaultMomentFormatter
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun JournalRoot(
    onNavigateToDetail: (String, Boolean) -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    val viewModel = koinViewModel<JournalViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val formatter = koinInject<MomentFormatter>()
    val timeZone = koinInject<TimeZone>()

    val listState = rememberLazyListState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is JournalEvent.NavigateToDetail -> onNavigateToDetail(event.momentId, event.isEditing)
            is JournalEvent.NavigateToProfile -> onNavigateToProfile()
            is JournalEvent.ShowError -> {
                println("Journal Error: ${event.error}")
            }
        }
    }

    JournalScreen(
        state = state,
        formatter = formatter,
        timeZone = timeZone,
        listState = listState,
        onAction = viewModel::onAction
    )
}

@Composable
fun JournalScreen(
    state: JournalState,
    formatter: MomentFormatter,
    timeZone: TimeZone,
    listState: LazyListState,
    onAction: (JournalAction) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    var pendingLink by remember { mutableStateOf<String?>(null) }

    // Detect when user scrolls near the bottom → load more
    LaunchedEffect(listState) {
        snapshotFlow {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems to lastVisibleIndex
        }
            .distinctUntilChanged()
            .collect { (totalItems, lastVisibleIndex) ->
                if (totalItems > 0 && lastVisibleIndex >= totalItems - 3) {
                    onAction(JournalAction.OnLoadMore)
                }
            }
    }

    AppSurface(
        header = {
            JournalTopBar(
                searchQuery = state.searchQuery,
                profilePhotoUrl = state.profilePhotoUrl,
                onSearchQueryChange = { onAction(JournalAction.OnSearchQueryChange(it)) },
                onProfileClick = { onAction(JournalAction.OnProfileClick) },
                onSearchModeChanged = {}
            )
        }
    ) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            state.sections.isEmpty() -> {
                val message = if (state.searchQuery.isNotBlank()) {
                    "No results for \"${state.searchQuery}\""
                } else {
                    stringResource(Res.string.no_moments_day)
                }
                Box(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().weight(1f),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md)
                ) {
                    item { Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xs)) }

                    state.sections.forEach { section ->
                        item(key = "header_${section.header}") {
                            JournalSectionHeader(text = section.header)
                        }

                        items(
                            items = section.items,
                            key = { it.id }
                        ) { moment ->
                            MomentCard(
                                moment = moment,
                                formatter = formatter,
                                timeZone = timeZone,
                                audioPlaybackState = if (state.playingAudioMomentId == moment.id) state.audioPlaybackState else PlaybackState.STOPPED,
                                audioCurrentPosition = if (state.playingAudioMomentId == moment.id && state.audioCurrentPositionMs > 0)
                                    formatter.formatDuration(state.audioCurrentPositionMs) else "0:00",
                                onAudioPlayClick = {
                                    val audioUrl =
                                        moment.attachments.filterIsInstance<AudioAttachment>()
                                            .firstOrNull()?.remoteUrl?.value
                                    if (audioUrl != null) {
                                        onAction(
                                            JournalAction.OnAudioPlayClick(
                                                moment.id,
                                                audioUrl
                                            )
                                        )
                                    }
                                },
                                onAudioPauseClick = { onAction(JournalAction.OnAudioPauseClick) },
                                onLinkClick = { pendingLink = it },
                                onEditClick = { onAction(JournalAction.OnEditMoment(moment.id)) },
                                onFavoriteToggleClick = {
                                    onAction(
                                        JournalAction.OnFavoriteToggle(
                                            moment.id
                                        )
                                    )
                                },
                                onDeleteClick = { onAction(JournalAction.OnDeleteMoment(moment.id)) },
                                onClick = { onAction(JournalAction.OnMomentClick(moment.id)) }
                            )
                        }
                    }

                    if (state.isLoadingMore) {
                        item(key = "loading_more") {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimens.spacing.lg),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(MaterialTheme.dimens.icon.xl),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(128.dp))
                    }
                }
            }
        }

        pendingLink?.let { url ->
            LinkConfirmationDialog(
                url = url,
                onConfirm = {
                    toOpenableWebUrl(url)?.let { safe -> runCatching { uriHandler.openUri(safe) } }
                    pendingLink = null
                },
                onDismiss = { pendingLink = null }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewJournalScreen() {
    AppTheme {
        JournalScreen(
            state = JournalState(
                visibleDate = LocalDate.fromEpochDays(1),
                isLoading = false,
                sections = persistentListOf(
                    JournalSection(
                        header = "Today",
                        items = persistentListOf(
                            MomentUi(
                                id = "",
                                title = "Test",
                                body = "Body",
                                createdAt = Clock.System.now(),
                                moods = persistentListOf(),
                                tags = persistentListOf(),
                                attachments = persistentListOf(),
                                location = null,
                                isFavorite = false,
                            )
                        )
                    )
                )
            ),
            formatter = DefaultMomentFormatter(TimeZone.UTC),
            timeZone = TimeZone.UTC,
            listState = rememberLazyListState(),
            onAction = {}
        )
    }
}
