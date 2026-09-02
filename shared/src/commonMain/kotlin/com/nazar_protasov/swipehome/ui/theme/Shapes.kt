package com.nazar_protasov.swipehome.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Форми (Заокруглення, які на кнопках і картинках)
data class SwipeHomeShapes(
    val small: Dp = 8.dp,   // Для кнопок, бейджиків
    val medium: Dp = 16.dp, // Для карток об'єктів
    val large: Dp = 24.dp,  // Для плаваючих панелей або BottomSheet
    val verySmallShape: CornerBasedShape = RoundedCornerShape(4.dp),
    val smallShape: CornerBasedShape = RoundedCornerShape(8.dp),
    val mediumShape: CornerBasedShape = RoundedCornerShape(16.dp),
    val largeShape: CornerBasedShape = RoundedCornerShape(24.dp)
)

val AppShapes = SwipeHomeShapes()

val LocalSwipeHomeShapes = staticCompositionLocalOf<SwipeHomeShapes> {
    error("SwipeHomeShapes not found")
}
