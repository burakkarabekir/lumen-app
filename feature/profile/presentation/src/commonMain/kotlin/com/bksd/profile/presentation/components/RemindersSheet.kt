@file:OptIn(ExperimentalMaterial3Api::class)

package com.bksd.profile.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.presentation.permission.Permission
import com.bksd.core.presentation.permission.PermissionState
import com.bksd.core.presentation.permission.rememberPermissionController
import com.bksd.profile.presentation.RemindersAction
import com.bksd.profile.presentation.RemindersViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RemindersSheet(
    onDismiss: () -> Unit
) {
    val viewModel = koinViewModel<RemindersViewModel>()
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val palette = rememberNewEntryPalette()
    val permissionController = rememberPermissionController()
    val scope = rememberCoroutineScope()
    var showTimePicker by remember { mutableStateOf(false) }

    fun toggleWithPermission(enabled: Boolean, apply: (Boolean) -> Unit) {
        if (!enabled) {
            apply(false)
            return
        }
        scope.launch {
            val current = permissionController.checkPermission(Permission.NOTIFICATION)
            val finalState =
                if (current == PermissionState.GRANTED || current == PermissionState.PERMANENTLY_DENIED) {
                    current
                } else {
                    permissionController.requestPermission(Permission.NOTIFICATION)
                }
            when (finalState) {
                PermissionState.GRANTED -> apply(true)
                PermissionState.PERMANENTLY_DENIED -> permissionController.openAppSettings()
                else -> Unit
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = palette.pageBg,
        tonalElevation = 0.dp
    ) {
        RemindersSheetContent(
            state = state,
            onClose = onDismiss,
            onDailyToggle = { enabled ->
                toggleWithPermission(enabled) { viewModel.onAction(RemindersAction.OnDailyToggle(it)) }
            },
            onTimeClick = { showTimePicker = true },
            onDayToggle = { viewModel.onAction(RemindersAction.OnDayToggle(it)) },
            onStreakToggle = { enabled ->
                toggleWithPermission(enabled) { viewModel.onAction(RemindersAction.OnStreakToggle(it)) }
            }
        )
    }

    if (showTimePicker) {
        ProfileTimePickerDialog(
            initialHour = state.hour,
            initialMinute = state.minute,
            onConfirm = { hour, minute ->
                viewModel.onAction(RemindersAction.OnTimeSelected(hour, minute))
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}
