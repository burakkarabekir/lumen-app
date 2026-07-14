package com.bksd.journal.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.favorite
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.edit
import com.bksd.journal.presentation.save_changes
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetailBottomActionBar(
    isEditing: Boolean,
    isSaving: Boolean,
    isFavorite: Boolean,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg

    val pageIsDarker = palette.pageBg.luminance() <= palette.surface.luminance()
    val trayColor = palette.surface
    val sideColor = if (pageIsDarker) lerp(palette.pageBg, Color.White, 0.08f) else palette.pageBg

    val animatedTrayColor by animateColorAsState(
        targetValue = if (isEditing) Color.Transparent else trayColor,
        animationSpec = tween(260),
        label = "tray_bg"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to Color.Transparent,
                    0.5f to palette.pageBg
                )
            )
            .navigationBarsPadding()
            .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xxl))
                .background(animatedTrayColor)
                .padding(MaterialTheme.dimens.spacing.sm)
        ) {
            AnimatedVisibility(
                visible = !isEditing,
                enter = fadeIn(tween(200)) + expandHorizontally(
                    tween(260),
                    expandFrom = Alignment.Start
                ),
                exit = fadeOut(tween(160)) + shrinkHorizontally(
                    tween(260),
                    shrinkTowards = Alignment.Start
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DetailActionButton(
                        icon = Icons.Default.Delete,
                        tint = palette.sub,
                        background = sideColor,
                        onClick = onDeleteClick
                    )
                    Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(MaterialTheme.dimens.size.fab)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
                    .background(accent)
                    .clickable(enabled = !isSaving) {
                        if (isEditing) onSaveClick() else onEditClick()
                    }
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(MaterialTheme.dimens.icon.xl),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    AnimatedContent(
                        targetState = isEditing,
                        transitionSpec = {
                            (fadeIn(tween(220)) + slideInVertically { it / 2 }) togetherWith
                                    (fadeOut(tween(160)) + slideOutVertically { -it / 2 })
                        },
                        label = "edit_save_swap"
                    ) { editing ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)
                        ) {
                            Icon(
                                imageVector = if (editing) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(MaterialTheme.dimens.icon.md)
                            )
                            Text(
                                text = stringResource(
                                    if (editing) Res.string.save_changes else Res.string.edit
                                ),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !isEditing,
                enter = fadeIn(tween(200)) + expandHorizontally(
                    tween(260),
                    expandFrom = Alignment.End
                ),
                exit = fadeOut(tween(160)) + shrinkHorizontally(
                    tween(260),
                    shrinkTowards = Alignment.End
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
                    DetailActionButton(
                        icon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        tint = if (isFavorite) MaterialTheme.colorScheme.extended.favorite else palette.sub,
                        background = sideColor,
                        onClick = onFavoriteClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailBottomActionBarReadLightPreview() {
    AppTheme(darkTheme = false) {
        DetailBottomActionBar(
            isEditing = false,
            isSaving = false,
            isFavorite = false,
            onDeleteClick = {},
            onEditClick = {},
            onSaveClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview
@Composable
private fun DetailBottomActionBarReadDarkPreview() {
    AppTheme(darkTheme = true) {
        DetailBottomActionBar(
            isEditing = false,
            isSaving = false,
            isFavorite = true,
            onDeleteClick = {},
            onEditClick = {},
            onSaveClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview
@Composable
private fun DetailBottomActionBarEditPreview() {
    AppTheme(darkTheme = true) {
        DetailBottomActionBar(
            isEditing = true,
            isSaving = false,
            isFavorite = false,
            onDeleteClick = {},
            onEditClick = {},
            onSaveClick = {},
            onFavoriteClick = {}
        )
    }
}
