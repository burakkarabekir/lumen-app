package com.bksd.journal.presentation.journal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bksd.core.design_system.component.button.fab.AppFab
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.journal.domain.model.JournalFilter
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.content_desc_create_moment
import com.bksd.journal.presentation.journal.components.CalendarStrip
import com.bksd.journal.presentation.journal.components.FilterChips
import com.bksd.journal.presentation.journal.components.MomentCard
import com.bksd.journal.presentation.journal_title
import com.bksd.journal.presentation.no_filter_day
import com.bksd.journal.presentation.no_moments_day
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JournalRoot(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
) {
    val viewModel = koinViewModel<JournalViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val formatter = koinInject<MomentFormatter>()
    val timeZone = koinInject<kotlinx.datetime.TimeZone>()

    val listState = rememberLazyListState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is JournalEvent.NavigateToDetail -> onNavigateToDetail(event.momentId)
            is JournalEvent.NavigateToCreate -> onNavigateToCreate()
            is JournalEvent.ShowError -> {
                println("Journal Error: ${event.error}")
            }
            is JournalEvent.ScrollToDate -> {
                // Find the index of the section header for this date
                val sectionIndex = findSectionHeaderIndex(state, event.date)
                if (sectionIndex >= 0) {
                    listState.animateScrollToItem(sectionIndex)
                }
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

/**
 * Find the LazyColumn item index for a section header by date.
 * Sections are grouped by month, so we match on year + monthNumber.
 * Layout order: [section_header, moment, moment, ..., section_header, moment, ...]
 */
private fun findSectionHeaderIndex(state: JournalState, date: LocalDate): Int {
    val targetId = date.toString() // e.g. "2025-05-03"
    var index = 0
    for (section in state.sections) {
        if (section.id == targetId) return index
        index += 1 + section.moments.size // header + moments
    }
    return -1
}

@Composable
fun JournalScreen(
    state: JournalState,
    formatter: MomentFormatter,
    timeZone: kotlinx.datetime.TimeZone,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onAction: (JournalAction) -> Unit
) {
    // FAB hides while user is scrolling, reappears when idle
    val isFabVisible by remember {
        derivedStateOf { !listState.isScrollInProgress }
    }

    // Detect when user scrolls near the bottom → load older days
    LaunchedEffect(listState) {
        snapshotFlow {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems to lastVisibleIndex
        }
            .distinctUntilChanged()
            .collect { (totalItems, lastVisibleIndex) ->
                // Trigger load-more when within 3 items of the bottom
                if (totalItems > 0 && lastVisibleIndex >= totalItems - 3) {
                    onAction(JournalAction.OnLoadMoreDays)
                }
            }
    }

    // Track which section is visible and sync the calendar
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> resolveDateForIndex(index, state.sections, timeZone) }
            .distinctUntilChanged()
            .collect { date ->
                if (date != null) {
                    onAction(JournalAction.OnVisibleDateChanged(date))
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppSurface(
            header = {
                AppTopBar(
                    title = stringResource(Res.string.journal_title),
                    style = AppBarStyle.Root,
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

                else -> {
                    CalendarStrip(
                        selectedDate = state.visibleDate,
                        timeZone = timeZone,
                        onDateSelect = { onAction(JournalAction.OnDateSelect(it)) }
                    )

                    AppDivider()
                    FilterChips(
                        selectedFilter = state.selectedFilter,
                        onFilterSelect = { onAction(JournalAction.OnFilterSelect(it)) },
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    AppDivider()

                    if (state.sections.isEmpty()) {
                        val message = when {
                            state.selectedFilter != JournalFilter.ALL ->
                                stringResource(
                                    Res.string.no_filter_day,
                                    stringResource(state.selectedFilter.labelRes).lowercase()
                                )

                            else ->
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
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize().weight(1f),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            state.sections.forEach { section ->
                                // Section date header
                                item(key = "header_${section.id}") {
                                    SectionHeader(text = section.dateHeader.asString())
                                }

                                // Moment cards for this section
                                items(
                                    items = section.moments,
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
                                        onClick = { onAction(JournalAction.OnMomentClick(moment.id)) }
                                    )
                                }
                            }

                            // Load more indicator at the bottom (oldest edge)
                            if (state.isLoadingMore) {
                                item(key = "loading_more") {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
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
            }
        }

        // FAB — positioned at top-right corner of the bottom navigation bar
        AnimatedVisibility(
            visible = isFabVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 96.dp),
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            AppFab(
                onClick = { onAction(JournalAction.OnCreateNewClick) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.content_desc_create_moment)
                )
            }
        }
    }
}

/**
 * Resolve which section date corresponds to the given LazyColumn item index.
 * Returns null if the index doesn't map to a known section.
 */
private fun resolveDateForIndex(
    index: Int,
    sections: List<JournalSection>,
    timeZone: kotlinx.datetime.TimeZone
): LocalDate? {
    var runningIndex = 0
    for (section in sections) {
        // This range covers the header + all moments for this section
        val sectionSize = 1 + section.moments.size
        if (index < runningIndex + sectionSize) {
            val itemIndex = index - runningIndex
            if (itemIndex == 0) return null // Header visible
            return section.moments[itemIndex - 1].createdAt.toLocalDateTime(timeZone).date
        }
        runningIndex += sectionSize
    }
    return null
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
    )
}
