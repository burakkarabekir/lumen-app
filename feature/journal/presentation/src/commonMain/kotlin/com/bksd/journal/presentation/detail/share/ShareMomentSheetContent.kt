package com.bksd.journal.presentation.detail.share

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.ShareStyle
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.domain.model.Mood
import com.bksd.journal.presentation.model.MomentUi
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Clock

private val styleLabels = mapOf(
    ShareStyle.AURORA to "Aurora",
    ShareStyle.PAPER to "Paper",
    ShareStyle.INK to "Ink",
    ShareStyle.PHOTO to "Photo",
)

@Composable
fun ShareMomentSheetContent(
    moment: MomentUi,
    dateLabel: String,
    format: ShareFormat,
    style: ShareStyle,
    showDate: Boolean,
    showLocation: Boolean,
    showMood: Boolean,
    onFormat: (ShareFormat) -> Unit,
    onStyle: (ShareStyle) -> Unit,
    onToggleDate: (Boolean) -> Unit,
    onToggleLocation: (Boolean) -> Unit,
    onToggleMood: (Boolean) -> Unit,
    onSystemShare: () -> Unit,
    onSaveImage: () -> Unit,
    onCopy: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = MaterialTheme.dimens.spacing.xl)
            .padding(top = MaterialTheme.dimens.spacing.xs, bottom = MaterialTheme.dimens.spacing.xxxl)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Share moment",
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = onSurface,
                modifier = Modifier.weight(1f)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.tile)
                    .clip(CircleShape)
                    .background(onSurface.copy(alpha = 0.07f))
                    .clickable(onClick = onDismiss)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = onSurface.copy(alpha = 0.7f), modifier = Modifier.size(MaterialTheme.dimens.icon.md))
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

        Box(modifier = Modifier.fillMaxWidth().heightIn(max = 440.dp), contentAlignment = Alignment.Center) {
            ShareableMomentCard(
                moment = moment,
                style = style,
                format = format,
                showDate = showDate,
                showLocation = showLocation,
                showMood = showMood,
                dateLabel = dateLabel,
                modifier = Modifier.height(440.dp)
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        ShareFormatSelector(selected = format, onSelect = onFormat, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxl))
        Text("STYLE", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp, color = onSurface.copy(alpha = 0.5f))
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.lg), modifier = Modifier.fillMaxWidth()) {
            ShareStyle.entries.forEach { s ->
                ShareStyleSwatch(
                    style = s,
                    label = styleLabels.getValue(s),
                    selected = s == style,
                    onClick = { onStyle(s) }
                )
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md), modifier = Modifier.fillMaxWidth()) {
            ShareToggleChip("Date", showDate, onToggleDate)
            if (moment.location?.displayName?.isNotBlank() == true) {
                ShareToggleChip("Location", showLocation, onToggleLocation)
            }
            if (moment.moods.isNotEmpty()) {
                ShareToggleChip("Mood", showMood, onToggleMood)
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxl))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            ShareActionButton(
                icon = Icons.Filled.PhotoCamera,
                label = "Story",
                background = Brush.linearGradient(listOf(Color(0xFFF77737), Color(0xFFE1306C), Color(0xFF833AB4))),
                contentColor = Color.White,
                onClick = onSystemShare
            )
            ShareActionButton(
                icon = Icons.AutoMirrored.Filled.Message,
                label = "Message",
                background = SolidColor(Color(0xFF34C759)),
                contentColor = Color.White,
                onClick = onSystemShare
            )
            ShareActionButton(
                icon = Icons.Filled.Image,
                label = "Save image",
                background = SolidColor(MaterialTheme.colorScheme.primary),
                contentColor = Color.White,
                onClick = onSaveImage
            )
            ShareActionButton(
                icon = Icons.Filled.Link,
                label = "Copy",
                background = SolidColor(onSurface.copy(alpha = 0.10f)),
                contentColor = onSurface,
                onClick = onCopy
            )
            ShareActionButton(
                icon = Icons.Filled.MoreHoriz,
                label = "More",
                background = SolidColor(onSurface.copy(alpha = 0.10f)),
                contentColor = onSurface,
                onClick = onSystemShare
            )
        }
    }
}

@Preview
@Composable
private fun ShareMomentSheetContentPreview() {
    PreviewAppTheme {
        ShareMomentSheetContent(
            moment = MomentUi(
                id = "1",
                title = "Morning pages",
                body = "I want more mornings that start this slowly.",
                createdAt = Clock.System.now(),
                moods = persistentListOf(Mood.CALM),
                tags = persistentListOf(),
                attachments = persistentListOf(),
                location = null,
                isFavorite = false,
            ),
            dateLabel = "Saturday, June 27",
            format = ShareFormat.PORTRAIT,
            style = ShareStyle.AURORA,
            showDate = true,
            showLocation = true,
            showMood = true,
            onFormat = {}, onStyle = {}, onToggleDate = {}, onToggleLocation = {}, onToggleMood = {},
            onSystemShare = {}, onSaveImage = {}, onCopy = {}, onDismiss = {},
        )
    }
}
