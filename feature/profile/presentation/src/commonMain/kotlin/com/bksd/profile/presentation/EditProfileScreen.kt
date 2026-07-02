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
import androidx.compose.material3.MaterialTheme
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
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.presentation.media.rememberImagePickerLauncher
import com.bksd.core.presentation.util.ObserveAsEvents
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
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
                    .height(MaterialTheme.dimens.size.fab)
                    .padding(horizontal = MaterialTheme.dimens.spacing.xl)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.icon.avatar)
                        .clip(CircleShape)
                        .background(palette.surface)
                        .clickable { onBack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.action_close),
                        tint = palette.text,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                    )
                }
                Text(
                    text = stringResource(Res.string.edit_profile_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.text
                )
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(MaterialTheme.dimens.icon.lg),
                        strokeWidth = 2.dp,
                        color = accent
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.action_save),
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
                    .padding(start = MaterialTheme.dimens.spacing.xxl, end = MaterialTheme.dimens.spacing.xxl, top = MaterialTheme.dimens.spacing.lg, bottom = MaterialTheme.dimens.spacing.huge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

                AppAvatar(
                    photoUrl = state.photoUrl,
                    size = 118.dp,
                    isLoading = state.isAvatarLoading,
                    showBorder = true,
                    contentDescription = stringResource(Res.string.edit_profile_photo_content_desc),
                    onEditClick = { onAction(EditProfileAction.OnChangePhotoClick) }
                )

                Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
                Text(
                    text = stringResource(Res.string.edit_profile_change_photo),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    modifier = Modifier.clickable { onAction(EditProfileAction.OnChangePhotoClick) }
                )

                Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxxl))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(Res.string.edit_profile_name_label),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.7.sp,
                        color = palette.sub,
                        modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.xxs, bottom = MaterialTheme.dimens.spacing.sm)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                            .background(palette.surface)
                            .border(1.5.dp, accent, RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                            .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg)
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
                                        text = stringResource(Res.string.edit_profile_name_placeholder),
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
                        text = stringResource(Res.string.edit_profile_name_hint),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = palette.sub,
                        modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.xxs, top = MaterialTheme.dimens.spacing.sm)
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
