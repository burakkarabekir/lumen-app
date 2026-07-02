package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.insightsWrittenGradient
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.stat_words
import com.bksd.insights.presentation.stat_written
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun WrittenCard(words: Int, modifier: Modifier) {
    val gradient = MaterialTheme.colorScheme.extended.insightsWrittenGradient
    Column(
        modifier = modifier
            .height(StatCardHeight)
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(Brush.verticalGradient(gradient))
            .padding(MaterialTheme.dimens.spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.stat_written),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.9f)
        )
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.md))
        Text(
            text = words.grouped(),
            fontSize = 40.sp,
            lineHeight = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = stringResource(Res.string.stat_words),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

private fun Int.grouped(): String =
    toString().reversed().chunked(3).joinToString(",").reversed()

@Preview
@Composable
private fun WrittenCardPreview() {
    AppTheme {
        Box(Modifier.padding(MaterialTheme.dimens.spacing.xl).width(170.dp)) {
            WrittenCard(words = 4930, modifier = Modifier)
        }
    }
}
