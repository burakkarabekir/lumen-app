package com.bksd.onboarding.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens

@Composable
internal fun OnboardingPageIndicator(
    count: Int,
    current: Int,
    inactiveColor: Color,
    modifier: Modifier = Modifier,
) {
    val activeColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(count) { index ->
            val active = index == current
            val width by animateDpAsState(if (active) 20.dp else 7.dp, label = "dotWidth")
            Box(
                modifier = Modifier
                    .height(7.dp)
                    .width(width)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xs))
                    .background(if (active) activeColor else inactiveColor),
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingPageIndicatorPreview() {
    AppTheme {
        OnboardingPageIndicator(
            count = 3,
            current = 0,
            inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.xxl),
        )
    }
}
