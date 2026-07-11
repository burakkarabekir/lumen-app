package com.bksd.lumen

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.core.domain.theme.ThemeRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        applyThemeReactively()

        setContent {
            App()
        }
    }

    private fun applyThemeReactively() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
        val sessionStorage: SessionStorage by inject()
        val themeRepository: ThemeRepository by inject()
        val prefs = SplashThemePrefs(applicationContext)
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        lifecycleScope.launch {
            combine(
                sessionStorage.observeAuthState(),
                themeRepository.observeTheme(),
            ) { loggedIn, mode -> loggedIn to mode.name }
                .collect { (loggedIn, mode) ->
                    prefs.update(isLoggedIn = loggedIn, themeMode = mode)
                    val nightMode = prefs.nightMode()
                    if (prefs.lastApplied != nightMode) {
                        prefs.lastApplied = nightMode
                        uiModeManager.setApplicationNightMode(nightMode)
                    }
                }
        }
    }
}
