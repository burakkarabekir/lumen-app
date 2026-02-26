package com.bksd.core.design_system.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.Manrope_Bold
import com.bksd.core.design_system.Manrope_ExtraBold
import com.bksd.core.design_system.Manrope_Light
import com.bksd.core.design_system.Manrope_Medium
import com.bksd.core.design_system.Manrope_Regular
import com.bksd.core.design_system.Manrope_SemiBold
import com.bksd.core.design_system.Res
import org.jetbrains.compose.resources.Font

val Manrope
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.Manrope_Light,
            weight = FontWeight.Light
        ),
        Font(
            resource = Res.font.Manrope_Regular,
            weight = FontWeight.Normal
        ),
        Font(
            resource = Res.font.Manrope_Medium,
            weight = FontWeight.Medium
        ),
        Font(
            resource = Res.font.Manrope_SemiBold,
            weight = FontWeight.SemiBold
        ),
        Font(
            resource = Res.font.Manrope_Bold,
            weight = FontWeight.Bold
        ),
        Font(
            resource = Res.font.Manrope_ExtraBold,
            weight = FontWeight.ExtraBold
        ),
    )

val Typography.labelXSmall: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )

val Typography.titleXSmall: TextStyle
    @Composable get() = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

val Typography
    @Composable get() = Typography(
        displayLarge = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        displayMedium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            lineHeight = 30.sp
        ),
        displaySmall = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 26.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        titleLarge = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp
        ),
        titleSmall = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp
        ),
        labelLarge = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        labelSmall = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            lineHeight = 16.sp
        ),
    )