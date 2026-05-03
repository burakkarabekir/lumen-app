package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

private const val PAST_DAYS = 14
private const val FUTURE_DAYS = 3

/**
 * Horizontally scrollable date strip that dynamically centers around [selectedDate].
 *
 * The strip generates a window of dates from [PAST_DAYS] before to [FUTURE_DAYS]
 * after the selected date, capped at today for the future boundary. This allows
 * the strip to follow the user as they scroll through the journal feed to any date.
 */
@Composable
fun CalendarStrip(
    selectedDate: LocalDate?,
    timeZone: TimeZone,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { Clock.System.todayIn(timeZone) }
    val effectiveSelected = selectedDate ?: today

    // Dynamic window centered on the selected date, capped at today + FUTURE_DAYS
    val dates = remember(effectiveSelected, today) {
        val futureCap = today.plus(FUTURE_DAYS, DateTimeUnit.DAY)
        val rangeStart = effectiveSelected.minus(PAST_DAYS, DateTimeUnit.DAY)
        val rangeEnd = minOf(
            effectiveSelected.plus(FUTURE_DAYS, DateTimeUnit.DAY),
            futureCap
        )

        generateSequence(rangeStart) { it.plus(1, DateTimeUnit.DAY) }
            .takeWhile { it <= rangeEnd }
            .toList()
    }

    // Position the selected date near the center of the visible area
    val listState = rememberLazyListState()
    val selectedIndex = remember(effectiveSelected, dates) {
        dates.indexOf(effectiveSelected).coerceAtLeast(0)
    }
    LaunchedEffect(selectedIndex) {
        // Offset by ~3 so the selected date appears center-ish, not at the edge
        listState.animateScrollToItem(maxOf(0, selectedIndex - 3))
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item { Spacer(modifier = Modifier.width(16.dp)) }
        items(dates, key = { it.toEpochDays() }) { date ->
            val isSelected = date == effectiveSelected
            val isToday = date == today
            val isFuture = date > today

            val dayOfWeek = date.dayOfWeek.name
                .take(3)
                .lowercase()
                .replaceFirstChar { it.uppercase() }
            val dayOfMonth = date.day.toString()

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when {
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> Color.Transparent
                        }
                    )
                    .clickable(enabled = !isFuture) { onDateSelect(date) }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Day of week label
                    Text(
                        text = dayOfWeek,
                        color = when {
                            isSelected -> MaterialTheme.colorScheme.onPrimary
                            isFuture -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Day number
                    Text(
                        text = dayOfMonth,
                        color = when {
                            isSelected -> MaterialTheme.colorScheme.onPrimary
                            isFuture -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        fontSize = 16.sp,
                        fontWeight = if (isToday || isSelected) FontWeight.ExtraBold else FontWeight.Bold
                    )

                    // "Today" dot indicator
                    if (isToday && !isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    } else {
                        // Reserve space for alignment
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.width(8.dp)) }
    }
}
