package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.add_favorite
import com.bksd.journal.presentation.delete
import com.bksd.journal.presentation.edit_entry
import com.bksd.journal.presentation.remove_favorite
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentActionsSheet(
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isLiked: Boolean = false,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.extraLarge,
        dragHandle = {
            Surface(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.spacing.md)
                    .width(MaterialTheme.dimens.icon.avatar)
                    .height(MaterialTheme.dimens.spacing.xs),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            ) {}
        },
        modifier = modifier
    ) {
        MomentActionsSheetContent(
            isLiked = isLiked,
            onEditClick = {
                onEditClick()
                onDismiss()
            },
            onFavoriteClick = {
                onFavoriteClick()
                onDismiss()
            },
            onDeleteClick = {
                onDeleteClick()
                onDismiss()
            }
        )
    }
}

@Composable
private fun MomentActionsSheetContent(
    isLiked: Boolean,
    onEditClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.sm))

    SheetActionRow(
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
            )
        },
        label = stringResource(Res.string.edit_entry),
        labelColor = MaterialTheme.colorScheme.onSurface,
        onClick = onEditClick
    )

    SheetActionRow(
        icon = {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
            )
        },
        label = when (isLiked) {
            true -> stringResource(Res.string.remove_favorite)
            else -> stringResource(Res.string.add_favorite)
        },
        labelColor = MaterialTheme.colorScheme.onSurface,
        onClick = onFavoriteClick
    )

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.spacing.xxl, vertical = MaterialTheme.dimens.spacing.xs),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    )

    SheetActionRow(
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
            )
        },
        label = stringResource(Res.string.delete),
        labelColor = MaterialTheme.colorScheme.error,
        onClick = onDeleteClick
    )

    Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.sm).navigationBarsPadding())
}

@Composable
private fun SheetActionRow(
    icon: @Composable () -> Unit,
    label: String,
    labelColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.spacing.xxl, vertical = MaterialTheme.dimens.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            icon()
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.lg))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = labelColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MomentActionsSheetContentPreview() {
    AppTheme {
        MomentActionsSheetContent(
            isLiked = false,
            onEditClick = {},
            onFavoriteClick = {},
            onDeleteClick = {}
        )
    }
}
