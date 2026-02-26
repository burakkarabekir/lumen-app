package com.bksd.lumen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import com.bksd.lumen.navigation.route.Route
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Stable
class NavigationState internal constructor(
    val startRoute: NavKey,
    private val topLevelRouteState: MutableState<NavKey>,
    private val backStacksMap: ImmutableMap<NavKey, NavBackStack<NavKey>>,
) {
    var topLevelRoute: NavKey by topLevelRouteState

    val stacksInUse: List<NavKey> by derivedStateOf {
        if (topLevelRoute == startRoute) listOf(startRoute)
        else listOf(startRoute, topLevelRoute)
    }

    val currentRoute: NavKey by derivedStateOf {
        val activeKey = if (topLevelRoute in backStacksMap) {
            topLevelRoute
        } else {
            startRoute
        }

        backStacksMap[activeKey]
            ?.lastOrNull()
            ?: startRoute
        }

    val backStacks: ImmutableMap<NavKey, NavBackStack<NavKey>>
        get() = backStacksMap
}

@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: ImmutableSet<NavKey>,
): NavigationState {
    val topLevelRouteState = rememberSerializable(
        startRoute,
        topLevelRoutes,
        configuration = serializersConfig,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class))
    ) {
        mutableStateOf(startRoute)
    }
    val orderedRoutes = remember(topLevelRoutes) { topLevelRoutes.toList() }
    val backStacks = orderedRoutes.associateWith { key ->
        rememberNavBackStack(
            configuration = serializersConfig,
            key
        )
    }.toImmutableMap()

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRouteState = topLevelRouteState,
            backStacksMap = backStacks
        )
    }
}

val serializersConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            // Main tabs
            subclass(Route.Main.Journal::class, Route.Main.Journal.serializer())
            subclass(Route.Main.Insights::class, Route.Main.Insights.serializer())
            subclass(Route.Main.Profile::class, Route.Main.Profile.serializer())
            // Onboarding
            subclass(Route.Onboarding::class, Route.Onboarding.serializer())
            // Auth
            subclass(Route.Auth.SignIn::class, Route.Auth.SignIn.serializer())
            subclass(Route.Auth.SignUp::class, Route.Auth.SignUp.serializer())
            subclass(Route.Auth.ResetPassword::class, Route.Auth.ResetPassword.serializer())
            // Sub-screens
            subclass(Route.MomentDetail::class, Route.MomentDetail.serializer())
            subclass(Route.CreateMoment::class, Route.CreateMoment.serializer())
            subclass(Route.Paywall::class, Route.Paywall.serializer())
        }
    }
}

@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val saveableDecorator = rememberSaveableStateHolderNavEntryDecorator<NavKey>()
    val viewModelDecorator = rememberViewModelStoreNavEntryDecorator<NavKey>()
    val decorators = listOf(saveableDecorator, viewModelDecorator)
    val decoratedEntriesByStack = backStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntriesByStack[it].orEmpty() }
        .toMutableStateList()
}