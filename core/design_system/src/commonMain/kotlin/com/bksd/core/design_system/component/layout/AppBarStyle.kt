package com.bksd.core.design_system.component.layout

sealed class AppBarStyle {
    data object Root : AppBarStyle()
    data object Child : AppBarStyle()
}
