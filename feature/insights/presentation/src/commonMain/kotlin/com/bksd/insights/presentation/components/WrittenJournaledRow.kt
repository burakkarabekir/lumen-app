package com.bksd.insights.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.insights.presentation.JournaledStat

@Composable
internal fun WrittenJournaledRow(writtenWords: Int, journaled: JournaledStat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md)
    ) {
        WrittenCard(writtenWords, Modifier.weight(1f))
        JournaledCard(journaled, Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun WrittenJournaledRowPreview() {
    AppTheme {
        Box(Modifier.padding(MaterialTheme.dimens.spacing.xl)) {
            WrittenJournaledRow(writtenWords = 4930, journaled = SampleJournaled)
        }
    }
}
