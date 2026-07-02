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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.insightsPlaceChipCount
import com.bksd.insights.presentation.PlaceKind
import com.bksd.insights.presentation.VisitedPlace

@Composable
internal fun PlaceChip(place: VisitedPlace, modifier: Modifier = Modifier) {
    val countColor = MaterialTheme.colorScheme.extended.insightsPlaceChipCount
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
            .background(Color.White.copy(alpha = 0.07f))
            .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = placeIcon(place.kind),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
        )
        Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
        Text(
            text = place.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1
        )
        Spacer(Modifier.width(MaterialTheme.dimens.spacing.sm))
        Text(
            text = "${place.count}",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = countColor
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
        Box(Modifier.background(Color(0xFF2B2D48)).padding(MaterialTheme.dimens.spacing.lg)) {
            PlaceChip(VisitedPlace("California", 4, PlaceKind.GENERIC))
        }
    }
}
