package com.bksd.onboarding.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.LumenBrand200
import com.bksd.core.design_system.theme.LumenBrand400
import com.bksd.core.design_system.theme.LumenBrand600
import com.bksd.core.design_system.theme.LumenBrand900

@Composable
internal fun OnboardingOrbHero(modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val transition = rememberInfiniteTransition(label = "orb")
    val floatY by transition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(tween(3600, easing = LinearEasing), RepeatMode.Reverse),
        label = "orbFloat",
    )
    val pulse by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Reverse),
        label = "orbPulse",
    )

    Box(
        modifier = modifier
            .size(260.dp)
            .graphicsLayer { translationY = floatY.dp.toPx() },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .graphicsLayer {
                    scaleX = pulse
                    scaleY = pulse
                }
                .background(
                    brush = Brush.radialGradient(
                        listOf(primary.copy(alpha = 0.5f), primary.copy(alpha = 0f)),
                    ),
                    shape = CircleShape,
                ),
        )
        Box(
            modifier = Modifier
                .size(186.dp)
                .border(1.dp, primary.copy(alpha = 0.28f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(132.dp)
                .clip(CircleShape)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0f to LumenBrand200,
                                0.34f to LumenBrand400,
                                0.70f to LumenBrand600,
                                1f to LumenBrand900,
                            ),
                            center = Offset(size.width * 0.38f, size.height * 0.32f),
                            radius = size.width * 0.82f,
                        ),
                    )
                },
            contentAlignment = Alignment.TopStart,
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 26.dp, top = 22.dp)
                    .size(width = 34.dp, height = 24.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(Color.White.copy(alpha = 0.85f), Color.White.copy(alpha = 0f)),
                        ),
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingOrbHeroPreview() {
    AppTheme {
        Box(Modifier.size(320.dp), contentAlignment = Alignment.Center) {
            OnboardingOrbHero()
        }
    }
}
