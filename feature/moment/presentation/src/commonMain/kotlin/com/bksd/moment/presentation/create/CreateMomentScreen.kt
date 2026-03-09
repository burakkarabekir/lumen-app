package com.bksd.moment.presentation.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.presentation.media.MediaPickResult
import com.bksd.core.presentation.media.rememberMediaPickerLauncher
import com.bksd.core.presentation.permission.LocationSettingsResolver
import com.bksd.core.presentation.permission.Permission
import com.bksd.core.presentation.permission.PermissionController
import com.bksd.core.presentation.permission.PermissionState
import com.bksd.core.presentation.permission.rememberLocationSettingsResolver
import com.bksd.core.presentation.permission.rememberPermissionController
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Mood
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.action_enable_location
import com.bksd.moment.presentation.action_open_settings
import com.bksd.moment.presentation.add_location
import com.bksd.moment.presentation.body_placeholder
import com.bksd.moment.presentation.content_desc_close
import com.bksd.moment.presentation.content_desc_location
import com.bksd.moment.presentation.content_desc_remove_location
import com.bksd.moment.presentation.create.components.AttachmentSummaryBar
import com.bksd.moment.presentation.create.components.AudioPlaybackCard
import com.bksd.moment.presentation.create.components.EntryActionItem
import com.bksd.moment.presentation.create.components.LinkAttachmentPreviewCard
import com.bksd.moment.presentation.create.components.LinkEntryBottomSheet
import com.bksd.moment.presentation.create.components.MediaPreviewCard
import com.bksd.moment.presentation.create.components.MoodChip
import com.bksd.moment.presentation.create.components.VoiceRecordingBottomSheet
import com.bksd.moment.presentation.create_moment_title
import com.bksd.moment.presentation.label_camera
import com.bksd.moment.presentation.label_link
import com.bksd.moment.presentation.label_mic
import com.bksd.moment.presentation.label_photo
import com.bksd.moment.presentation.label_video
import com.bksd.moment.presentation.permission_camera_denied
import com.bksd.moment.presentation.permission_location_denied
import com.bksd.moment.presentation.permission_location_services_required
import com.bksd.moment.presentation.permission_mic_denied
import com.bksd.moment.presentation.save_moment
import com.bksd.moment.presentation.section_add_to_entry
import com.bksd.moment.presentation.section_how_feeling
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateMomentRoot(
    onNavigateBack: () -> Unit,
) {
    val viewModel: CreateMomentViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val permissionController = rememberPermissionController()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val locationSettingsResolver = rememberLocationSettingsResolver()

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

                is MediaPickResult.Error -> coroutineScope.launch {
                    snackbarHostState.showSnackbar(UiText.Dynamic(result.message).asStringAsync())
                }
                MediaPickResult.Cancelled -> { /* No-op */
                }
            }
        }
    )

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CreateMomentEvent.NavigateBack -> onNavigateBack()
            is CreateMomentEvent.ShowError -> coroutineScope.launch {
                val message = event.error.asStringAsync()
                if (message.contains("disabled", ignoreCase = true)) {
                    // Device location services are off; guide user to system Location Settings
                    val result = snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = getString(Res.string.action_enable_location),
                        duration = SnackbarDuration.Long
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        permissionController.openLocationSettings()
                    }
                } else {
                    snackbarHostState.showSnackbar(message)
                }
            }

            is CreateMomentEvent.ShowSaveSuccess -> coroutineScope.launch {
                snackbarHostState.showSnackbar(event.message.asStringAsync())
            }
            CreateMomentEvent.LaunchCamera -> mediaPickerLauncher.launchCamera()
            CreateMomentEvent.LaunchPhotoPicker -> mediaPickerLauncher.launchPhotoPicker()
            CreateMomentEvent.LaunchVideoPicker -> mediaPickerLauncher.launchVideoPicker()
            CreateMomentEvent.LaunchFilePicker -> mediaPickerLauncher.launchFilePicker()
            CreateMomentEvent.RequestAudioPermission -> {
                coroutineScope.launch {
                    handlePermission(
                        permission = Permission.RECORD_AUDIO,
                        permissionController = permissionController,
                        snackbarHostState = snackbarHostState,
                        deniedMessage = getString(Res.string.permission_mic_denied),
                        onGranted = { viewModel.onAction(CreateMomentAction.OnAudioPermissionGranted) },
                        onDenied = { viewModel.onAction(CreateMomentAction.OnAudioPermissionDenied) }
                    )
                }
            }

            CreateMomentEvent.RequestCameraPermission -> {
                coroutineScope.launch {
                    handlePermission(
                        permission = Permission.CAMERA,
                        permissionController = permissionController,
                        snackbarHostState = snackbarHostState,
                        deniedMessage = getString(Res.string.permission_camera_denied),
                        onGranted = { viewModel.onAction(CreateMomentAction.OnCameraPermissionGranted) },
                        onDenied = { viewModel.onAction(CreateMomentAction.OnCameraPermissionDenied) }
                    )
                }
            }

            CreateMomentEvent.RequestLocationPermission -> {
                coroutineScope.launch {
                    handleLocationPermission(
                        permissionController = permissionController,
                        snackbarHostState = snackbarHostState,
                        locationSettingsResolver = locationSettingsResolver,
                        onGranted = { viewModel.onAction(CreateMomentAction.OnLocationPermissionGranted) },
                        onDenied = { viewModel.onAction(CreateMomentAction.OnLocationPermissionDenied) }
                    )
                }
            }
        }
    }

    CreateMomentScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )
}

