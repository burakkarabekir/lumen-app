package com.bksd.moment.presentation.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.presentation.attachment.LinkAttachmentCard
import com.bksd.core.presentation.attachment.PhotoAttachmentCard
import com.bksd.core.presentation.attachment.VideoAttachmentCard
import com.bksd.core.presentation.attachment.VoiceAttachmentCard
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
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.action_enable_location
import com.bksd.moment.presentation.action_open_settings
import com.bksd.moment.presentation.body_placeholder
import com.bksd.moment.presentation.create.components.AttachmentSummaryBar
import com.bksd.moment.presentation.create.components.EntryMetaRow
import com.bksd.moment.presentation.create.components.EntryToolbar
import com.bksd.moment.presentation.create.components.LinkEntryBottomSheet
import com.bksd.moment.presentation.create.components.MoodSection
import com.bksd.moment.presentation.create.components.NewEntryTopBar
import com.bksd.moment.presentation.create.components.VoiceRecordingBottomSheet
import com.bksd.moment.presentation.permission_camera_denied
import com.bksd.moment.presentation.permission_location_denied
import com.bksd.moment.presentation.permission_location_services_required
import com.bksd.moment.presentation.permission_mic_denied
import com.bksd.moment.presentation.title_placeholder
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

@Composable
private fun CreateMomentScreen(
    state: CreateMomentState,
    onAction: (CreateMomentAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scrollState = rememberScrollState()
    val palette = rememberNewEntryPalette()

    AppScaffold(snackbarHostState = snackbarHostState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.pageBg)
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                NewEntryTopBar(
                    onClose = { onAction(CreateMomentAction.OnBackClick) },
                    onSave = { onAction(CreateMomentAction.OnSaveClick) },
                    saveEnabled = (state.body.isNotBlank() || state.allAttachments.isNotEmpty()) && state.selectedMoods.isNotEmpty(),
                    isSaving = state.isSaving,
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp, bottom = 24.dp)
                ) {
                    EntryMetaRow(
                        dateHeadline = state.dateHeadline,
                        timestamp = state.timestampFormatted,
                        locationName = state.location?.displayName,
                        isFetchingLocation = state.isFetchingLocation,
                        onRemoveLocation = { onAction(CreateMomentAction.OnRemoveLocationClick) },
                    )

                    Spacer(Modifier.height(18.dp))

                    BasicTextField(
                        value = state.title,
                        onValueChange = { onAction(CreateMomentAction.OnTitleChange(it)) },
                        textStyle = TextStyle(
                            fontSize = 27.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = palette.text,
                            lineHeight = 34.sp
                        ),
                        cursorBrush = SolidColor(palette.pinFg),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (state.title.isEmpty()) {
                                Text(
                                    text = stringResource(Res.string.title_placeholder),
                                    fontSize = 27.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = palette.sub
                                )
                            }
                            innerTextField()
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    BasicTextField(
                        value = state.body,
                        onValueChange = { onAction(CreateMomentAction.OnBodyChange(it)) },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = palette.bodyText,
                            lineHeight = 26.sp
                        ),
                        cursorBrush = SolidColor(palette.pinFg),
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 150.dp),
                        decorationBox = { innerTextField ->
                            if (state.body.isEmpty()) {
                                Text(
                                    text = stringResource(Res.string.body_placeholder),
                                    fontSize = 16.sp,
                                    color = palette.sub,
                                    lineHeight = 26.sp
                                )
                            }
                            innerTextField()
                        }
                    )

                    if (state.allAttachments.isNotEmpty()) {
                        Spacer(Modifier.height(20.dp))
                        AttachmentSummaryBar(
                            attachments = state.allAttachments,
                            isExpanded = state.isAttachmentsExpanded,
                            onToggleExpand = { onAction(CreateMomentAction.OnToggleAttachments) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        AnimatedVisibility(visible = state.isAttachmentsExpanded) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.padding(top = 12.dp)
                            ) {
                                state.allAttachments.forEach { attachment ->
                                    when (attachment) {
                                        is AttachmentUiModel.Photo -> PhotoAttachmentCard(
                                            imageModel = attachment.localPath
                                                ?: attachment.remoteUrl,
                                            onRemove = {
                                                onAction(
                                                    CreateMomentAction.OnRemoveAttachment(
                                                        attachment.id
                                                    )
                                                )
                                            }
                                        )

                                        is AttachmentUiModel.Video -> VideoAttachmentCard(
                                            durationFormatted = "",
                                            onRemove = {
                                                onAction(
                                                    CreateMomentAction.OnRemoveAttachment(
                                                        attachment.id
                                                    )
                                                )
                                            }
                                        )

                                        is AttachmentUiModel.Audio -> VoiceAttachmentCard(
                                            playbackState = state.playbackState,
                                            amplitudes = state.playbackAmplitudes,
                                            positionFormatted = state.playbackPositionFormatted,
                                            durationFormatted = state.playbackDurationFormatted,
                                            onPlay = {
                                                onAction(CreateMomentAction.OnPlayAudio(attachment.id))
                                            },
                                            onPause = {
                                                onAction(CreateMomentAction.OnPauseAudio)
                                            },
                                            onRemove = {
                                                onAction(
                                                    CreateMomentAction.OnDeleteRecording(
                                                        attachment.id
                                                    )
                                                )
                                            }
                                        )

                                        is AttachmentUiModel.Link -> LinkAttachmentCard(
                                            url = attachment.remoteUrl,
                                            onRemove = {
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

                    Spacer(Modifier.height(26.dp))

                    MoodSection(
                        selectedMoods = state.selectedMoods,
                        isExpanded = state.isMoodSectionExpanded,
                        onMoodClick = { onAction(CreateMomentAction.OnMoodSelect(it)) },
                        onToggleExpand = { onAction(CreateMomentAction.OnToggleMoodSection) },
                    )
                }

                EntryToolbar(
                    onPhotoClick = { onAction(CreateMomentAction.OnPhotoClick) },
                    onCameraClick = { onAction(CreateMomentAction.OnCameraClick) },
                    onPlaceClick = { onAction(CreateMomentAction.OnAddLocationClick) },
                    onVoiceClick = { onAction(CreateMomentAction.OnMicClick) },
                    onLinkClick = { onAction(CreateMomentAction.OnLinkClick) },
                    isRecording = state.recordingState is RecordingUiState.Recording,
                )
            }

            if (state.isLinkSheetVisible) {
                LinkEntryBottomSheet(
                    linkInput = state.linkInput,
                    onInputChange = { onAction(CreateMomentAction.OnLinkInputChange(it)) },
                    onDoneClick = { onAction(CreateMomentAction.OnAddLink) },
                    onDismiss = { onAction(CreateMomentAction.OnDismissLinkSheet) }
                )
            }

            if (state.isRecordingSheetVisible) {
                VoiceRecordingBottomSheet(
                    recordingState = state.recordingState,
                    onDoneClick = { onAction(CreateMomentAction.OnRecordingDone) },
                    onCancelClick = { onAction(CreateMomentAction.OnCancelRecording) },
                    onDismiss = { onAction(CreateMomentAction.OnDismissRecordingSheet) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateMomentScreenPreview() {
    AppTheme {
        CreateMomentScreen(
            state = CreateMomentState(
                dateHeadline = "June 27",
                timestampFormatted = "Today, 9:41 AM"
            ),
            snackbarHostState = SnackbarHostState(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun CreateMomentScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        CreateMomentScreen(
            state = CreateMomentState(
                dateHeadline = "June 27",
                timestampFormatted = "Today, 9:41 AM"
            ),
            snackbarHostState = SnackbarHostState(),
            onAction = {},
        )
    }
}
