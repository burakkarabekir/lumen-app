package com.bksd.journal.presentation.journal.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.Res
import com.bksd.core.design_system.sparkle
import com.bksd.core.design_system.theme.LumenTheme
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun JournalEmptyState(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "emptyState")

    // Outer ring: slow rotation
    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outerRotation"
    )

    // Middle ring: breathe (scale pulse)
    val middleScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "middleScale"
    )

    // Inner glow: soft pulse
    val innerGlow by infiniteTransition.animateFloat(
        initialValue = 0.06f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "innerGlow"
    )

    // Sparkle icon: gentle float
    val iconFloat by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconFloat"
    )

    // Orbiting dots phase
    val orbitPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitPhase"
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(200.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = this.center
                val minDim = size.minDimension

                // Outer ring — gradient arc, slowly rotating
                rotate(degrees = outerRotation, pivot = center) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            0f to primaryColor.copy(alpha = 0.0f),
                            0.4f to primaryColor.copy(alpha = 0.35f),
                            0.7f to primaryColor.copy(alpha = 0.12f),
                            1f to primaryColor.copy(alpha = 0.0f)
                        ),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 1.5f),
                        size = size
                    )
                }

                // Middle ring — breathing scale
                val middleRadius = (minDim / 2.6f) * middleScale
                drawCircle(
                    color = primaryColor.copy(alpha = 0.25f),
                    radius = middleRadius,
                    center = center,
                    style = Stroke(width = 1.2f)
                )

                // Inner filled circle — soft glow pulse
                drawCircle(
                    color = primaryColor.copy(alpha = innerGlow),
                    radius = minDim / 3.5f,
                    center = center
                )

                // Orbiting dots — 3 small dots circling the middle ring
                val orbitRadius = minDim / 2.8f
                for (i in 0 until 3) {
                    val angle = orbitPhase + (i * 2 * PI / 3).toFloat()
                    val dotX = center.x + orbitRadius * cos(angle)
                    val dotY = center.y + orbitRadius * sin(angle)
                    val dotAlpha = (0.2f + 0.4f * ((sin(angle) + 1f) / 2f))
                    drawCircle(
                        color = primaryColor.copy(alpha = dotAlpha),
                        radius = 2.5f,
                        center = androidx.compose.ui.geometry.Offset(dotX, dotY)
                    )
                }
            }

            // Sparkle icon — floating gently
            Icon(
                imageVector = vectorResource(Res.drawable.sparkle),
                contentDescription = "Sparkle",
                tint = primaryColor,
                modifier = Modifier
                    .size(48.dp)
                    .offset(y = iconFloat.dp)
                    .graphicsLayer {
                        // Subtle scale pulse tied to inner glow
                        val s = 1f + (innerGlow - 0.06f) * 0.6f
                        scaleX = s
                        scaleY = s
                    }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "Your first moment\nstarts here",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle
        Text(
            text = "Tap + to capture a thought, a feeling,\nor whatever's on your mind.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PreviewJournalEmptyState() {
    LumenTheme {
        JournalEmptyState()
    }
}
