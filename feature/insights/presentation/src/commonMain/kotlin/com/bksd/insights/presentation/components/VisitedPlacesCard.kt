package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.insightsPlacesGradient
import com.bksd.core.design_system.theme.insightsPlacesLightGradient
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.VisitedPlace
import com.bksd.insights.presentation.places_show_more
import com.bksd.insights.presentation.visited_places
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun VisitedPlacesCard(
    places: ImmutableList<VisitedPlace>,
    onShowMore: () -> Unit = {},
) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val gradient = if (dark) {
        MaterialTheme.colorScheme.extended.insightsPlacesGradient
    } else {
        MaterialTheme.colorScheme.extended.insightsPlacesLightGradient
    }
    val shape = RoundedCornerShape(MaterialTheme.dimens.radius.card)
    val titleColor = if (dark) Color.White else MaterialTheme.colorScheme.onSurface
    val moreColor = if (dark) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (dark) Modifier else Modifier.shadow(2.dp, shape))
            .clip(shape)
            .background(brush = Brush.verticalGradient(gradient))
            .padding(MaterialTheme.dimens.spacing.lg)
    ) {
        Text(
            text = stringResource(Res.string.visited_places),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
            maxItemsInEachRow = 2
        ) {
            places.take(4).forEach { place -> PlaceChip(place) }
        }
        if (places.size > 4) {
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                    .clickable(onClick = onShowMore)
                    .padding(vertical = MaterialTheme.dimens.spacing.sm),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.places_show_more),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = moreColor,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = moreColor,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.md),
                )
            }
        }
    }
}

@Preview
@Composable
private fun VisitedPlacesCardPreview() {
    AppTheme {
        Box(Modifier.padding(MaterialTheme.dimens.spacing.xl)) {
            VisitedPlacesCard(SamplePlaces)
        }
    }
}
