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

// Головна обгортка
@Composable
fun SwipeHomeTheme(
    // Параметр який буде визначенням світлової теми
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    typography: SwipeHomeTypography = AppTypography,
    shapes: SwipeHomeShapes = AppShapes,
    content: @Composable () -> Unit
) {
    // Визначаємо яку палітру використовувати
    val colors = if (useDarkTheme) DarkColors else LightColors

    val materialColorScheme = if (useDarkTheme) {
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
        LocalSwipeHomeShapes provides shapes
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = materialTypography,
            shapes = materialShapes,
            content = content
        )
    }
}
