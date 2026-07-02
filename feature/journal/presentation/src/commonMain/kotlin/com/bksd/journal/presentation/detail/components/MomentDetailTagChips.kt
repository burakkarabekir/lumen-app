package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MomentDetailTagChips(
    tags: ImmutableList<String>,
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    onTagRemove: (String) -> Unit = {}
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)
    ) {
        tags.forEach { tag ->
            Surface(
                shape = RoundedCornerShape(MaterialTheme.dimens.radius.xl),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                if (isEditing) {
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = "#$tag",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.lg, top = MaterialTheme.dimens.spacing.sm, bottom = MaterialTheme.dimens.spacing.sm)
                        )
                        IconButton(
                            onClick = { onTagRemove(tag) },
                            modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
                            )
                        }
                    }
                } else {
                    Text(
                        text = "#$tag",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.sm)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MomentDetailTagChipsPreview() {
    AppTheme {
        MomentDetailTagChips(tags = persistentListOf("mindfulness", "career", "japan"))
    }
}

@Preview
@Composable
private fun MomentDetailTagChipsEditPreview() {
    AppTheme(darkTheme = true) {
        MomentDetailTagChips(
            tags = persistentListOf("mindfulness", "career"),
            isEditing = true,
            onTagRemove = {}
        )
    }
}
