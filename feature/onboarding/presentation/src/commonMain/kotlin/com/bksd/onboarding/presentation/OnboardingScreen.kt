package com.bksd.onboarding.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.LumenBase500
import com.bksd.core.design_system.theme.LumenBase900
import com.bksd.core.design_system.theme.LumenBrand500
import com.bksd.core.design_system.theme.LumenBrand600
import com.bksd.core.design_system.theme.LumenBrand700
import com.bksd.core.design_system.theme.LumenRadius
import com.bksd.core.design_system.theme.LumenSpacing
import com.bksd.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingRoot(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: OnboardingViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            OnboardingEvent.NavigateToAuth -> onComplete()
        }
    }

    OnboardingScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
internal fun OnboardingScreen(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val step = onboardingSteps[state.currentStep]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = LumenSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top bar with Skip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = LumenSpacing.xxl),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Momentum",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (!state.isLastStep) {
                TextButton(onClick = { onAction(OnboardingAction.Skip) }) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Illustration placeholder
        AnimatedContent(
            targetState = state.currentStep,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it } + fadeOut())
            },
            label = "onboarding_illustration",
        ) { step ->
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(LumenRadius.lg))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                LumenBrand700.copy(alpha = 0.3f),
                                LumenBase900.copy(alpha = 0.5f),
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                // Placeholder for 3D illustrations
                Text(
                    text = onboardingSteps[step].highlightedWord,
                    style = MaterialTheme.typography.headlineLarge,
                    color = LumenBrand500.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.height(LumenSpacing.xxxl))

        // Title
        AnimatedContent(
            targetState = state.currentStep,
            transitionSpec = {
                (fadeIn()) togetherWith (fadeOut())
            },
            label = "onboarding_title",
        ) { currentStep ->
            val currentStepData = onboardingSteps[currentStep]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentStepData.title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = currentStepData.highlightedWord,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 32.sp,
                    ),
                    color = LumenBrand500,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.height(LumenSpacing.lg))

        // Subtitle
        AnimatedContent(
            targetState = state.currentStep,
            transitionSpec = {
                (fadeIn()) togetherWith (fadeOut())
            },
            label = "onboarding_subtitle",
        ) { currentStep ->
            Text(
                text = onboardingSteps[currentStep].subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = LumenSpacing.lg),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Page indicators
        PageIndicator(
            currentPage = state.currentStep,
            totalPages = state.totalSteps,
            modifier = Modifier.padding(bottom = LumenSpacing.xxl),
        )

        // Next / Get Started button
        Button(
            onClick = {
                if (state.isLastStep) {
                    onAction(OnboardingAction.Complete)
                } else {
                    onAction(OnboardingAction.Next)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(LumenRadius.full),
            colors = ButtonDefaults.buttonColors(
                containerColor = LumenBrand600,
            ),
        ) {
            Text(
                text = if (state.isLastStep) "Get Started" else "Next",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Spacer(modifier = Modifier.height(LumenSpacing.xxxl))
    }
}

@Composable
private fun PageIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(LumenSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) LumenBrand500
                        else LumenBase500.copy(alpha = 0.4f)
                    )
            )
        }
    }
}
