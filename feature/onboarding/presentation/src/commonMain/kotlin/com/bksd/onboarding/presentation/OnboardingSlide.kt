package com.bksd.onboarding.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.onboarding.presentation.component.OnboardingEntryCardHero
import com.bksd.onboarding.presentation.component.OnboardingOrbHero
import com.bksd.onboarding.presentation.component.OnboardingPageIndicator
import com.bksd.onboarding.presentation.component.OnboardingReflectionHero
import com.bksd.onboarding.presentation.component.OnboardingStep
import com.bksd.onboarding.presentation.component.onboardingSteps
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OnboardingSlide(
    step: OnboardingStep,
    index: Int,
    pageCount: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = rememberNewEntryPalette()
    val primary = MaterialTheme.colorScheme.primary
    val isLast = index == pageCount - 1

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(palette.pageBg)
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = 0.10f), Color.Transparent),
                        center = Offset(size.width / 2f, size.height * 0.14f),
                        radius = size.height * 0.55f,
                    ),
                )
            }
            .statusBarsPadding(),
    ) {
        if (!isLast) {
            Text(
                text = stringResource(Res.string.skip),
                style = MaterialTheme.typography.labelLarge,
                color = palette.sub,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = MaterialTheme.dimens.spacing.sm, end = MaterialTheme.dimens.spacing.md)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                    .clickable(onClick = onSkip)
                    .padding(MaterialTheme.dimens.spacing.xs),
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                when (index) {
                    0 -> OnboardingOrbHero()
                    1 -> OnboardingEntryCardHero()
                    else -> OnboardingReflectionHero()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.dimens.spacing.xl,
                        end = MaterialTheme.dimens.spacing.xl,
                        bottom = MaterialTheme.dimens.spacing.xxl,
                    ),
            ) {
                Text(
                    text = stringResource(step.titleRes),
                    style = MaterialTheme.typography.headlineMedium,
                    color = palette.text,
                )
                Text(
                    text = stringResource(step.bodyRes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = palette.sub,
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.spacing.sm)
                        .widthIn(max = 300.dp),
                )
                OnboardingPageIndicator(
                    count = pageCount,
                    current = index,
                    inactiveColor = palette.sub.copy(alpha = 0.35f),
                    modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg),
                )
                AppButton(
                    text = stringResource(step.ctaRes),
                    onClick = if (step.advances) onNext else onFinish,
                    style = AppButtonStyle.PRIMARY,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.dimens.spacing.lg),
                    trailingIcon = if (step.showArrow) {
                        {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(MaterialTheme.dimens.icon.sm),
                            )
                        }
                    } else {
                        null
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingSlidePreview() {
    AppTheme {
        OnboardingSlide(
            step = onboardingSteps.first(),
            index = 0,
            pageCount = 3,
            onSkip = {},
            onNext = {},
            onFinish = {},
        )
    }
}
