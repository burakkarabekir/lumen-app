package com.bksd.insights.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.insights.presentation.reflection.WeeklyReflectionRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InsightsRoot(
    onViewFullReflection: () -> Unit,
    onNavigateToPaywall: () -> Unit,
) {
    val viewModel = koinViewModel<InsightsViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is InsightsEvent.ShowError -> Unit
        }
    }

    InsightsScreen(
        state = state,
        onAction = viewModel::onAction,
        reflectionSlot = {
            WeeklyReflectionRoot(
                onViewFullReflection = onViewFullReflection,
                onNavigateToPaywall = onNavigateToPaywall,
            )
        }
    )
}
