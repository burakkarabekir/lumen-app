package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
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
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.content_desc_location
import com.bksd.moment.presentation.content_desc_remove_location
import org.jetbrains.compose.resources.stringResource

private val LocationBoxHeight = 34.dp

@Composable
fun EntryMetaRow(
    dateHeadline: String,
    timestamp: String,
    locationName: String?,
    isFetchingLocation: Boolean,
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
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)
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

        Box(
            modifier = Modifier.height(LocationBoxHeight),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (isFetchingLocation || locationName != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                    modifier = Modifier
                        .height(LocationBoxHeight)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .background(palette.pinBg)
                        .padding(horizontal = MaterialTheme.dimens.spacing.md)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = stringResource(Res.string.content_desc_location),
                        tint = palette.pinFg,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
                    )
                    if (isFetchingLocation) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(MaterialTheme.dimens.icon.xs),
                            strokeWidth = 2.dp,
                            color = palette.pinFg
                        )
                    } else {
                        Text(
                            text = locationName.orEmpty(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.pinFg,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 150.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(Res.string.content_desc_remove_location),
                            tint = palette.pinFg,
                            modifier = Modifier
                                .size(MaterialTheme.dimens.icon.xs)
                                .clickable(onClick = onRemoveLocation)
                        )
                    }
                }
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
                .padding(MaterialTheme.dimens.spacing.lg)
        ) {
            EntryMetaRow(
                dateHeadline = "June 27",
                timestamp = "Today, 9:41 AM",
                locationName = "Mountain View",
                isFetchingLocation = false,
                onRemoveLocation = {}
            )
        }
    }
}
