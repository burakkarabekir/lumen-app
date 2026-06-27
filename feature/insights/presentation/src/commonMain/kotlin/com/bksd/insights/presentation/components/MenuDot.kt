package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
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
internal fun MenuDot(
    tint: Color,
    bg: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.MoreHoriz,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(bg)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview
@Composable
private fun MenuDotPreview() {
    AppTheme {
        Box(Modifier.background(Color(0xFF30344F)).padding(8.dp)) {
            MenuDot(tint = Color.White.copy(alpha = 0.7f), bg = Color.White.copy(alpha = 0.12f))
        }
    }
}
