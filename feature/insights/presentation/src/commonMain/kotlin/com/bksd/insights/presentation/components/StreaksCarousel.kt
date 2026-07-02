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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberInsightsPalette
import com.bksd.insights.presentation.InsightsState

@Composable
internal fun StreaksCarousel(state: InsightsState, palette: InsightsPalette) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    Column {
        HorizontalPager(
            state = pagerState,
            pageSpacing = MaterialTheme.dimens.spacing.md,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> CurrentStreakCard(state.currentStreak, state.currentDailyStreak)
                1 -> StreakDetailCard(state.longest, palette)
                else -> StreakDetailCard(state.recent, palette)
            }
        }
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
        PagerDots(count = 3, current = pagerState.currentPage, palette = palette)
    }
}

@Preview
@Composable
private fun StreaksCarouselPreview() {
    AppTheme {
        val palette = rememberInsightsPalette()
        Box(Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.xl)) {
            StreaksCarousel(state = SampleInsightsState, palette = palette)
        }
    }
}
