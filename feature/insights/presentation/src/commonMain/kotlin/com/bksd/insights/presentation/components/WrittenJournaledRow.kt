package com.bksd.insights.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.insights.presentation.JournaledStat

@Composable
internal fun WrittenJournaledRow(writtenWords: Int, journaled: JournaledStat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        WrittenCard(writtenWords, Modifier.weight(1f))
        JournaledCard(journaled, Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun WrittenJournaledRowPreview() {
    AppTheme {
        Box(Modifier.padding(18.dp)) {
            WrittenJournaledRow(writtenWords = 4930, journaled = SampleJournaled)
        }
    }
}
