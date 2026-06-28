package com.bksd.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.bksd.profile.presentation.about_app_name
import com.bksd.profile.presentation.about_tagline
import com.bksd.profile.presentation.about_version
import com.bksd.profile.presentation.logo_blue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutHeader(
    version: String,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 24.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.logo_blue),
            contentDescription = null,
            modifier = Modifier.size(84.dp)
        )
        Text(
            text = stringResource(Res.string.about_app_name),
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp,
            color = palette.text,
            modifier = Modifier.padding(top = 14.dp)
        )
        Text(
            text = stringResource(Res.string.about_version, version),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = palette.sub,
            modifier = Modifier.padding(top = 5.dp)
        )
        Text(
            text = stringResource(Res.string.about_tagline),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = palette.bodyText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(top = 12.dp, start = 32.dp, end = 32.dp)
        )
    }
}

@Preview
@Composable
private fun AboutHeaderPreview() {
    PreviewAppTheme(darkTheme = true) {
        AboutHeader(version = "1.0 (1)")
    }
}
