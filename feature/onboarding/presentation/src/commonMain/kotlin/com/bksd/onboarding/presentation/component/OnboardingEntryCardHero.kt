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
import androidx.compose.foundation.layout.offset
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
import com.bksd.core.design_system.theme.MoodHueCalm
import com.bksd.core.design_system.theme.MoodHueGrateful
import com.bksd.core.design_system.theme.aiIconGradient
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette

@Composable
internal fun OnboardingEntryCardHero(modifier: Modifier = Modifier) {
    val palette = rememberNewEntryPalette()
    val stripe = MaterialTheme.colorScheme.extended.aiIconGradient
    val transition = rememberInfiniteTransition(label = "entryCard")
    val floatY by transition.animateFloat(
        initialValue = 0f,
        targetValue = -9f,
        animationSpec = infiniteRepeatable(tween(3250, easing = LinearEasing), RepeatMode.Reverse),
        label = "floatY",
    )

    Box(
        modifier = modifier
            .width(250.dp)
            .graphicsLayer { translationY = floatY.dp.toPx() },
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-14).dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(palette.hairline),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(18.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(palette.surface),
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(150.dp)
                    .background(Brush.verticalGradient(stripe)),
            )
            Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 15.dp)) {
                Text(
                    text = "London, UK",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.5.sp),
                    color = palette.pinFg,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(palette.pinBg)
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                )
                Text(
                    text = "Morning pages",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, letterSpacing = (-0.3).sp),
                    color = palette.text,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text(
                    text = "Fog over the hills, coffee on the balcony…",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 12.5.sp, lineHeight = 18.sp),
                    color = palette.sub,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    MoodChipRow(label = "Calm", accent = MoodHueCalm)
                    MoodChipRow(label = "Grateful", accent = MoodHueGrateful)
                }
            }
        }
    }
}

@Composable
private fun MoodChipRow(label: String, accent: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .background(accent.copy(alpha = 0.16f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(accent))
        Spacer(Modifier.width(5.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
            color = accent,
        )
    }
}

@Preview
@Composable
private fun OnboardingEntryCardHeroPreview() {
    AppTheme {
        Box(Modifier.size(340.dp), contentAlignment = Alignment.Center) {
            OnboardingEntryCardHero()
        }
    }
}
