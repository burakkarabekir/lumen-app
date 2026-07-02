package com.bksd.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.profile.presentation.RemindersState
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.action_close
import com.bksd.profile.presentation.reminders
import com.bksd.profile.presentation.reminders_daily
import com.bksd.profile.presentation.reminders_repeat
import com.bksd.profile.presentation.reminders_streak
import com.bksd.profile.presentation.reminders_streak_description
import com.bksd.profile.presentation.reminders_subtitle
import com.bksd.profile.presentation.reminders_time
import org.jetbrains.compose.resources.stringResource

private val DayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

internal fun formatTimeLabel(hour: Int, minute: Int): String {
    val h12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val amPm = if (hour < 12) "AM" else "PM"
    return "$h12:${minute.toString().padStart(2, '0')} $amPm"
}

@Composable
internal fun RemindersSheetContent(
    state: RemindersState,
    onClose: () -> Unit,
    onDailyToggle: (Boolean) -> Unit,
    onTimeClick: () -> Unit,
    onDayToggle: (Int) -> Unit,
    onStreakToggle: (Boolean) -> Unit,
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg
    val card = palette.surface
    val pill = lerp(palette.surface, palette.text, 0.06f)

    Column(modifier = Modifier.fillMaxWidth().padding(start = MaterialTheme.dimens.spacing.xl, end = MaterialTheme.dimens.spacing.xl, bottom = MaterialTheme.dimens.spacing.xxxl)) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.dimens.spacing.xs, bottom = MaterialTheme.dimens.spacing.lg)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.reminders),
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp,
                    color = palette.text
                )
                Text(
                    text = stringResource(Res.string.reminders_subtitle),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = palette.sub,
                    modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xs)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.spacing.md)
                    .size(MaterialTheme.dimens.icon.avatar)
                    .clip(CircleShape)
                    .background(pill)
                    .clickable(onClick = onClose)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.action_close),
                    tint = palette.sub,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
                .background(card)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(MaterialTheme.dimens.icon.avatar)
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                            .background(accent.copy(alpha = 0.14f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.md)
                        )
                    }
                    Text(
                        text = stringResource(Res.string.reminders_daily),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.text
                    )
                }
                ReminderToggle(checked = state.dailyEnabled, onCheckedChange = onDailyToggle)
            }

            Box(
                modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive).fillMaxWidth().height(1.dp)
                    .background(palette.hairline)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg)
            ) {
                Text(
                    text = stringResource(Res.string.reminders_time),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.text
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .background(pill)
                        .clickable(onClick = onTimeClick)
                        .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.sm)
                ) {
                    Text(
                        text = formatTimeLabel(state.hour, state.minute),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp,
                        color = palette.text
                    )
                }
            }

            Box(
                modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.lg).fillMaxWidth().height(1.dp)
                    .background(palette.hairline)
            )

            Column(
                modifier = Modifier.padding(
                    start = MaterialTheme.dimens.spacing.lg,
                    end = MaterialTheme.dimens.spacing.lg,
                    top = MaterialTheme.dimens.spacing.lg,
                    bottom = MaterialTheme.dimens.spacing.lg
                )
            ) {
                Text(
                    text = stringResource(Res.string.reminders_repeat),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.text
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.dimens.spacing.md),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DayLabels.forEachIndexed { index, label ->
                        val day = index + 1
                        val selected = day in state.days
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(MaterialTheme.dimens.icon.avatar)
                                .clip(CircleShape)
                                .background(if (selected) accent else pill)
                                .clickable { onDayToggle(day) }
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) Color.White else palette.sub
                            )
                        }
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.lg),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.dimens.spacing.md)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
                .background(card)
                .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.reminders_streak),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.text
                )
                Text(
                    text = stringResource(Res.string.reminders_streak_description),
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp,
                    color = palette.sub,
                    modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xs)
                )
            }
            ReminderToggle(checked = state.streakEnabled, onCheckedChange = onStreakToggle)
        }
    }
}

@Preview
@Composable
private fun RemindersSheetContentPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(top = MaterialTheme.dimens.spacing.md)) {
            RemindersSheetContent(
                state = RemindersState(
                    dailyEnabled = true,
                    hour = 20,
                    minute = 0,
                    days = setOf(1, 2, 3, 4, 5),
                    streakEnabled = false,
                    loaded = true
                ),
                onClose = {},
                onDailyToggle = {},
                onTimeClick = {},
                onDayToggle = {},
                onStreakToggle = {}
            )
        }
    }
}
