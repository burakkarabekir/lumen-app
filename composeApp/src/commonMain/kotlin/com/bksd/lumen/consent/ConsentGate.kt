package com.bksd.lumen.consent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConsentGate(
    modifier: Modifier = Modifier,
    viewModel: ConsentGateViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AnimatedVisibility(
        visible = state.visible,
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        ConsentGateScreen(
            isLoading = state.isLoading,
            onAgree = viewModel::onAgree,
        )
    }
}
