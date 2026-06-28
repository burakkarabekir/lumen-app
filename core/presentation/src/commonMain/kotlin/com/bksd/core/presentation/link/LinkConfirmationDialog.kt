package com.bksd.core.presentation.link

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette

fun toOpenableWebUrl(url: String): String? {
    val trimmed = url.trim()
    if (trimmed.isEmpty()) return null
    val normalized = if (trimmed.contains("://")) trimmed else "https://$trimmed"
    val scheme = normalized.substringBefore("://", "").lowercase()
    return if (scheme == "http" || scheme == "https") normalized else null
}

@Composable
fun LinkConfirmationDialog(
    url: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = modifier
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Open link?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.text
                )
                Text(
                    text = "You'll leave Lumen to open this in your browser.",
                    fontSize = 13.sp,
                    color = palette.sub,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    text = url,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = palette.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 14.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        style = AppButtonStyle.SECONDARY,
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        text = "Open",
                        onClick = onConfirm,
                        style = AppButtonStyle.PRIMARY,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LinkConfirmationDialogPreview() {
    AppTheme(darkTheme = true) {
        LinkConfirmationDialog(
            url = "https://www.alltrails.com/trail/us/california/mission-peak",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
