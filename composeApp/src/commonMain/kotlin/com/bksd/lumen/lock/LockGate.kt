package com.bksd.lumen.lock

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bksd.core.presentation.biometric.BiometricResult
import com.bksd.core.presentation.biometric.rememberBiometricAuthenticator
import com.bksd.lumen.Res
import com.bksd.lumen.lock_gate_prompt_subtitle
import com.bksd.lumen.lock_gate_prompt_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LockGate(
    modifier: Modifier = Modifier,
    viewModel: LockGateViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (!state.loaded) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) { detectTapGestures { } },
        )
        return
    }

    val authenticator = rememberBiometricAuthenticator()
    val scope = rememberCoroutineScope()
    val promptTitle = stringResource(Res.string.lock_gate_prompt_title)
    val promptSubtitle = stringResource(Res.string.lock_gate_prompt_subtitle)

    var locked by rememberSaveable { mutableStateOf(state.enabled) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, state.enabled) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP && state.enabled) {
                locked = true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(state.enabled) {
        if (!state.enabled) locked = false
    }

    LaunchedEffect(locked) {
        if (locked) {
            if (!authenticator.isAvailable()) {
                locked = false
                return@LaunchedEffect
            }
            val result = authenticator.authenticate(promptTitle, promptSubtitle)
            if (result == BiometricResult.SUCCESS) locked = false
        }
    }

    AnimatedVisibility(
        visible = locked,
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        LockGateScreen(
            onUnlock = {
                scope.launch {
                    if (!authenticator.isAvailable()) {
                        locked = false
                        return@launch
                    }
                    val result = authenticator.authenticate(promptTitle, promptSubtitle)
                    if (result == BiometricResult.SUCCESS) locked = false
                }
            },
        )
    }
}
