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
    data class Onboarding(val greeting: String) : Route

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
    }

    // ==================== Detail / Sub-screen Routes ====================
    @Serializable
    data class MomentDetail(val momentId: String, val isEditing: Boolean = false) : Route

    @Serializable
    data object CreateMoment : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data object EditProfile : Route

    @Serializable
    data object About : Route

    @Serializable
    data object Help : Route

    @Serializable
    data object WeeklyReflection : Route

    @Serializable
    data object Places : Route

    @Serializable
    data object Paywall : Route

    @Serializable
    data object ManagePremium : Route

    @Serializable
    data object CloudSync : Route

    @Serializable
    data object LockPrivacy : Route

    @Serializable
    data object ExportJournal : Route

    @Serializable
    data object Legal : Route

    @Serializable
    data class LegalDocument(val url: String, val title: String) : Route

    companion object {
        fun NavKey.shouldShowBottomBar(): Boolean =
            when (this) {
                Main.Journal,
                Main.Insights -> true

                else -> false
            }
    }
}