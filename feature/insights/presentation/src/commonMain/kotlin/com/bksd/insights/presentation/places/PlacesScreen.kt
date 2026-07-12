package com.bksd.insights.presentation.places

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberInsightsPalette
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.insights.presentation.PlaceKind
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.places.components.PlaceRow
import com.bksd.insights.presentation.places.components.PlacesSearchField
import com.bksd.insights.presentation.places_empty
import com.bksd.insights.presentation.places_header
import com.bksd.insights.presentation.places_no_results
import com.bksd.insights.presentation.places_title
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlacesRoot(onBack: () -> Unit) {
    val viewModel = koinViewModel<PlacesViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            PlacesEvent.NavigateBack -> onBack()
        }
    }

    PlacesScreen(state = state, onAction = viewModel::onAction)
}

@Composable
private fun PlacesScreen(
    state: PlacesState,
    onAction: (PlacesAction) -> Unit,
) {
    val palette = rememberInsightsPalette()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.pageBg)
            .statusBarsPadding(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.spacing.lg,
                    vertical = MaterialTheme.dimens.spacing.md,
                ),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.tile)
                    .clip(CircleShape)
                    .background(palette.surface)
                    .clickable { onAction(PlacesAction.OnBack) },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = palette.text,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.xl),
                )
            }
            Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
            Text(
                text = stringResource(Res.string.places_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = palette.text,
            )
        }

        PlacesSearchField(
            query = state.query,
            onQueryChange = { onAction(PlacesAction.OnSearchChange(it)) },
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.spacing.lg),
        )

        Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
        Text(
            text = pluralStringResource(Res.plurals.places_header, state.places.size, state.places.size),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp,
            color = palette.label,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.spacing.lg),
        )
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))

        if (state.places.isEmpty() && !state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().weight(1f).padding(MaterialTheme.dimens.spacing.xxl),
            ) {
                Text(
                    text = stringResource(
                        if (state.query.isBlank()) Res.string.places_empty else Res.string.places_no_results,
                    ),
                    color = palette.sub,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg),
            ) {
                itemsIndexed(state.places, key = { _, place -> place.name }) { index, place ->
                    PlaceRow(place = place, onClick = {})
                    if (index < state.places.lastIndex) {
                        HorizontalDivider(color = palette.hair)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PlacesScreenPreview() {
    AppTheme {
        PlacesScreen(
            state = PlacesState(
                isLoading = false,
                places = persistentListOf(
                    PlaceUi("London, UK", 24, "Jun 27", PlaceKind.GENERIC, 0),
                    PlaceUi("Mountain View, California", 18, "Jun 21", PlaceKind.GENERIC, 1),
                    PlaceUi("Delice, Kırıkkale", 11, "Jun 14", PlaceKind.GENERIC, 2),
                    PlaceUi("Lisbon, Portugal", 7, "May 30", PlaceKind.PARK, 4),
                ),
            ),
            onAction = {},
        )
    }
}
