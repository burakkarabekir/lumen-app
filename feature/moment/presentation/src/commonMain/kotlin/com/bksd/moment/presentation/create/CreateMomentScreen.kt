package com.bksd.moment.presentation.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.domain.model.MediaType
import com.bksd.core.presentation.media.MediaPickResult
import com.bksd.core.presentation.media.rememberMediaPickerLauncher
import com.bksd.core.presentation.permission.Permission
import com.bksd.core.presentation.permission.rememberPermissionController
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Mood
import com.bksd.moment.presentation.create.components.AttachmentPreviewCard
import com.bksd.moment.presentation.create.components.AttachmentSummaryBar
import com.bksd.moment.presentation.create.components.AudioPlaybackCard
import com.bksd.moment.presentation.create.components.LinkEntryBottomSheet
import com.bksd.moment.presentation.create.components.VoiceRecordingBottomSheet
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMomentRoot(
    onNavigateBack: () -> Unit,
    onShowSnackbar: (UiText) -> Unit
) {
    val viewModel: CreateMomentViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val permissionController = rememberPermissionController()
    val coroutineScope = rememberCoroutineScope()

    val mediaPickerLauncher = rememberMediaPickerLauncher(
        onResult = { result ->
            when (result) {
                is MediaPickResult.Success -> {
                    viewModel.onAction(
                        CreateMomentAction.OnMediaPicked(
                            result.filePath,
                            result.type,
                            result.sizeBytes
                        )
                    )
                }

                is MediaPickResult.Error -> onShowSnackbar(UiText.Dynamic(result.message))
                MediaPickResult.Cancelled -> { /* No-op */
                }
            }
        }
    )

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CreateMomentEvent.NavigateBack -> onNavigateBack()
            is CreateMomentEvent.ShowError -> onShowSnackbar(event.error)
            is CreateMomentEvent.ShowSaveSuccess -> onShowSnackbar(event.message)
            CreateMomentEvent.LaunchCamera -> mediaPickerLauncher.launchCamera()
            CreateMomentEvent.LaunchPhotoPicker -> mediaPickerLauncher.launchPhotoPicker()
            CreateMomentEvent.LaunchVideoPicker -> mediaPickerLauncher.launchVideoPicker()
            CreateMomentEvent.LaunchFilePicker -> mediaPickerLauncher.launchFilePicker()
            CreateMomentEvent.RequestAudioPermission -> {
                coroutineScope.launch {
                    val permState = permissionController.requestPermission(Permission.RECORD_AUDIO)
                    if (permState.isGranted) {
                        viewModel.onAction(CreateMomentAction.OnAudioPermissionGranted)
                    } else {
                        viewModel.onAction(CreateMomentAction.OnAudioPermissionDenied)
                    }
                }
            }
        }
    }

    CreateMomentScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMomentScreen(
    state: CreateMomentState,
    onAction: (CreateMomentAction) -> Unit
) {
    val scrollState = rememberScrollState()

    AppSurface(
        header = {
            AppTopBar(
                title = "Create Moment",
                style = AppBarStyle.Root,
                titleContent = {
                    IconButton(onClick = { onAction(CreateMomentAction.OnBackClick) }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                    Text(
                        text = "Create Moment",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 100.dp) // Bottom padding for sticky button
            ) {
                // Context Line (Time & Location)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text(
                        text = state.timestampFormatted,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (state.location != null) {
                        Text(
                            text = " • ",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = state.location,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Body Input
                TextField(
                    value = state.body,
                    onValueChange = { onAction(CreateMomentAction.OnBodyChange(it)) },
                    placeholder = {
                        Text(
                            text = "Reflect on your day... What's on your mind?",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 28.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .defaultMinSize(minHeight = 200.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Link Entry Bottom Sheet
                if (state.isLinkSheetVisible) {
                    LinkEntryBottomSheet(
                        linkInput = state.linkInput,
                        onInputChange = { onAction(CreateMomentAction.OnLinkInputChange(it)) },
                        onDoneClick = { onAction(CreateMomentAction.OnAddLink) },
                        onDismiss = { onAction(CreateMomentAction.OnDismissLinkSheet) }
                    )
                }

                // Voice Recording Bottom Sheet
                if (state.isRecordingSheetVisible) {
                    VoiceRecordingBottomSheet(
                        recordingState = state.recordingState,
                        onDoneClick = { onAction(CreateMomentAction.OnRecordingDone) },
                        onCancelClick = { onAction(CreateMomentAction.OnCancelRecording) },
                        onDismiss = { onAction(CreateMomentAction.OnDismissRecordingSheet) }
                    )
                }

                // ADD TO ENTRY
                Text(
                    text = "ADD TO ENTRY",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    EntryActionItem(
                        icon = Icons.Default.Mic,
                        label = "Mic",
                        onClick = { onAction(CreateMomentAction.OnMicClick) },
                        isActive = state.recordingState is RecordingUiState.Recording
                    )
                    EntryActionItem(
                        icon = Icons.Default.CameraAlt,
                        label = "Photo",
                        onClick = { onAction(CreateMomentAction.OnCameraClick) })
                    EntryActionItem(
                        icon = Icons.Default.Videocam,
                        label = "Video",
                        onClick = { onAction(CreateMomentAction.OnVideoClick) })
                    EntryActionItem(
                        icon = Icons.Default.Link,
                        label = "Link",
                        onClick = { onAction(CreateMomentAction.OnLinkClick) })
                }

                // Attachments Area (media + links) — below action buttons, expands downward
                if (state.attachments.isNotEmpty() || state.links.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    AttachmentSummaryBar(
                        attachments = state.attachments,
                        linksCount = state.links.size,
                        isExpanded = state.isAttachmentsExpanded,
                        onToggleExpand = { onAction(CreateMomentAction.OnToggleAttachments) },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    AnimatedVisibility(visible = state.isAttachmentsExpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            state.attachments.forEach { attachment ->
                                when (attachment.type) {
                                    MediaType.PHOTO, MediaType.VIDEO -> {
                                        AttachmentPreviewCard(
                                            onRemoveClick = {
                                                onAction(
                                                    CreateMomentAction.OnRemoveAttachment(
                                                        attachment.id
                                                    )
                                                )
                                            }
                                        )
                                    }

                                    MediaType.AUDIO -> {
                                        AudioPlaybackCard(
                                            playbackState = state.playbackState,
                                            amplitudes = state.playbackAmplitudes,
                                            currentPositionFormatted = state.playbackPositionFormatted,
                                            durationFormatted = state.playbackDurationFormatted,
                                            onPlayClick = {
                                                onAction(
                                                    CreateMomentAction.OnPlayAudio(
                                                        attachment.id
                                                    )
                                                )
                                            },
                                            onPauseClick = {
                                                onAction(CreateMomentAction.OnPauseAudio)
                                            },
                                            onDeleteClick = {
                                                onAction(
                                                    CreateMomentAction.OnDeleteRecording(
                                                        attachment.id
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                            // Links in accordion
                            state.links.forEach { link ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Link,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = link,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { onAction(CreateMomentAction.OnRemoveLink(link)) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove Link",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Mood Selection
                Text(
                    text = "HOW ARE YOU FEELING?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Mood.entries.forEach { mood ->
                        MoodChip(
                            mood = mood,
                            isSelected = state.selectedMood == mood,
                            onClick = { onAction(CreateMomentAction.OnMoodSelect(mood)) }
                        )
                    }
                } // End FlowRow
            } // End scrollable column content
        } // End AppSurface
    }
}

@Composable
fun EntryActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isActive: Boolean = false
) {
    val containerColor =
        if (isActive) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
    val iconColor =
        if (isActive) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MoodChip(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = mood.emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = mood.label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}


