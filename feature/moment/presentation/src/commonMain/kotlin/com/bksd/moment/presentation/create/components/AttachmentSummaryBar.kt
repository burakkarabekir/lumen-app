package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.LumenTheme
import com.bksd.core.design_system.theme.labelAttachmentBar
import com.bksd.core.design_system.theme.labelXSmall
import com.bksd.core.domain.model.MediaType
import com.bksd.moment.presentation.create.AttachmentUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AttachmentSummaryBar(
    attachments: ImmutableList<AttachmentUiModel>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalItems = attachments.size
    if (totalItems == 0) return

    val typesText = attachments.joinToString(", ") {
        when (it) {
            is AttachmentUiModel.Photo -> "1 Photo"
            is AttachmentUiModel.Audio -> "1 Audio"
            is AttachmentUiModel.Video -> "1 Video"
            is AttachmentUiModel.Link -> "1 Link"
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onToggleExpand() }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Attachment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "$totalItems Attachment${if (totalItems > 1) "s" else ""}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = typesText,
                style = MaterialTheme.typography.labelAttachmentBar,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Toggle Attachments",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttachmentSummaryBarPreview() {
    LumenTheme {
        AttachmentSummaryBar(
            attachments = persistentListOf(
                AttachmentUiModel.Photo(id = "1"),
                AttachmentUiModel.Video(id = "2"),
                AttachmentUiModel.Audio(id = "3"),
            ),
            isExpanded = false,
            onToggleExpand = {}
        )
    }
}
@Preview(showBackground = true)
@Composable
fun AttachmentSummaryBarExpandedPreview() {
    LumenTheme(darkTheme = true) {
        AttachmentSummaryBar(
            attachments = persistentListOf(
                AttachmentUiModel.Photo(id = "1"),
                AttachmentUiModel.Video(id = "2"),
                AttachmentUiModel.Audio(id = "3"),
            ),
            isExpanded = true,
            onToggleExpand = {}
        )
    }
}