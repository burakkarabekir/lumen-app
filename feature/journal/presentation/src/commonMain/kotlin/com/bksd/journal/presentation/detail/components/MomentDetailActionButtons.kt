package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.add_reflection
import com.bksd.journal.presentation.content_desc_bookmark
import com.bksd.journal.presentation.export_to_pdf
import org.jetbrains.compose.resources.stringResource

@Composable
fun MomentDetailActionButtons(
    onAddReflectionClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onExportPdfClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)
        ) {
            OutlinedButton(
                onClick = onAddReflectionClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(MaterialTheme.dimens.radius.md),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NoteAdd,
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.md)
                )
                Spacer(modifier = Modifier.size(MaterialTheme.dimens.spacing.sm))
                Text(text = stringResource(Res.string.add_reflection))
            }

            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = stringResource(Res.string.content_desc_bookmark)
                )
            }
        }

        Button(
            onClick = onExportPdfClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.size.cancelIcon),
            shape = RoundedCornerShape(MaterialTheme.dimens.radius.xxl),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimens.icon.md)
            )
            Spacer(modifier = Modifier.size(MaterialTheme.dimens.spacing.sm))
            Text(text = stringResource(Res.string.export_to_pdf))
        }
    }
}

@Preview
@Composable
private fun MomentDetailActionButtonsPreview() {
    AppTheme {
        MomentDetailActionButtons(
            onAddReflectionClick = {},
            onBookmarkClick = {},
            onExportPdfClick = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
