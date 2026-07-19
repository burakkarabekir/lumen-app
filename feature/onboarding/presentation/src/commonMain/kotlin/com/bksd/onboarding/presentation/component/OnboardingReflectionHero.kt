package com.bksd.onboarding.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.aiIconGradient
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.insightsEntriesGradient
import com.bksd.core.design_system.theme.insightsStreakGradient
import com.bksd.core.design_system.theme.onboardingReflectionBarIdle

private val ReflectionBars = listOf(14, 22, 18, 30, 26, 38, 28)

@Composable
internal fun OnboardingReflectionHero(modifier: Modifier = Modifier) {
    val extended = MaterialTheme.colorScheme.extended
    val transition = rememberInfiniteTransition(label = "reflection")
    val floatY by transition.animateFloat(
        initialValue = 0f,
        targetValue = -9f,
        animationSpec = infiniteRepeatable(tween(3250, easing = LinearEasing), RepeatMode.Reverse),
        label = "floatY",
    )

    Column(
        modifier = modifier
            .width(256.dp)
            .graphicsLayer { translationY = floatY.dp.toPx() }
            .shadow(20.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(extended.insightsStreakGradient))
            .padding(18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(Brush.linearGradient(extended.aiIconGradient)),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.size(13.dp).clip(CircleShape).background(Color.White))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = "Weekly Reflection",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold, fontSize = 14.sp),
                    color = Color.White,
                )
                Text(
                    text = "6 entries · this week",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 10.5.sp),
                    color = Color.White.copy(alpha = 0.5f),
                )
            }
        }
        Text(
            text = "This week leaned quiet — mornings, gratitude, and a wish for slower days.",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 12.5.sp, lineHeight = 20.sp),
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 13.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            ReflectionBars.forEachIndexed { index, barHeight ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(barHeight.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (index >= 3) {
                                Brush.verticalGradient(extended.insightsEntriesGradient)
                            } else {
                                Brush.verticalGradient(listOf(extended.onboardingReflectionBarIdle, extended.onboardingReflectionBarIdle))
                            },
                        ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingReflectionHeroPreview() {
    AppTheme {
        Box(Modifier.size(340.dp), contentAlignment = Alignment.Center) {
            OnboardingReflectionHero()
        }
    }
}
