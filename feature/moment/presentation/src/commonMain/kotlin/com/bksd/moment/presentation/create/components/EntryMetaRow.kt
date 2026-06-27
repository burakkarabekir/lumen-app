package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.add_location
import com.bksd.moment.presentation.content_desc_location
import com.bksd.moment.presentation.content_desc_remove_location
import org.jetbrains.compose.resources.stringResource

@Composable
fun EntryMetaRow(
    dateHeadline: String,
    timestamp: String,
    locationName: String?,
    isFetchingLocation: Boolean,
    onAddLocation: () -> Unit,
    onRemoveLocation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = rememberNewEntryPalette()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Text(
                text = dateHeadline,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = palette.text
            )
            Text(
                text = timestamp,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = palette.sub
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(palette.pinBg)
                .clickable(
                    enabled = locationName == null && !isFetchingLocation,
                    onClick = onAddLocation
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Place,
                contentDescription = stringResource(Res.string.content_desc_location),
                tint = palette.pinFg,
                modifier = Modifier.size(13.dp)
            )
            if (isFetchingLocation) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 2.dp,
                    color = palette.pinFg
                )
            } else {
                Text(
                    text = locationName ?: stringResource(Res.string.add_location),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.pinFg
                )
            }
            if (locationName != null) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(Res.string.content_desc_remove_location),
                    tint = palette.pinFg,
                    modifier = Modifier
                        .size(12.dp)
                        .clickable(onClick = onRemoveLocation)
                )
            }
        }
    }
}

@Preview
@Composable
private fun EntryMetaRowPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Row(
            modifier = Modifier
                .background(palette.pageBg)
                .padding(16.dp)
        ) {
            EntryMetaRow(
                dateHeadline = "June 27",
                timestamp = "Today, 9:41 AM",
                locationName = "Mountain View",
                isFetchingLocation = false,
                onAddLocation = {},
                onRemoveLocation = {}
            )
        }
    }
}
