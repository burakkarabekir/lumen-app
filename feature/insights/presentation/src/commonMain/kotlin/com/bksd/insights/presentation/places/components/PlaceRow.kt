package com.bksd.insights.presentation.places.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentIndigo
import com.bksd.core.design_system.theme.rememberInsightsPalette
import com.bksd.insights.presentation.PlaceKind
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.place_entry_count
import com.bksd.insights.presentation.place_last
import com.bksd.insights.presentation.placeAccentColor
import com.bksd.insights.presentation.places.PlaceUi
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlaceRow(
    place: PlaceUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = rememberInsightsPalette()
    val extended = MaterialTheme.colorScheme.extended
    val accent = placeAccentColor(place.accentIndex, extended)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
            .clickable(onClick = onClick)
            .padding(vertical = MaterialTheme.dimens.spacing.md),
    ) {
        PlaceThumbnail(kind = place.kind, accent = accent)
        Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = place.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = palette.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = pluralStringResource(Res.plurals.place_entry_count, place.count, place.count),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = extended.profileAccentIndigo,
                )
                Text(
                    text = "  ·  ${stringResource(Res.string.place_last, place.lastEntryLabel)}",
                    fontSize = 13.sp,
                    color = palette.sub,
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = palette.sub,
            modifier = Modifier.size(MaterialTheme.dimens.icon.md),
        )
    }
}

@Preview
@Composable
private fun PlaceRowPreview() {
    AppTheme {
        PlaceRow(
            place = PlaceUi(
                name = "London, UK",
                count = 24,
                lastEntryLabel = "Jun 27",
                kind = PlaceKind.GENERIC,
                accentIndex = 0,
            ),
            onClick = {},
        )
    }
}
