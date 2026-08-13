package com.nazar_protasov.swipehome.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Налаштувуємо палітру для світлої теми
private val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White, // Колір тексту на головних зелених кнопках

    secondary = SecondaryGreen,
    onSecondary = TextPrimary, // Колір тексту на второстепенних зелених кнопках

    background = BackgroundLight,
    onBackground = TextPrimary, // Колір тексту на загальному фоні

    surface = SurfaceWhite, // Колір фону карток
    onSurface = TextPrimary, // Колір тексту на картах

    error = ErrorRed, // Колір для помилок
    onError = Color.White // Колір тексту помилок
)

@Composable
fun SwipeHomeTheme(content: @Composable () -> Unit) {
    // Передаємо нашу палітру в стандартну MaterialTheme
    MaterialTheme(
        colorScheme = LightColors,
        // Пізніше ми зможемо додати сюди кастомні шрифти (typography)
        content = content
        )
}