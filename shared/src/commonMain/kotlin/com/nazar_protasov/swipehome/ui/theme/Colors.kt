package com.nazar_protasov.swipehome.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Базові колірні палітри SwipeHome
val EmeraldPrimary = Color(0xFF0D9488)
val CoralSecondary = Color(0xFFFF6B6B)
val NeutralDark = Color(0xFF18181B)
val NeutralLight = Color(0xFFF4F4F5)
val BackgroundLightColor = Color(0xFFFFFFFF)
val BackgroundDarkColor = Color(0xFF121212)
val ErrorRedLight = Color(0xFFD32F2F)
val ErrorRedDark = Color(0xFFEF5350)
val OutlineLight = Color(0xFFE4E4E7)
val OutlineDark = Color(0xFF27272A)
val TextSecondaryLight = Color(0xFF757575)
val TextSecondaryDark = Color(0xFFA1A1AA)

// Структура кольорів теми SwipeHome
data class SwipeHomeColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val neutral: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onSurface: Color,
    val error: Color,
    val onSurfaceSecondary: Color,
    val outline: Color
)

// Світла палітра
val LightColors = SwipeHomeColors(
    primary = EmeraldPrimary,
    secondary = CoralSecondary,
    tertiary = NeutralLight,
    neutral = NeutralDark,
    background = BackgroundLightColor,
    surface = NeutralLight,
    onPrimary = Color.White,
    onSurface = NeutralDark,
    error = ErrorRedLight,
    onSurfaceSecondary = TextSecondaryLight,
    outline = OutlineLight
)

val DarkColors = SwipeHomeColors(
    primary = EmeraldPrimary,
    secondary = CoralSecondary,
    tertiary = OutlineDark,
    neutral = NeutralLight,
    background = BackgroundDarkColor,
    surface = NeutralDark,
    onPrimary = Color.White,
    onSurface = NeutralLight,
    error = ErrorRedDark,
    onSurfaceSecondary = TextSecondaryDark,
    outline = OutlineDark
)

val LocalSwipeHomeColors = staticCompositionLocalOf<SwipeHomeColors> {
    error("SwipeHomeColors not found")
}
