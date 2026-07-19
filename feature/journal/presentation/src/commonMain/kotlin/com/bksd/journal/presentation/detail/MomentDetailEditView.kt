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
import androidx.compose.material3.MaterialTheme
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
import com.bksd.core.design_system.theme.coverGradient
import com.bksd.core.design_system.theme.detailEditingBadgeBg
import com.bksd.core.design_system.theme.detailLocationChipRemoveBg
import com.bksd.core.design_system.theme.detailToolbarButtonScrim
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.momentDetailCoverScrim
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.presentation.shortDateLabel
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.journal.presentation.model.MomentUi
import com.bksd.core.domain.model.Mood
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.add_mood
import com.bksd.journal.presentation.cancel
import com.bksd.journal.presentation.detail.components.DetailTimePickerDialog
import com.bksd.journal.presentation.detail.components.EditSectionLabel
import com.bksd.journal.presentation.detail.components.MomentDetailAttachments
import com.bksd.journal.presentation.detail.components.MomentDetailEditMoodChip
import com.bksd.journal.presentation.detail.components.MomentDetailMoodChip
import com.bksd.journal.presentation.edit_mood_done
import com.bksd.journal.presentation.edit_section_date_time
import com.bksd.journal.presentation.edit_section_entry
import com.bksd.journal.presentation.edit_section_moods
import com.bksd.journal.presentation.edit_section_title
import com.bksd.journal.presentation.edit_title_hint
import com.bksd.journal.presentation.editing_badge
import com.bksd.journal.presentation.next
import com.bksd.journal.presentation.util.MomentFormatter
import com.bksd.journal.presentation.write_thoughts_hint
import org.jetbrains.compose.resources.stringResource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun MomentDetailEditView(
    state: MomentDetailState,
    moment: MomentUi,
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
                    .background(Brush.linearGradient(MaterialTheme.colorScheme.extended.coverGradient))
            ) {
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.extended.momentDetailCoverScrim))
                val locationName = state.editLocation?.displayName
                if (!locationName.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = MaterialTheme.dimens.spacing.xl, bottom = MaterialTheme.dimens.spacing.lg)
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                            .background(Color.White.copy(alpha = 0.22f))
                            .padding(start = MaterialTheme.dimens.spacing.md, end = MaterialTheme.dimens.spacing.sm, top = MaterialTheme.dimens.spacing.sm, bottom = MaterialTheme.dimens.spacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
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
                                .size(MaterialTheme.dimens.icon.md)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.extended.detailLocationChipRemoveBg)
                                .clickable { onAction(MomentDetailAction.OnLocationRemove) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.spacing.xl)
                    .padding(top = MaterialTheme.dimens.spacing.xxl, bottom = 150.dp)
            ) {
                EditSectionLabel(stringResource(Res.string.edit_section_date_time), palette.sub)
                val localCreated = createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
                val dateTimeLabel = "${shortDateLabel(localCreated.date)} · ${formatter.formatTime(createdAt)}"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .background(palette.surface)
                        .border(1.dp, palette.hairline, RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .clickable { showDatePicker = true }
                        .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.md)
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
                        modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                    )
                }

                Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
                EditSectionLabel(stringResource(Res.string.edit_section_title), palette.sub)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .background(palette.surface)
                        .border(1.dp, palette.hairline, RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
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
                                    text = stringResource(Res.string.edit_title_hint),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = palette.sub
                                )
                            }
                            inner()
                        }
                    )
                }

                Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
                EditSectionLabel(stringResource(Res.string.edit_section_moods), palette.sub)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
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
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                        modifier = Modifier
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                            .border(
                                1.5.dp,
                                if (moodPickerOpen) accent else palette.hairline,
                                RoundedCornerShape(MaterialTheme.dimens.radius.lg)
                            )
                            .clickable { showMoodPicker = !showMoodPicker }
                            .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.sm)
                    ) {
                        Icon(
                            imageVector = if (moodPickerOpen) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            tint = if (moodPickerOpen) accent else palette.sub,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                        )
                        Text(
                            text = if (moodPickerOpen) stringResource(Res.string.edit_mood_done) else stringResource(Res.string.add_mood),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (moodPickerOpen) accent else palette.sub
                        )
                    }
                }
                if (showMoodPicker) {
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
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

                Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
                EditSectionLabel(stringResource(Res.string.edit_section_entry), palette.sub)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .background(palette.surface)
                        .border(1.5.dp, accent, RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .padding(MaterialTheme.dimens.spacing.lg)
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
                                    text = stringResource(Res.string.write_thoughts_hint),
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
                    Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
                    MomentDetailAttachments(
                        attachments = state.editAttachments,
                        audioPlaybackState = state.audioPlaybackState,
                        audioDuration = state.audioDurationFormatted,
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
                .padding(start = MaterialTheme.dimens.spacing.xl, top = MaterialTheme.dimens.spacing.sm)
                .size(MaterialTheme.dimens.icon.tile)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.extended.detailToolbarButtonScrim)
                .clickable { onAction(MomentDetailAction.OnNavigateBack) }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.md)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = MaterialTheme.dimens.spacing.md)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                .background(MaterialTheme.colorScheme.extended.detailEditingBadgeBg)
                .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.sm)
        ) {
            Box(
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.xs)
                    .clip(CircleShape)
                    .background(accent)
            )
            Text(
                text = stringResource(Res.string.editing_badge),
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
            modifier = Modifier.padding(16.dp),
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pendingDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                    if (pendingDateMillis != null) showTimePicker = true
                }) { Text(stringResource(Res.string.next)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(Res.string.cancel)) }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.spacing.sm)
            )
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
            moment = MomentUi(
                id = "1",
                title = "Morning pages",
                body = "Woke up before the alarm.",
                createdAt = Clock.System.now(),
                moods = persistentListOf(),
                tags = persistentListOf(),
                attachments = persistentListOf(),
                location = null,
                isFavorite = false,
            ),
            formatter = object : MomentFormatter {
                override fun formatTime(instant: Instant): String = "Saturday, June 27 · 9:41 AM"
                override fun formatDuration(ms: Long): String = "0:42"
            },
            onAction = {}
        )
    }
}
