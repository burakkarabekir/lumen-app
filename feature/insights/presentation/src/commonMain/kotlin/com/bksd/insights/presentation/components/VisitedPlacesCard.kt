package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.insights.presentation.VisitedPlace
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun VisitedPlacesCard(places: ImmutableList<VisitedPlace>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF2B2D48), Color(0xFF1B1C2B))))
            .padding(16.dp)
    ) {
        Text(
            text = "Visited Places",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        places.chunked(2).forEachIndexed { index, rowPlaces ->
            if (index > 0) Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowPlaces.forEach { place -> PlaceChip(place, Modifier.weight(1f)) }
                if (rowPlaces.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
private fun VisitedPlacesCardPreview() {
    AppTheme {
        Box(Modifier.padding(18.dp)) {
            VisitedPlacesCard(SamplePlaces)
        }
    }
}
