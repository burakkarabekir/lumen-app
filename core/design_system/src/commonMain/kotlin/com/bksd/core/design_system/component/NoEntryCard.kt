package com.bksd.core.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.Res
import com.bksd.core.design_system.confused_and_happy_mood_faces
import com.bksd.core.design_system.desc_entry_empty
import com.bksd.core.design_system.moods
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.title_list_empty
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun NoEntryCard(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = vectorResource(Res.drawable.moods),
            contentDescription = stringResource(Res.string.confused_and_happy_mood_faces)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.title_list_empty),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(Res.string.desc_entry_empty),
             color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        NoEntryCard()
    }
}