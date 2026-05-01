package com.bksd.journal.presentation.journal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bksd.core.design_system.component.button.fab.AppFab
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.journal.domain.model.JournalFilter
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.content_desc_create_moment
import com.bksd.journal.presentation.journal.components.CalendarStrip
import com.bksd.journal.presentation.journal.components.FilterChips
import com.bksd.journal.presentation.journal.components.MomentCard
import com.bksd.journal.presentation.journal_title
import com.bksd.journal.presentation.no_filter_day
import com.bksd.journal.presentation.no_moments_day
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun JournalRoot(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
) {
    val viewModel = koinViewModel<JournalViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val formatter = koinInject<MomentFormatter>()
    val timeZone = koinInject<kotlinx.datetime.TimeZone>()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is JournalEvent.NavigateToDetail -> onNavigateToDetail(event.momentId)
            is JournalEvent.NavigateToCreate -> onNavigateToCreate()
            is JournalEvent.ShowError -> {
                println("Journal Error: ${event.error}")
            }
        }
    }

    JournalScreen(
        state = state,
        formatter = formatter,
        timeZone = timeZone,
        onAction = viewModel::onAction
    )
}

@Composable
fun JournalScreen(
    state: JournalState,
    formatter: MomentFormatter,
    timeZone: kotlinx.datetime.TimeZone,
    onAction: (JournalAction) -> Unit
) {
    val listState = rememberLazyListState()

    // FAB hides while user is scrolling, reappears when idle
    val isFabVisible by remember {
        derivedStateOf { !listState.isScrollInProgress }
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
                        selectedDate = state.selectedDate,
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

                    if (state.moments.isEmpty()) {
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
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.moments, key = { it.id }) { moment ->
                                MomentCard(
                                    moment = moment,
                                    formatter = formatter,
                                    onClick = { onAction(JournalAction.OnMomentClick(moment.id)) }
                                )
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

@Preview
@Composable
fun Preview() {
    val mockFormatter = object : MomentFormatter {
        override fun formatTime(instant: kotlin.time.Instant) = "12:00 PM"
        override fun formatDuration(ms: Long) = "1:23"
    }
    AppTheme(darkTheme = true) {
        JournalScreen(
            state = JournalState(
                moments = persistentListOf(
                Moment(
                    id = "1",
                    body = "Morning Coffee Run. Got my favorite oat milk latte from the corner shop.",
                    createdAt = Clock.System.now(),
                    mood = Mood.ENERGETIC,
                )
                ),
                isLoading = false,
                selectedDate = Clock.System.todayIn(kotlinx.datetime.TimeZone.currentSystemDefault())
            ),
            formatter = mockFormatter,
            timeZone = kotlinx.datetime.TimeZone.currentSystemDefault(),
            onAction = {}
        )
    }
}
