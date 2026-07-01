package com.bksd.insights.presentation.reflection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.insights.presentation.reflection.components.WeeklyReflectionCard
import com.bksd.insights.presentation.reflection.components.WeeklyReflectionLoadingCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeeklyReflectionRoot(
    onViewFullReflection: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<ReflectionViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ReflectionEvent.NavigateToFullReflection -> onViewFullReflection()
        }
    }

    val reflection = state.reflection
    when {
        reflection != null -> Column(modifier) {
            WeeklyReflectionCard(
                reflection = reflection,
                onViewFull = { viewModel.onAction(ReflectionAction.OnViewFull) }
            )
            Spacer(Modifier.height(18.dp))
        }

        state.isGenerating -> Column(modifier) {
            WeeklyReflectionLoadingCard()
            Spacer(Modifier.height(18.dp))
        }

        else -> Unit
    }
}
