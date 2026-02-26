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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.bksd.core.design_system.component.button.fab.AppFab
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.LumenTheme
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.journal.presentation.journal.components.CalendarStrip
import com.bksd.journal.presentation.journal.components.FilterChips
import com.bksd.journal.presentation.journal.components.JournalEmptyState
import com.bksd.journal.presentation.journal.components.MomentCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JournalRoot(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
) {
    val viewModel = koinViewModel<JournalViewModel>()
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
        state = viewModel._state,
        onAction = viewModel::onAction
    )
}

@Composable
fun JournalScreen(
    state: JournalState,
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
                    title = "Journal",
                    style = AppBarStyle.Root,
                )
            }
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (state.moments.isEmpty()) {
                JournalEmptyState()
            } else {
                CalendarStrip()
                AppDivider()

                // Filter Chips
                FilterChips(
                    selectedFilter = state.selectedFilter,
                    onFilterSelect = { onAction(JournalAction.OnFilterSelect(it)) },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                AppDivider()

                // Timeline List
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.moments, key = { it.id }) { moment ->
                        MomentCard(
                            moment = moment,
                            onClick = { onAction(JournalAction.OnMomentClick(moment.id)) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(128.dp))
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
                    contentDescription = "Create Moment"
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    LumenTheme {
        JournalScreen(
            state = JournalState(),
            onAction = {}
        )
    }
}