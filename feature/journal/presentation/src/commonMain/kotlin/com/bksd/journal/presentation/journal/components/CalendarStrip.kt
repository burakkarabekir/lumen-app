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

@Composable
fun CalendarStrip(
    selectedDate: LocalDate?,
    timeZone: TimeZone,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { Clock.System.todayIn(timeZone) }
    val effectiveSelected = selectedDate ?: today

    // Generate dates: 4 past days + today + 3 future days
    val dates = remember(today) {
        (-4..3).map { offset ->
            if (offset < 0) today.minus(-offset, DateTimeUnit.DAY)
            else today.plus(offset, DateTimeUnit.DAY)
        }
    }

    // Auto-scroll to selected date (today by default, index 4)
    val listState = rememberLazyListState()
    val selectedIndex = remember(effectiveSelected, dates) {
        dates.indexOf(effectiveSelected).coerceAtLeast(0)
    }
    LaunchedEffect(selectedIndex) {
        listState.animateScrollToItem(maxOf(0, selectedIndex - 1))
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item { Spacer(modifier = Modifier.width(16.dp)) }
        items(dates) { date ->
            val isSelected = date == effectiveSelected
            val isToday = date == today
            val isFuture = date > today

            val dayOfWeek = date.dayOfWeek.name
                .take(3)
                .lowercase()
                .replaceFirstChar { it.uppercase() }
            val dayOfMonth = date.dayOfMonth.toString()

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
