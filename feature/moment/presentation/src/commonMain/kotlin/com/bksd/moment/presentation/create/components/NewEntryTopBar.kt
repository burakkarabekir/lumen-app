package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.action_save
import com.bksd.moment.presentation.content_desc_close
import com.bksd.moment.presentation.new_entry_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewEntryTopBar(
    onClose: () -> Unit,
    onSave: () -> Unit,
    saveEnabled: Boolean,
    isSaving: Boolean,
    modifier: Modifier = Modifier,
) {
    val palette = rememberNewEntryPalette()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.pageBg)
            .height(56.dp)
            .padding(horizontal = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(38.dp)
                .clip(CircleShape)
                .background(palette.surface)
                .clickable(onClick = onClose)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(Res.string.content_desc_close),
                tint = palette.sub,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = stringResource(Res.string.new_entry_title),
            modifier = Modifier.align(Alignment.Center),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = palette.text
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(17.dp))
                .background(palette.saveBg.copy(alpha = if (saveEnabled) 1f else 0.4f))
                .clickable(enabled = saveEnabled && !isSaving, onClick = onSave)
                .padding(horizontal = 17.dp, vertical = 8.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text(
                    text = stringResource(Res.string.action_save),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewEntryTopBarPreview() {
    AppTheme(darkTheme = true) {
        NewEntryTopBar(onClose = {}, onSave = {}, saveEnabled = true, isSaving = false)
    }
}
