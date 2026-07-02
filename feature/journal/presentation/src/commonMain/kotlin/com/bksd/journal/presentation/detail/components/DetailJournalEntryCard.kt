package com.bksd.journal.presentation.detail.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.collapse
import com.bksd.journal.presentation.expand
import com.bksd.journal.presentation.journal_entry_label
import com.bksd.journal.presentation.write_thoughts_hint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetailJournalEntryCard(
    body: String,
    tags: ImmutableList<String>,
    isExpanded: Boolean,
    isEditing: Boolean,
    onToggleExpand: () -> Unit,
    onBodyChange: (String) -> Unit,
    onTagRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(MaterialTheme.dimens.spacing.xl)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.journal_entry_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.5.sp,
                modifier = Modifier.weight(1f)
            )
            if (!isEditing && body.length > 200) {
                Text(
                    text = if (isExpanded) stringResource(Res.string.collapse) else stringResource(Res.string.expand),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onToggleExpand)
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.lg))

        if (isEditing) {
            TextField(
                value = body,
                onValueChange = onBodyChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 26.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.write_thoughts_hint),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            )
        } else {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 26.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else 6,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (tags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.lg))
            MomentDetailTagChips(
                tags = tags,
                isEditing = isEditing,
                onTagRemove = onTagRemove
            )
        }
    }
}

@Preview
@Composable
private fun DetailJournalEntryCardPreview() {
    AppTheme(darkTheme = true) {
        DetailJournalEntryCard(
            body = "Today was unexpectedly peaceful. After the chaos of the last few weeks, finding this moment of stillness felt like a gift. I realized that I've been pushing too hard without pausing to calibrate my direction.",
            tags = persistentListOf("mindfulness", "career"),
            isExpanded = false,
            isEditing = false,
            onToggleExpand = {},
            onBodyChange = {},
            onTagRemove = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}

@Preview
@Composable
private fun DetailJournalEntryCardEditPreview() {
    AppTheme(darkTheme = true) {
        DetailJournalEntryCard(
            body = "Today was unexpectedly peaceful.",
            tags = persistentListOf("mindfulness", "career"),
            isExpanded = true,
            isEditing = true,
            onToggleExpand = {},
            onBodyChange = {},
            onTagRemove = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
