package com.bksd.onboarding.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.onboarding.presentation.component.onboardingSteps
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingRoot(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: OnboardingViewModel = koinViewModel()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            OnboardingEvent.Finished -> onComplete()
        }
    }

    OnboardingScreen(
        onFinish = { viewModel.onAction(OnboardingAction.Complete) },
        modifier = modifier,
    )
}

@Composable
internal fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val steps = onboardingSteps
    val pagerState = rememberPagerState(pageCount = { steps.size })
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
    ) { page ->
        OnboardingSlide(
            step = steps[page],
            index = page,
            pageCount = steps.size,
            onSkip = onFinish,
            onNext = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
            onFinish = onFinish,
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    AppTheme {
        OnboardingScreen(onFinish = {})
    }
}

@Preview
@Composable
private fun OnboardingScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        OnboardingScreen(onFinish = {})
    }
}
