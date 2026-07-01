package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.reflection.domain.support.SupportResource

@Composable
fun SupportResourceRow(
    resource: SupportResource,
    accent: Color,
    rowBackground: Color,
    labelColor: Color,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(rowBackground)
            .clickable { runCatching { uriHandler.openUri("tel:${resource.phoneNumber}") } }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = resource.label,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = labelColor
            )
            Text(
                text = resource.phoneNumber,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = accent,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(accent.copy(alpha = 0.16f))
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call ${resource.label}",
                tint = accent,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SupportResourceRowPreview() {
    AppTheme {
        SupportResourceRow(
            resource = SupportResource("Emergency", "112"),
            accent = Color(0xFFC08A1E),
            rowBackground = Color.White,
            labelColor = Color(0xFF22203A),
            modifier = Modifier.padding(16.dp)
        )
    }
}
