package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.insightsEntriesGradient
import com.bksd.insights.presentation.EntriesStat
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.StatsRange
import com.bksd.insights.presentation.stat_entries
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EntriesCard(
    entries: EntriesStat,
    selectedRange: StatsRange,
    rangeOptions: ImmutableList<StatsRange>,
    onRangeSelect: (StatsRange) -> Unit
) {
    val gradient = MaterialTheme.colorScheme.extended.insightsEntriesGradient
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(Brush.verticalGradient(gradient))
            .padding(MaterialTheme.dimens.spacing.xl)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "${entries.total}",
                    fontSize = 50.sp,
                    lineHeight = 50.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxs))
                Text(
                    text = stringResource(Res.string.stat_entries),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.92f)
                )
            }
            EntriesBarChart(
                bars = entries.bars,
                axisLabels = entries.axisLabels,
                modifier = Modifier.width(160.dp).height(MaterialTheme.dimens.size.topBar)
            )
        }
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs)
        ) {
            entries.breakdown.forEach { item -> BreakdownItem(item) }
        }
        if (rangeOptions.size > 2) {
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)) {
                rangeOptions.forEach { range ->
                    RangeChip(
                        label = range.label,
                        selected = range == selectedRange,
                        onClick = { onRangeSelect(range) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EntriesCardPreview() {
    AppTheme {
        Box(Modifier.padding(MaterialTheme.dimens.spacing.xl)) {
            EntriesCard(
                entries = SampleEntries,
                selectedRange = StatsRange.AllTime,
                rangeOptions = persistentListOf(
                    StatsRange.AllTime,
                    StatsRange("2025", 2025),
                    StatsRange("2024", 2024)
                ),
                onRangeSelect = {}
            )
        }
    }
}
