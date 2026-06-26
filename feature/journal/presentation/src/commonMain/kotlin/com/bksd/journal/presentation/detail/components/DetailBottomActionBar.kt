package com.bksd.journal.presentation.detail.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.edit
import com.bksd.journal.presentation.save_changes
import org.jetbrains.compose.resources.stringResource

private val BarShape = RoundedCornerShape(32.dp)
private const val EXPAND_DURATION = 300

@Composable
fun DetailBottomActionBar(
    isEditing: Boolean,
    isSaving: Boolean,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier
) {
    val sideSlotWidth by animateDpAsState(
        targetValue = if (isEditing) 0.dp else 48.dp,
        animationSpec = tween(EXPAND_DURATION),
        label = "side_slot_width"
    )
    val dividerWidth by animateDpAsState(
        targetValue = if (isEditing) 0.dp else 1.dp,
        animationSpec = tween(EXPAND_DURATION),
        label = "divider_width"
    )
    val sideAlpha by animateDpAsState(
        targetValue = if (isEditing) 0.dp else 1.dp,
        animationSpec = tween(EXPAND_DURATION / 2),
        label = "side_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 64.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(BarShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.12f),
                    shape = BarShape
                )
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left slot: Delete icon
            Box(
                modifier = Modifier
                    .width(sideSlotWidth)
                    .graphicsLayer { alpha = sideAlpha.value },
                contentAlignment = Alignment.Center
            ) {
                if (sideSlotWidth > 0.dp) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Left divider
            Box(
                modifier = Modifier
                    .width(dividerWidth)
                    .height(24.dp)
                    .graphicsLayer { alpha = sideAlpha.value }
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
            )

            AppButton(
                text = when {
                    isEditing -> stringResource(Res.string.save_changes)
                    else -> stringResource(Res.string.edit)
                },
                onClick = if (isEditing) onSaveClick else onEditClick,
                enabled = !isSaving,
                isLoading = isSaving,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                style = AppButtonStyle.PRIMARY,
                cornerRadius = 24.dp,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )

            // Right divider
            Box(
                modifier = Modifier
                    .width(dividerWidth)
                    .height(24.dp)
                    .graphicsLayer { alpha = sideAlpha.value }
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
            )

            // Right slot: Favorite icon
            Box(
                modifier = Modifier
                    .width(sideSlotWidth)
                    .graphicsLayer { alpha = sideAlpha.value },
                contentAlignment = Alignment.Center
            ) {
                if (sideSlotWidth > 0.dp) {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailBottomActionBarPreview() {
    AppTheme(darkTheme = true) {
        DetailBottomActionBar(
            isEditing = false,
            isSaving = false,
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
            onDeleteClick = {},
            onEditClick = {},
            onSaveClick = {},
            onFavoriteClick = {}
        )
    }
}
