package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.attachmentPhoto
import com.bksd.core.design_system.theme.attachmentRemove
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.presentation.Res
import com.bksd.core.presentation.cd_remove_attachment
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AttachmentCardChrome(
    badgeColor: Color,
    badgeIcon: ImageVector,
    title: String,
    onRemove: (() -> Unit)?,
    modifier: Modifier = Modifier,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    body: @Composable ColumnScope.() -> Unit
) {
    val palette = rememberNewEntryPalette()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(palette.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.avatar)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                    .background(badgeColor.copy(alpha = 0.16f))
            ) {
                Icon(
                    imageVector = badgeIcon,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                )
            }
            Text(
                text = title,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = palette.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (trailing != null) trailing()
            if (onRemove != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.icon.xl)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.extended.attachmentRemove.copy(alpha = 0.14f))
                        .clickable(onClick = onRemove)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.cd_remove_attachment),
                        tint = MaterialTheme.colorScheme.extended.attachmentRemove,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                    )
                }
            }
        }
        body()
    }
}

@Preview
@Composable
private fun AttachmentCardChromePreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            AttachmentCardChrome(
                badgeColor = MaterialTheme.colorScheme.extended.attachmentPhoto,
                badgeIcon = Icons.Default.Image,
                title = "Photo",
                onRemove = {},
                trailing = {
                    Text(text = "1334 × 750", fontSize = 12.sp, color = palette.sub)
                }
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = MaterialTheme.dimens.spacing.md, end = MaterialTheme.dimens.spacing.md, bottom = MaterialTheme.dimens.spacing.md)
                        .fillMaxWidth()
                        .size(120.dp)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                        .background(Color(0xFF3A3F63))
                )
            }
        }
    }
}