private suspend fun handlePermission(
    permission: Permission,
    permissionController: PermissionController,
    snackbarHostState: SnackbarHostState,
    deniedMessage: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    val finalState = when (val permissionState = permissionController.checkPermission(permission)) {
        PermissionState.GRANTED,
        PermissionState.PERMANENTLY_DENIED -> permissionState

        else -> permissionController.requestPermission(permission)
    }

    when (finalState) {
        PermissionState.GRANTED -> onGranted()
        PermissionState.DENIED,
        PermissionState.PERMANENTLY_DENIED -> {
            onDenied()
            showPermissionSnackbar(
                snackbarHostState = snackbarHostState,
                message = deniedMessage,
                onActionClick = { permissionController.openAppSettings() }
            )
        }

        PermissionState.NOT_DETERMINED -> Unit
    }
}

private suspend fun handleLocationPermission(
    permissionController: PermissionController,
    snackbarHostState: SnackbarHostState,
    locationSettingsResolver: LocationSettingsResolver,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    val finalState =
        when (val permissionState = permissionController.checkPermission(Permission.LOCATION)) {
            PermissionState.GRANTED,
            PermissionState.PERMANENTLY_DENIED -> permissionState

            else -> permissionController.requestPermission(Permission.LOCATION)
        }

    when (finalState) {
        PermissionState.GRANTED -> {
            // Permission granted — now ensure device location services are enabled
            val servicesEnabled = locationSettingsResolver.ensureLocationEnabled()
            if (servicesEnabled) {
                onGranted()
            } else {
                // User declined the native "Turn on" dialog
                showPermissionSnackbar(
                    snackbarHostState = snackbarHostState,
                    message = getString(Res.string.permission_location_services_required),
                    onActionClick = { permissionController.openLocationSettings() }
                )
            }
        }

        PermissionState.DENIED,
        PermissionState.PERMANENTLY_DENIED -> {
            onDenied()
            showPermissionSnackbar(
                snackbarHostState = snackbarHostState,
                message = getString(Res.string.permission_location_denied),
                onActionClick = { permissionController.openAppSettings() }
            )
        }

        PermissionState.NOT_DETERMINED -> Unit
    }
}

