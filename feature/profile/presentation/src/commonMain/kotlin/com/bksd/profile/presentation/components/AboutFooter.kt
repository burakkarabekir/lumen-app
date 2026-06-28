package com.bksd.profile.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.about_copyright
import com.bksd.profile.presentation.about_made_with_love
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutFooter(
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = stringResource(Res.string.about_made_with_love),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = palette.sub,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.about_copyright),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = palette.sub.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Preview
@Composable
private fun AboutFooterPreview() {
    PreviewAppTheme(darkTheme = true) {
        AboutFooter()
    }
}
