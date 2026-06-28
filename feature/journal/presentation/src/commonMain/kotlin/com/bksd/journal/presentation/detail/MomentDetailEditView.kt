@file:OptIn(ExperimentalMaterial3Api::class)

package com.bksd.journal.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.Mood
import com.bksd.journal.presentation.detail.components.DetailTimePickerDialog
import com.bksd.journal.presentation.detail.components.EditSectionLabel
import com.bksd.journal.presentation.detail.components.MomentDetailAttachments
import com.bksd.journal.presentation.detail.components.MomentDetailEditMoodChip
import com.bksd.journal.presentation.detail.components.MomentDetailMoodChip
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

private val CoverColors = listOf(Color(0xFF7682D6), Color(0xFF9281C6), Color(0xFFCF6F64))

@Composable
fun MomentDetailEditView(
    state: MomentDetailState,
    moment: Moment,
    formatter: MomentFormatter,
    onAction: (MomentDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg
    val scrollState = rememberScrollState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pendingDateMillis by remember { mutableStateOf<Long?>(null) }
    var showMoodPicker by remember { mutableStateOf(false) }
    val createdAt = state.editCreatedAt ?: moment.createdAt

    Box(modifier = modifier.fillMaxSize().background(palette.pageBg)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(212.dp)
                    .background(Brush.linearGradient(CoverColors))
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0x2E000000)))
                val locationName = state.editLocation?.displayName
                if (!locationName.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 18.dp, bottom = 16.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White.copy(alpha = 0.22f))
                            .padding(start = 12.dp, end = 9.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = locationName,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0x47000000))
                                .clickable { onAction(MomentDetailAction.OnLocationRemove) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 22.dp, bottom = 150.dp)
            ) {
                EditSectionLabel("DATE & TIME", palette.sub)
                val localCreated = createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
                val monthShort = localCreated.month.name.lowercase()
                    .replaceFirstChar { it.uppercase() }
                    .take(3)
                val dateTimeLabel =
                    "$monthShort ${localCreated.day}, ${localCreated.year} · ${
                        formatter.formatTime(
                            createdAt
                        )
                    }"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(palette.surface)
                        .border(1.dp, palette.hairline, RoundedCornerShape(14.dp))
                        .clickable { showDatePicker = true }
                        .padding(horizontal = 14.dp, vertical = 13.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(
                        text = dateTimeLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.text,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = palette.sub,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(Modifier.height(18.dp))
                EditSectionLabel("TITLE", palette.sub)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(palette.surface)
                        .border(1.dp, palette.hairline, RoundedCornerShape(14.dp))
                        .padding(horizontal = 15.dp, vertical = 13.dp)
                ) {
                    BasicTextField(
                        value = state.editTitle,
                        onValueChange = { onAction(MomentDetailAction.OnTitleChange(it)) },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = palette.text
                        ),
                        cursorBrush = SolidColor(accent),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { inner ->
                            if (state.editTitle.isEmpty()) {
                                Text(
                                    text = "Title",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = palette.sub
                                )
                            }
                            inner()
                        }
                    )
                }

                Spacer(Modifier.height(18.dp))
                EditSectionLabel("MOODS", palette.sub)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    state.editMoods.forEach { mood ->
                        MomentDetailEditMoodChip(
                            mood = mood,
                            onRemove = { onAction(MomentDetailAction.OnMoodToggle(mood)) }
                        )
                    }
                    val moodPickerOpen = showMoodPicker
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.5.dp,
                                if (moodPickerOpen) accent else palette.hairline,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { showMoodPicker = !showMoodPicker }
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    ) {
                        Icon(
                            imageVector = if (moodPickerOpen) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            tint = if (moodPickerOpen) accent else palette.sub,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = if (moodPickerOpen) "Done" else "Add mood",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (moodPickerOpen) accent else palette.sub
                        )
                    }
                }
                if (showMoodPicker) {
                    Spacer(Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Mood.entries.filter { it !in state.editMoods }.forEach { mood ->
                            Box(
                                modifier = Modifier.clickable {
                                    onAction(MomentDetailAction.OnMoodToggle(mood))
                                }
                            ) {
                                MomentDetailMoodChip(mood)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))
                EditSectionLabel("ENTRY", palette.sub)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(palette.surface)
                        .border(1.5.dp, accent, RoundedCornerShape(14.dp))
                        .padding(15.dp)
                ) {
                    BasicTextField(
                        value = state.editBody,
                        onValueChange = { onAction(MomentDetailAction.OnBodyChange(it)) },
                        textStyle = TextStyle(
                            fontSize = 15.5.sp,
                            lineHeight = 25.sp,
                            color = palette.bodyText
                        ),
                        cursorBrush = SolidColor(accent),
                        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 120.dp),
                        decorationBox = { inner ->
                            if (state.editBody.isEmpty()) {
                                Text(
                                    text = "Write your thoughts...",
                                    fontSize = 15.5.sp,
                                    color = palette.sub
                                )
                            }
                            inner()
                        }
                    )
                }

                if (state.editAttachments.isNotEmpty()) {
                    val audioUrl = state.editAttachments
                        .filterIsInstance<AudioAttachment>()
                        .firstOrNull()?.remoteUrl?.value
                    Spacer(Modifier.height(20.dp))
                    MomentDetailAttachments(
                        attachments = state.editAttachments,
                        audioPlaybackState = state.audioPlaybackState,
                        audioCurrentPosition = state.audioPositionFormatted,
                        audioDuration = state.audioDurationFormatted,
                        audioAmplitudes = state.audioAmplitudes,
                        onAudioPlayClick = {
                            audioUrl?.let { onAction(MomentDetailAction.OnAudioPlayClick(it)) }
                        },
                        onAudioPauseClick = { onAction(MomentDetailAction.OnAudioPauseClick) },
                        formatter = formatter,
                        onRemove = { onAction(MomentDetailAction.OnAttachmentRemove(it.id)) }
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 18.dp, top = 8.dp)
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0x57141420))
                .clickable { onAction(MomentDetailAction.OnNavigateBack) }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x99141420))
                .padding(horizontal = 13.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
            Text(
                text = "Editing",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

    if (showDatePicker) {
        val localDate = createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val initialDateMillis =
            LocalDateTime(localDate.year, localDate.month, localDate.day, 0, 0, 0, 0)
                .toInstant(TimeZone.UTC)
                .toEpochMilliseconds()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pendingDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                    if (pendingDateMillis != null) showTimePicker = true
                }) { Text("Next") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val localStart = createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
        DetailTimePickerDialog(
            initialHour = localStart.hour,
            initialMinute = localStart.minute,
            onConfirm = { hour, minute ->
                pendingDateMillis?.let { millis ->
                    val tz = TimeZone.currentSystemDefault()
                    val date =
                        Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date
                    val combined =
                        LocalDateTime(date.year, date.month, date.day, hour, minute, 0, 0)
                    onAction(MomentDetailAction.OnDateChange(combined.toInstant(tz)))
                }
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Preview
@Composable
private fun MomentDetailEditViewPreview() {
    AppTheme(darkTheme = true) {
        MomentDetailEditView(
            state = MomentDetailState(
                isEditing = true,
                editTitle = "Morning pages",
                editBody = "Woke up before the alarm and watched the fog roll off the hills.",
                editMoods = persistentSetOf(Mood.CALM, Mood.GRATEFUL),
                editLocation = LocationData(0.0, 0.0, "Mountain View, California"),
                editCreatedAt = Clock.System.now()
            ),
            moment = Moment(
                id = "1",
                title = "Morning pages",
                body = "Woke up before the alarm.",
                createdAt = Clock.System.now()
            ),
            formatter = object : MomentFormatter {
                override fun formatTime(instant: Instant): String = "Saturday, June 27 · 9:41 AM"
                override fun formatDuration(ms: Long): String = "0:42"
            },
            onAction = {}
        )
    }
}
