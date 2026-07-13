package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.attachmentPhoto
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.presentation.Res
import com.bksd.core.presentation.attachment_label_photo
import org.jetbrains.compose.resources.stringResource

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
            shape = RoundedCornerShape(topStart = MaterialTheme.dimens.radius.md, topEnd = MaterialTheme.dimens.radius.md)
        )
        return
    }
    AttachmentCardChrome(
        badgeColor = MaterialTheme.colorScheme.extended.attachmentPhoto,
        badgeIcon = Icons.Default.Image,
        title = stringResource(Res.string.attachment_label_photo),
        onRemove = onRemove,
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.md, end = MaterialTheme.dimens.spacing.md, bottom = MaterialTheme.dimens.spacing.md)) {
            AttachmentImage(model = imageModel, height = 188.dp)
        }
    }
}

@Preview
@Composable
private fun PhotoAttachmentCardPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            PhotoAttachmentCard(imageModel = "", onRemove = {})
        }
    }
}
