package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.attachmentLink
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette

private val LinkTileColors = listOf(Color(0xFFA98FD6), Color(0xFF8A6FBF))

private fun linkHost(url: String): String {
    var s = url.trim()
    val scheme = s.indexOf("://")
    if (scheme >= 0) s = s.substring(scheme + 3)
    s = s.substringBefore('/').removePrefix("www.")
    return s.ifBlank { url }
}

@Composable
fun LinkAttachmentCard(
    url: String,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    compact: Boolean = false
) {
    val palette = rememberNewEntryPalette()
    if (compact) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                .background(palette.hairline)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
        ) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.extended.attachmentLink,
                modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
            )
            Text(
                text = linkHost(url),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = palette.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        return
    }
    AttachmentCardChrome(
        badgeColor = MaterialTheme.colorScheme.extended.attachmentLink,
        badgeIcon = Icons.Default.Link,
        title = "Link",
        onRemove = onRemove,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(palette.hairline)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(MaterialTheme.dimens.size.cancelIcon)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                    .background(Brush.linearGradient(LinkTileColors))
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = linkHost(url),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = url,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = palette.sub,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xxs)
                )
            }
            Icon(
                imageVector = Icons.Default.NorthEast,
                contentDescription = null,
                tint = palette.sub,
                modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
            )
        }
    }
}

@Preview
@Composable
private fun LinkAttachmentCardPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            LinkAttachmentCard(url = "https://www.alltrails.com/trail/mission-peak", onRemove = {})
        }
    }
}
