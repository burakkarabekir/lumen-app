package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme

@Composable
fun DetailActionButton(
    icon: ImageVector,
    tint: Color,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(54.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(background)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(21.dp)
        )
    }
}

@Preview
@Composable
private fun DetailActionButtonPreview() {
    AppTheme(darkTheme = true) {
        Row(modifier = Modifier.padding(16.dp)) {
            DetailActionButton(
                icon = Icons.Default.Delete,
                tint = Color(0xFF9C9AA6),
                background = Color(0xFF26262C),
                onClick = {},
                modifier = Modifier.padding(end = 12.dp)
            )
            DetailActionButton(
                icon = Icons.Default.Favorite,
                tint = Color(0xFFE5484D),
                background = Color(0xFF26262C),
                onClick = {}
            )
        }
    }
}
