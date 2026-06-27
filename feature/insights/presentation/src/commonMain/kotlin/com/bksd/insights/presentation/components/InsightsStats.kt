package com.bksd.insights.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun EntriesCard(
    entries: EntriesStat,
    selectedRange: StatsRange,
    onRangeSelect: (StatsRange) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF7682D6), Color(0xFF9281C6))))
            .padding(18.dp)
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
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Entries",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.92f)
                )
            }
            EntriesBarChart(
                bars = entries.bars,
                modifier = Modifier.width(155.dp).height(74.dp)
            )
        }
        Spacer(Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            entries.breakdown.forEach { item -> BreakdownItem(item) }
        }
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            StatsRange.entries.forEach { range ->
                RangeChip(
                    label = range.label,
                    selected = range == selectedRange,
                    onClick = { onRangeSelect(range) }
                )
            }
        }
    }
}

@Composable
private fun RowScope.BreakdownItem(item: StatItem) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${item.value}",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = item.label,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.72f),
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun EntriesBarChart(bars: ImmutableList<Int>, modifier: Modifier) {
    val maxBar = (bars.maxOrNull() ?: 1).coerceAtLeast(1)
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            bars.forEach { value ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(value.toFloat() / maxBar)
                        .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                        .background(Color.White.copy(alpha = 0.62f))
                )
            }
        }
        Spacer(Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("2024", "2024", "2025").forEach {
                Text(it, fontSize = 8.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun RangeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(if (selected) Color.White.copy(alpha = 0.22f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.55f)
        )
    }
}

@Composable
internal fun VisitedPlacesCard(places: ImmutableList<VisitedPlace>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF2B2D48), Color(0xFF1B1C2B))))
            .padding(16.dp)
    ) {
        Text(
            text = "Visited Places",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(14.dp))
        places.chunked(2).forEachIndexed { index, rowPlaces ->
            if (index > 0) Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowPlaces.forEach { place -> PlaceChip(place, Modifier.weight(1f)) }
                if (rowPlaces.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun PlaceChip(place: VisitedPlace, modifier: Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.07f))
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = placeIcon(place.kind),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = place.name,
            modifier = Modifier.weight(1f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "${place.count}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.45f)
        )
    }
}

@Composable
internal fun WrittenJournaledRow(writtenWords: Int, journaled: JournaledStat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        WrittenCard(writtenWords, Modifier.weight(1f))
        JournaledCard(journaled, Modifier.weight(1f))
    }
}

@Composable
private fun WrittenCard(words: Int, modifier: Modifier) {
    Column(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFFD3796A), Color(0xFFC0584F))))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Written",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.9f)
        )
        Column {
            Text(
                text = words.grouped(),
                fontSize = 36.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "Words",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun JournaledCard(stat: JournaledStat, modifier: Modifier) {
    Column(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFFCF524B), Color(0xFFA8474F))))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Journaled",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.9f)
        )
        Column {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${stat.days}",
                    fontSize = 44.sp,
                    lineHeight = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "Days",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniStat("${stat.thisMonth}", "This Month")
                MiniStat("${stat.thisYear}", "This Year")
            }
        }
    }
}

@Composable
private fun MiniStat(value: String, label: String) {
    Column {
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text(label, fontSize = 9.5.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.6f))
    }
}

private fun placeIcon(kind: PlaceKind): ImageVector =
    when (kind) {
        PlaceKind.BEACH -> Icons.Default.BeachAccess
        PlaceKind.LANDMARK -> Icons.Default.AccountBalance
        PlaceKind.PARK -> Icons.Default.Park
        PlaceKind.RESTAURANT -> Icons.Default.Restaurant
    }

private fun Int.grouped(): String =
    toString().reversed().chunked(3).joinToString(",").reversed()
