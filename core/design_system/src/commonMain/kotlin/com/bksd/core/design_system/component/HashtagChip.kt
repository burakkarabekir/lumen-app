package com.bksd.core.design_system.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.Res
import com.bksd.core.design_system.tag_prefix
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun HashtagChip(
    text: String,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs)
        ) {
            Text(
                text = stringResource(Res.string.tag_prefix),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            trailingIcon?.invoke()
        }
    }
}

@Preview
@Composable
private fun HashtagChipWithIconPreview() {
    AppTheme {
        HashtagChip(
            text = "travel",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove tag",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
                )
            }
        )
    }
}

@Preview
@Composable
private fun HashtagChipPreview() {
    AppTheme {
        HashtagChip(text = "past_tense")
    }
}

@Preview
@Composable
private fun HashtagChipDarkPreview() {
    AppTheme(darkTheme = true) {
        HashtagChip(text = "adjectives")
    }
}