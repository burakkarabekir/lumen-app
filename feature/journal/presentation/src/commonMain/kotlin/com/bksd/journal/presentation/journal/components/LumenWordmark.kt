package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme

@Composable
fun LumenWordmark(modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val brush = Brush.linearGradient(
        listOf(primary, lerp(primary, Color.White, 0.4f)),
    )
    Text(
        text = "Lumen",
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge.copy(
            brush = brush,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp,
        ),
    )
}

@Preview
@Composable
private fun LumenWordmarkPreview() {
    AppTheme {
        Box(Modifier.padding(16.dp)) { LumenWordmark() }
    }
}

@Preview
@Composable
private fun LumenWordmarkDarkPreview() {
    AppTheme(darkTheme = true) {
        Box(Modifier.padding(16.dp)) { LumenWordmark() }
    }
}
