package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import com.bksd.core.domain.model.Mood
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.mood_less
import com.bksd.moment.presentation.mood_more
import com.bksd.moment.presentation.mood_section_subtitle
import com.bksd.moment.presentation.mood_section_title
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import org.jetbrains.compose.resources.stringResource

private const val COLLAPSED_MOOD_COUNT = 8

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodSection(
    selectedMoods: PersistentSet<Mood>,
    isExpanded: Boolean,
    onMoodClick: (Mood) -> Unit,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = rememberNewEntryPalette()
    val allMoods = Mood.entries
    val visibleMoods = if (isExpanded) allMoods else allMoods.take(COLLAPSED_MOOD_COUNT)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.mood_section_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = palette.text
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = stringResource(Res.string.mood_section_subtitle),
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium,
            color = palette.sub
        )
        Spacer(Modifier.height(13.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            visibleMoods.forEach { mood ->
                MoodChip(
                    mood = mood,
                    isSelected = mood in selectedMoods,
                    onClick = { onMoodClick(mood) }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(palette.surface)
                    .clickable(onClick = onToggleExpand)
                    .padding(horizontal = 14.dp, vertical = 9.dp)
            ) {
                Text(
                    text = if (isExpanded) {
                        stringResource(Res.string.mood_less)
                    } else {
                        stringResource(
                            Res.string.mood_more,
                            (allMoods.size - COLLAPSED_MOOD_COUNT).toString()
                        )
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.text
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = palette.sub,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MoodSectionPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Column(
            modifier = Modifier
                .background(palette.pageBg)
                .padding(16.dp)
        ) {
            MoodSection(
                selectedMoods = persistentSetOf(Mood.CALM, Mood.GRATEFUL),
                isExpanded = false,
                onMoodClick = {},
                onToggleExpand = {}
            )
        }
    }
}
