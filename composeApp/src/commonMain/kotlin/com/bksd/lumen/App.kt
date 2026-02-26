package com.bksd.lumen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.LumenTheme
import com.bksd.lumen.navigation.NavigationRoot

@Composable
@Preview
fun App(
     isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    LumenTheme(
         darkTheme = isDarkTheme
    ) {
        NavigationRoot()
    }
}