package com.bksd.lumen.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

private const val MIN_VISIBLE_MILLIS = 2000L
private const val EXIT_MILLIS = 500L

@Composable
fun WelcomeGate(
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var active by remember { mutableStateOf<WelcomeGreeting?>(null) }
    var firstName by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(state.greeting) {
        val greeting = state.greeting ?: return@LaunchedEffect
        active = greeting
        firstName = state.firstName
        visible = true
        viewModel.consume()
    }

    LaunchedEffect(active) {
        if (active != null) {
            delay(MIN_VISIBLE_MILLIS)
            visible = false
            delay(EXIT_MILLIS)
            active = null
        }
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = EnterTransition.None,
        exit = fadeOut(animationSpec = tween(durationMillis = EXIT_MILLIS.toInt())),
    ) {
        active?.let { WelcomeScreen(greeting = it, firstName = firstName) }
    }
}
