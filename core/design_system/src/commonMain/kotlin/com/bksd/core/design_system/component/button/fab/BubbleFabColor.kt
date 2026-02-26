package com.bksd.core.design_system.component.button.fab

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import com.bksd.core.design_system.theme.buttonGradient
import com.bksd.core.design_system.theme.buttonGradientPressed
import com.bksd.core.design_system.theme.primary90
import com.bksd.core.design_system.theme.primary95

data class BubbleFabColor(
    val primary: Brush,
    val primaryPressed: Brush,
    val outerCircle: Brush,
    val innerCircle: Brush,
)

@Composable
fun rememberBubbleFabColor(
    primary: Brush = MaterialTheme.colorScheme.buttonGradient,
    primaryPressed: Brush = MaterialTheme.colorScheme.buttonGradientPressed,
    outerCircle: Brush = SolidColor(MaterialTheme.colorScheme.primary95),
    innerCircle: Brush = SolidColor(MaterialTheme.colorScheme.primary90),
): BubbleFabColor = remember(primary, primaryPressed, outerCircle, innerCircle) {
    BubbleFabColor(
        primary = primary,
        primaryPressed = primaryPressed,
        outerCircle = outerCircle,
        innerCircle = innerCircle
    )
}