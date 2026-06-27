package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.core.design_system.theme.rememberInsightsPalette
import com.bksd.insights.presentation.InsightsState

@Composable
internal fun StreaksCarousel(state: InsightsState, palette: InsightsPalette) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    Column {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> CurrentStreakCard(state.currentStreak, state.currentDailyStreak)
                1 -> StreakDetailCard(state.longest, palette)
                else -> StreakDetailCard(state.recent, palette)
            }
        }
        Spacer(Modifier.height(14.dp))
        PagerDots(count = 3, current = pagerState.currentPage, palette = palette)
    }
}

@Preview
@Composable
private fun StreaksCarouselPreview() {
    AppTheme {
        val palette = rememberInsightsPalette()
        Box(Modifier.background(palette.pageBg).padding(18.dp)) {
            StreaksCarousel(state = SampleInsightsState, palette = palette)
        }
    }
}
