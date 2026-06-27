package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.core.design_system.theme.rememberInsightsPalette

@Composable
internal fun PagerDots(count: Int, current: Int, palette: InsightsPalette) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { i ->
            val active = i == current
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .height(6.dp)
                    .width(if (active) 18.dp else 6.dp)
                    .clip(CircleShape)
                    .background(if (active) palette.sub else palette.dotGray)
            )
        }
    }
}

@Preview
@Composable
private fun PagerDotsPreview() {
    AppTheme {
        val palette = rememberInsightsPalette()
        Box(Modifier.background(palette.pageBg).padding(16.dp)) {
            PagerDots(count = 3, current = 0, palette = palette)
        }
    }
}
