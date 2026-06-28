package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette

@Composable
fun PhotoAttachmentCard(
    imageModel: Any?,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
    compact: Boolean = false
) {
    if (compact) {
        AttachmentImage(
            model = imageModel,
            height = 160.dp,
            modifier = modifier,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
        )
        return
    }
    AttachmentCardChrome(
        badgeColor = PhotoBadgeColor,
        badgeIcon = Icons.Default.Image,
        title = "Photo",
        onRemove = onRemove,
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)) {
            AttachmentImage(model = imageModel, height = 188.dp)
        }
    }
}

@Preview
@Composable
private fun PhotoAttachmentCardPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(16.dp)) {
            PhotoAttachmentCard(imageModel = "", onRemove = {})
        }
    }
}
