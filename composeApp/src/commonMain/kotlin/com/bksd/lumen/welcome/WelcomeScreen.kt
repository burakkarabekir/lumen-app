package com.bksd.lumen.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.coverGradient
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.lumen.Res
import com.bksd.lumen.welcome_greeting_new
import com.bksd.lumen.welcome_greeting_returning
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun WelcomeScreen(
    greeting: WelcomeGreeting,
    firstName: String,
    modifier: Modifier = Modifier,
) {
    val inspection = LocalInspectionMode.current
    val alpha = remember { Animatable(if (inspection) 1f else 0f) }
    val translationY = remember { Animatable(if (inspection) 0f else 20f) }

    LaunchedEffect(Unit) {
        if (!inspection) {
            launch { alpha.animateTo(1f, animationSpec = tween(durationMillis = 700)) }
            translationY.animateTo(0f, animationSpec = tween(durationMillis = 700))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.spacing.xxxl)
                .graphicsLayer {
                    this.alpha = alpha.value
                    this.translationY = translationY.value.dp.toPx()
                },
        ) {
            Text(
                text = when (greeting) {
                    WelcomeGreeting.RETURNING -> stringResource(Res.string.welcome_greeting_returning)
                    WelcomeGreeting.NEW -> stringResource(Res.string.welcome_greeting_new)
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            if (firstName.isNotBlank()) {
                Text(
                    text = firstName,
                    style = MaterialTheme.typography.displayMedium.copy(
                        brush = Brush.linearGradient(MaterialTheme.colorScheme.extended.coverGradient),
                        fontWeight = FontWeight.Bold,
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenReturningPreview() {
    AppTheme {
        WelcomeScreen(greeting = WelcomeGreeting.RETURNING, firstName = "Burak")
    }
}

@Preview
@Composable
private fun WelcomeScreenNewPreview() {
    AppTheme {
        WelcomeScreen(greeting = WelcomeGreeting.NEW, firstName = "Burak")
    }
}
