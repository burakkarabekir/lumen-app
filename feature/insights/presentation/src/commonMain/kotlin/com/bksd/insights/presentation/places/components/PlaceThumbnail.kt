package com.bksd.insights.presentation.places.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.insights.presentation.PlaceKind
import com.bksd.insights.presentation.placeKindIcon

@Composable
internal fun PlaceThumbnail(
    kind: PlaceKind,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
            .background(accent.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.matchParentSize()) {
            val stroke = 1.4.dp.toPx()
            val line = accent.copy(alpha = 0.12f)
            drawLine(line, Offset(0f, size.height * 0.32f), Offset(size.width, size.height * 0.14f), stroke)
            drawLine(line, Offset(0f, size.height * 0.74f), Offset(size.width, size.height * 0.56f), stroke)
            drawLine(line, Offset(size.width * 0.44f, 0f), Offset(size.width * 0.60f, size.height), stroke)
        }
        Icon(
            imageVector = placeKindIcon(kind),
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(MaterialTheme.dimens.icon.lg),
        )
    }
}

@Preview
@Composable
private fun PlaceThumbnailPreview() {
    AppTheme {
        PlaceThumbnail(kind = PlaceKind.GENERIC, accent = Color(0xFF6E7AD0))
    }
}
