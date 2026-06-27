package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.insights.presentation.PlaceKind
import com.bksd.insights.presentation.VisitedPlace

@Composable
internal fun PlaceChip(place: VisitedPlace, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.07f))
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = placeIcon(place.kind),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = place.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "${place.count}",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFE9A98E)
        )
    }
}

private fun placeIcon(kind: PlaceKind): ImageVector =
    when (kind) {
        PlaceKind.BEACH -> Icons.Default.BeachAccess
        PlaceKind.LANDMARK -> Icons.Default.AccountBalance
        PlaceKind.PARK -> Icons.Default.Park
        PlaceKind.RESTAURANT -> Icons.Default.Restaurant
        PlaceKind.GENERIC -> Icons.Default.Place
    }

@Preview
@Composable
private fun PlaceChipPreview() {
    AppTheme {
        Box(Modifier.background(Color(0xFF2B2D48)).padding(16.dp)) {
            PlaceChip(VisitedPlace("California", 4, PlaceKind.GENERIC))
        }
    }
}
