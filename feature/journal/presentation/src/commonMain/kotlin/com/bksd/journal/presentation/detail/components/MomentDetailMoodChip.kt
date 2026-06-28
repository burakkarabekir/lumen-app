package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.bksd.core.design_system.theme.extended
import com.bksd.core.domain.model.Mood
import com.bksd.journal.presentation.journal.components.moodColors

@Composable
fun MomentDetailMoodChip(
    mood: Mood,
    modifier: Modifier = Modifier
) {
    val (background, accent) = moodColors(mood, MaterialTheme.colorScheme.extended)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(background)
            .padding(start = 11.dp, end = 13.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Icon(
            imageVector = detailMoodIcon(mood),
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = mood.label,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun MomentDetailMoodChipPreview() {
    AppTheme(darkTheme = true) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MomentDetailMoodChip(Mood.CALM)
            MomentDetailMoodChip(Mood.GRATEFUL)
        }
    }
}
