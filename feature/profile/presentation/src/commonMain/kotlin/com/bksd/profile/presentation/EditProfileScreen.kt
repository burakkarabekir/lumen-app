package com.bksd.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.AppAvatar
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.presentation.media.rememberImagePickerLauncher
import com.bksd.core.presentation.util.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileRoot(
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<EditProfileViewModel>()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePickerLauncher = rememberImagePickerLauncher { picked ->
        viewModel.onAction(EditProfileAction.OnPictureSelected(picked.bytes, picked.mimeType))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EditProfileEvent.NavigateBack -> onBack()
            EditProfileEvent.OpenPhotoPicker -> imagePickerLauncher.launch()
            is EditProfileEvent.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(event.error.asStringAsync())
            }
        }
    }

    EditProfileScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun EditProfileScreen(
    state: EditProfileState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onAction: (EditProfileAction) -> Unit
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg

    AppScaffold(snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.pageBg)
                .statusBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(palette.surface)
                        .clickable { onBack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = palette.text,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Text(
                    text = "Edit Profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.text
                )
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = accent
                    )
                } else {
                    Text(
                        text = "Save",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accent,
                        modifier = Modifier.clickable { onAction(EditProfileAction.OnSaveClick) }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(18.dp))

                AppAvatar(
                    photoUrl = state.photoUrl,
                    size = 118.dp,
                    isLoading = state.isAvatarLoading,
                    showBorder = true,
                    contentDescription = "Profile photo",
                    onEditClick = { onAction(EditProfileAction.OnChangePhotoClick) }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Change Photo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    modifier = Modifier.clickable { onAction(EditProfileAction.OnChangePhotoClick) }
                )

                Spacer(Modifier.height(34.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "NAME",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.7.sp,
                        color = palette.sub,
                        modifier = Modifier.padding(start = 2.dp, bottom = 9.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .background(palette.surface)
                            .border(1.5.dp, accent, RoundedCornerShape(15.dp))
                            .padding(horizontal = 16.dp, vertical = 15.dp)
                    ) {
                        BasicTextField(
                            value = state.name,
                            onValueChange = { onAction(EditProfileAction.OnNameChange(it)) },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.text
                            ),
                            cursorBrush = SolidColor(accent),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { inner ->
                                if (state.name.isEmpty()) {
                                    Text(
                                        text = "Your name",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = palette.sub
                                    )
                                }
                                inner()
                            }
                        )
                    }
                    Text(
                        text = "This is the name shown on your journal and profile.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = palette.sub,
                        modifier = Modifier.padding(start = 2.dp, top = 9.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    AppTheme(darkTheme = true) {
        EditProfileScreen(
            state = EditProfileState(
                name = "Burak Yılmaz",
                photoUrl = null,
                isLoading = false
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBack = {},
            onAction = {}
        )
    }
}
