package com.bksd.insights.presentation.reflection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.insights.presentation.reflection.components.WeeklyReflectionCard
import com.bksd.insights.presentation.reflection.components.WeeklyReflectionErrorCard
import com.bksd.insights.presentation.reflection.components.WeeklyReflectionLoadingCard
import com.bksd.insights.presentation.reflection.components.WeeklyReflectionLockedCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeeklyReflectionRoot(
    onViewFullReflection: () -> Unit,
    onNavigateToPaywall: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<ReflectionViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ReflectionEvent.NavigateToFullReflection -> onViewFullReflection()
            ReflectionEvent.NavigateToPaywall -> onNavigateToPaywall()
        }
    }

    val reflection = state.reflection
    when {
        !state.isPremium -> Column(modifier) {
            WeeklyReflectionLockedCard(
                onUnlock = { viewModel.onAction(ReflectionAction.OnUnlock) }
            )
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        }

        reflection != null -> Column(modifier) {
            WeeklyReflectionCard(
                reflection = reflection,
                onViewFull = { viewModel.onAction(ReflectionAction.OnViewFull) }
            )
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        }

        state.isGenerating -> Column(modifier) {
            WeeklyReflectionLoadingCard()
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        }

        state.error -> Column(modifier) {
            WeeklyReflectionErrorCard(
                onRetry = { viewModel.onAction(ReflectionAction.OnRetry) }
            )
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        }

        else -> Unit
    }
}
