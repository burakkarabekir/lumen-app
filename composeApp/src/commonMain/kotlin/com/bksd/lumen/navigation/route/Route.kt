package com.bksd.lumen.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Defines all navigation routes in the Lumen application.
 * Routes are organized hierarchically by feature area.
 */
@Serializable
sealed interface Route : NavKey {

    // ==================== Onboarding ====================
    @Serializable
    data object Onboarding : Route

    // ==================== Authentication ====================
    @Serializable
    sealed interface Auth : Route {
        @Serializable
        data object SignIn : Auth

        @Serializable
        data object SignUp : Auth

        @Serializable
        data object ResetPassword : Auth
    }

    // ==================== Main (Bottom Navigation tabs) ====================
    @Serializable
    sealed interface Main : Route {
        @Serializable
        data object Journal : Main

        @Serializable
        data object Insights : Main

        @Serializable
        data object Profile : Main
    }

    // ==================== Detail / Sub-screen Routes ====================
    @Serializable
    data class MomentDetail(val momentId: String) : Route

    @Serializable
    data object CreateMoment : Route

    @Serializable
    data object Paywall : Route

    companion object {
        fun NavKey.shouldShowBottomBar(): Boolean =
            when (this) {
                Main.Journal,
                Main.Insights,
                Main.Profile -> true

                else -> false
            }
    }
}