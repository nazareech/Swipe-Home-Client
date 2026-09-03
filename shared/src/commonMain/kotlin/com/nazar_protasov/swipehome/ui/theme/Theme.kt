package com.nazar_protasov.swipehome.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

// Об'єкт доступу
object SwipeHomeTheme {
    val colors: SwipeHomeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSwipeHomeColors.current

    val typography: SwipeHomeTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalSwipeHomeTypography.current

    val shapes: SwipeHomeShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalSwipeHomeShapes.current
}

// Ключ для читання стану теми
val LocalThemeIsDark = staticCompositionLocalOf<Boolean> {
    error("No ThemeIsDark provided")
}
// Ключ для зміни теми
val LocalThemeToggle = staticCompositionLocalOf<()-> Unit> {
    error("No ThemeToggle provided")
}

// Головна обгортка
@Composable
fun SwipeHomeTheme(
    // Параметр який буде визначенням світлової теми
    systemIsDark: Boolean = isSystemInDarkTheme(),
    typography: SwipeHomeTypography = AppTypography,
    shapes: SwipeHomeShapes = AppShapes,
    content: @Composable () -> Unit
) {
    // Стан який може змінюватись
    var isDarkTheme by remember { mutableStateOf(systemIsDark) }

    // Визначаємо яку палітру використовувати
    val colors = if (isDarkTheme) DarkColors else LightColors

    val materialColorScheme = if (isDarkTheme) {
        darkColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            secondary = colors.secondary,
            tertiary = colors.tertiary,
            background = colors.background,
            onBackground = colors.neutral,
            surface = colors.surface,
            onSurface = colors.onSurface,
            error = colors.error,
            outline = colors.outline,
            surfaceVariant = colors.surface,
            onSurfaceVariant = colors.onSurfaceSecondary
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            secondary = colors.secondary,
            tertiary = colors.tertiary,
            background = colors.background,
            onBackground = colors.neutral,
            surface = colors.surface,
            onSurface = colors.onSurface,
            error = colors.error,
            outline = colors.outline,
            surfaceVariant = colors.surface,
            onSurfaceVariant = colors.onSurfaceSecondary
        )
    }

    val materialTypography = Typography(
        headlineLarge = typography.headline,
        headlineMedium = typography.headline,
        titleMedium = typography.subheadline,
        bodyLarge = typography.body,
        bodyMedium = typography.label,
        labelMedium = typography.caption
    )

    val materialShapes = Shapes(
        small = shapes.smallShape,
        medium = shapes.mediumShape,
        large = shapes.largeShape
    )

    CompositionLocalProvider(
        LocalSwipeHomeColors provides colors,
        LocalSwipeHomeTypography provides typography,
        LocalSwipeHomeShapes provides shapes,

        // Передаємо стан та функцію перемикання вниз по дереву
        LocalThemeIsDark provides isDarkTheme,
        LocalThemeToggle provides { isDarkTheme = !isDarkTheme },
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = materialTypography,
            shapes = materialShapes,
            content = content
        )
    }
}
