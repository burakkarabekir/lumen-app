package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun EntriesBarChart(
    bars: ImmutableList<Int>,
    axisLabels: ImmutableList<String>,
    modifier: Modifier
) {
    val maxBar = (bars.maxOrNull() ?: 1).coerceAtLeast(1)
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xxs),
            verticalAlignment = Alignment.Bottom
        ) {
            bars.forEach { value ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(value.toFloat() / maxBar)
                        .clip(RoundedCornerShape(topStart = MaterialTheme.dimens.radius.xs, topEnd = MaterialTheme.dimens.radius.xs))
                        .background(Color.White.copy(alpha = 0.62f))
                )
            }
        }
        if (axisLabels.isNotEmpty()) {
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xs))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                axisLabels.forEach { label ->
                    Text(
                        text = label,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.55f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EntriesBarChartPreview() {
    AppTheme {
        Box(Modifier.background(Color(0xFF7682D6)).padding(MaterialTheme.dimens.spacing.lg)) {
            EntriesBarChart(
                bars = SampleEntries.bars,
                axisLabels = SampleEntries.axisLabels,
                modifier = Modifier.width(160.dp).height(MaterialTheme.dimens.size.topBar)
            )
        }
    }
}
