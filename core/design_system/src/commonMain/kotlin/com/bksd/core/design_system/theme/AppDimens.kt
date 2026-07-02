package com.bksd.core.design_system.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimens(
    val spacing: Spacing = Spacing(),
    val radius: Radius = Radius(),
    val icon: IconSize = IconSize(),
    val size: ComponentSize = ComponentSize(),
) {
    @Immutable
    data class Spacing(
        val xxs: Dp = LumenSpacing.xxs,
        val xs: Dp = LumenSpacing.xs,
        val sm: Dp = LumenSpacing.sm,
        val md: Dp = LumenSpacing.md,
        val lg: Dp = LumenSpacing.lg,
        val xl: Dp = LumenSpacing.xl,
        val xxl: Dp = LumenSpacing.xxl,
        val xxxl: Dp = LumenSpacing.xxxl,
        val huge: Dp = LumenSpacing.huge,
        val massive: Dp = LumenSpacing.massive,
    )

    @Immutable
    data class Radius(
        val xs: Dp = LumenRadius.xs,
        val sm: Dp = LumenRadius.sm,
        val md: Dp = LumenRadius.md,
        val cardTight: Dp = 14.dp,
        val lg: Dp = LumenRadius.lg,
        val card: Dp = 22.dp,
        val xl: Dp = LumenRadius.xl,
        val xxl: Dp = LumenRadius.xxl,
        val full: Dp = LumenRadius.full,
    )

    @Immutable
    data class IconSize(
        val xs: Dp = 14.dp,
        val sm: Dp = 16.dp,
        val md: Dp = 18.dp,
        val lg: Dp = 20.dp,
        val xl: Dp = 24.dp,
        val avatar: Dp = 36.dp,
        val tile: Dp = 38.dp,
    )

    @Immutable
    data class ComponentSize(
        val fab: Dp = 56.dp,
        val cancelIcon: Dp = 48.dp,
        val topBar: Dp = 64.dp,
    )
}

val LocalAppDimens = staticCompositionLocalOf { AppDimens() }

val MaterialTheme.dimens: AppDimens
    @Composable
    @ReadOnlyComposable
    get() = LocalAppDimens.current
