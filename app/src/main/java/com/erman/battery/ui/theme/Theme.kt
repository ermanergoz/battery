package com.erman.battery.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val outline: Color,
    val selected: Color,
    val onSelected: Color,
    val gauge: Color
)

private val LightColors = lightColors(
    primary = BlueLight,
    primaryVariant = BlueLight,
    secondary = BlueLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = ErrorLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight
)

private val DarkColors = darkColors(
    primary = BlueDark,
    primaryVariant = BlueDark,
    secondary = BlueDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorDark,
    onPrimary = OnBlueDark,
    onSecondary = OnBlueDark,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark
)

private val LightExtendedColors = ExtendedColors(
    outline = OutlineLight,
    selected = SelectedLight,
    onSelected = OnSelectedLight,
    gauge = BatteryBlue
)

private val DarkExtendedColors = ExtendedColors(
    outline = OutlineDark,
    selected = SelectedDark,
    onSelected = OnSelectedDark,
    gauge = BlueDark
)

private val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

@Composable
fun BatteryTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalExtendedColors provides if (darkTheme) DarkExtendedColors else LightExtendedColors
    ) {
        MaterialTheme(
            colors = if (darkTheme) DarkColors else LightColors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

object BatteryTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
