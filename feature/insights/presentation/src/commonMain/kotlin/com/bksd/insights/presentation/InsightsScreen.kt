package com.bksd.insights.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberInsightsPalette
import com.bksd.insights.presentation.components.EmptyStreakCard
import com.bksd.insights.presentation.components.EntriesCard
import com.bksd.insights.presentation.components.SampleInsightsState
import com.bksd.insights.presentation.components.SectionLabel
import com.bksd.insights.presentation.components.StreaksCarousel
import com.bksd.insights.presentation.components.VisitedPlacesCard
import com.bksd.insights.presentation.components.WrittenJournaledRow

@Composable
internal fun InsightsScreen(
    state: InsightsState,
    onAction: (InsightsAction) -> Unit,
    reflectionSlot: @Composable () -> Unit = {}
) {
    val palette = rememberInsightsPalette()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.pageBg)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "Insights",
            fontSize = 27.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.6).sp,
            color = palette.text
        )
        Spacer(Modifier.height(14.dp))

        SectionLabel("Streaks", palette)
        Spacer(Modifier.height(8.dp))
        if (state.hasActiveStreak) {
            StreaksCarousel(state = state, palette = palette)
        } else {
            EmptyStreakCard(onSetReminder = {})
        }

        Spacer(Modifier.height(18.dp))
        reflectionSlot()
        SectionLabel("Stats", palette)
        Spacer(Modifier.height(8.dp))
        EntriesCard(
            entries = state.entries,
            selectedRange = state.selectedRange,
            rangeOptions = state.rangeOptions,
            onRangeSelect = { onAction(InsightsAction.OnStatsRangeSelect(it)) }
        )

        if (state.places.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            VisitedPlacesCard(places = state.places)
        }

        Spacer(Modifier.height(12.dp))
        WrittenJournaledRow(writtenWords = state.writtenWords, journaled = state.journaled)

        Spacer(Modifier.height(120.dp))
    }
}

@Preview
@Composable
private fun InsightsScreenPreviewLight() {
    AppTheme {
        InsightsScreen(state = SampleInsightsState, onAction = {})
    }
}

@Preview
@Composable
private fun InsightsScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        InsightsScreen(state = SampleInsightsState, onAction = {})
    }
}

@Preview
@Composable
private fun InsightsScreenPreviewEmpty() {
    AppTheme {
        InsightsScreen(state = InsightsState(), onAction = {})
    }
}
