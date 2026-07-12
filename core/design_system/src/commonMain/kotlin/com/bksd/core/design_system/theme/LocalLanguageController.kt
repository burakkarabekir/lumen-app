package com.bksd.core.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalLanguageController = staticCompositionLocalOf<LanguageController> {
    error("LanguageController not provided. Ensure LocalLanguageController is provided at the app root.")
}

@Composable
fun rememberLanguageController(): LanguageController = LocalLanguageController.current