private suspend fun showPermissionSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    onActionClick: () -> Unit
) {
    val result = snackbarHostState.showSnackbar(
        message = message,
        actionLabel = getString(Res.string.action_open_settings),
        duration = SnackbarDuration.Short
    )
    if (result == SnackbarResult.ActionPerformed) {
        onActionClick()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMomentScreen(
    state: CreateMomentState,
    onAction: (CreateMomentAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scrollState = rememberScrollState()

    AppScaffold(
        snackbarHostState = snackbarHostState
    ) {
        AppSurface(
            header = {
                AppTopBar(
                    title = stringResource(Res.string.create_moment_title),
                    style = AppBarStyle.Child,
                    navigationIcon = {
                        IconButton(onClick = { onAction(CreateMomentAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(Res.string.content_desc_close)
                            )
                        }
                    },
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
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        // Time Chip
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = state.timestampFormatted,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }

                        // Location
                        Row(
                            modifier = Modifier
                                .clickable {
                                    if (state.location == null && !state.isFetchingLocation) {
                                        onAction(CreateMomentAction.OnAddLocationClick)
                                    }
                                }
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = stringResource(Res.string.content_desc_location),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            if (state.isFetchingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Text(
                                    text = state.location?.displayName
                                        ?: stringResource(Res.string.add_location),
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }

                            if (state.location != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(Res.string.content_desc_remove_location),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable { onAction(CreateMomentAction.OnRemoveLocationClick) }
                                )
                            }
                        }
                    }

                    // Body Input
                    TextField(
                        value = state.body,
                        onValueChange = { onAction(CreateMomentAction.OnBodyChange(it)) },
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.body_placeholder),
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
                        textStyle = TextStyle(
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
                        text = stringResource(Res.string.section_add_to_entry),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        EntryActionItem(
                            icon = Icons.Default.Mic,
                            label = stringResource(Res.string.label_mic),
                            onClick = { onAction(CreateMomentAction.OnMicClick) },
                            isActive = state.recordingState is RecordingUiState.Recording
                        )
                        EntryActionItem(
                            icon = Icons.Default.CameraAlt,
                            label = stringResource(Res.string.label_camera),
                            onClick = { onAction(CreateMomentAction.OnCameraClick) }
                        )
                        EntryActionItem(
                            icon = Icons.Default.Image,
                            label = stringResource(Res.string.label_photo),
                            onClick = { onAction(CreateMomentAction.OnPhotoClick) }
                        )
                        EntryActionItem(
                            icon = Icons.Default.Videocam,
                            label = stringResource(Res.string.label_video),
                            onClick = { onAction(CreateMomentAction.OnVideoClick) }
                        )
                        EntryActionItem(
                            icon = Icons.Default.Link,
                            label = stringResource(Res.string.label_link),
                            onClick = { onAction(CreateMomentAction.OnLinkClick) }
                        )
                    }

                    if (state.allAttachments.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AttachmentSummaryBar(
                            attachments = state.allAttachments,
                            isExpanded = state.isAttachmentsExpanded,
                            onToggleExpand = { onAction(CreateMomentAction.OnToggleAttachments) },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        AnimatedVisibility(visible = state.isAttachmentsExpanded) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                state.allAttachments.forEach { attachment ->
                                    when (attachment) {
                                        is AttachmentUiModel.Photo, is AttachmentUiModel.Video -> {
                                            MediaPreviewCard(
                                                type = attachment.type,
                                                onRemoveClick = {
                                                    onAction(
                                                        CreateMomentAction.OnRemoveAttachment(
                                                            attachment.id
                                                        )
                                                    )
                                                }
                                            )
                                        }

                                        is AttachmentUiModel.Audio -> {
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

                                        is AttachmentUiModel.Link -> {
                                            LinkAttachmentPreviewCard(
                                                url = attachment.remoteUrl,
                                                onRemoveClick = {
                                                    onAction(
                                                        CreateMomentAction.OnRemoveAttachment(
                                                            attachment.id
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Mood Selection
                    Text(
                        text = stringResource(Res.string.section_how_feeling),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
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

                // Sticky Bottom Save Button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp)
                ) {
                    AppButton(
                        onClick = { onAction(CreateMomentAction.OnSaveClick) },
                        enabled = state.body.isNotEmpty() && state.body.isNotBlank() && state.selectedMood != null,
                        style = AppButtonStyle.PRIMARY,
                        isLoading = state.isSaving,
                        text = stringResource(Res.string.save_moment),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } // End Box
        } // End AppSurface
    }
    }

@Preview
@Composable
fun CreateMomentScreenPreview() {
    CreateMomentScreen(
        state = CreateMomentState(),
        snackbarHostState = SnackbarHostState(),
        onAction = {},
    )
}


@Preview
@Composable
fun CreateMomentScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        CreateMomentScreen(
            state = CreateMomentState(),
            snackbarHostState = SnackbarHostState(),
            onAction = {},
        )
    }
}