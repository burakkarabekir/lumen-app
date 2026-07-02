package com.bksd.insights.presentation.reflection.full.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette

@Composable
fun QuestionsToSitWithCard(
    questions: List<String>,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
        modifier = modifier.fillMaxWidth()
    ) {
        questions.forEach { question ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                    .background(accent.copy(alpha = 0.06f))
                    .border(1.dp, accent.copy(alpha = 0.14f), RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.spacing.sm)
                        .size(MaterialTheme.dimens.icon.xs)
                        .clip(CircleShape)
                        .background(accent)
                )
                Text(
                    text = question,
                    fontSize = 14.5.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = palette.text
                )
            }
        }
    }
}

@Preview
@Composable
private fun QuestionsToSitWithCardPreview() {
    AppTheme(darkTheme = true) {
        QuestionsToSitWithCard(
            questions = listOf(
                "What would it take to protect one slow morning each week?",
                "Where did the week feel most like your own?"
            ),
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
