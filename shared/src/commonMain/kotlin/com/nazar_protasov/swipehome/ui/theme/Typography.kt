package com.nazar_protasov.swipehome.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Типографіка (Шрифти)
data class SwipeHomeTypography(
    val headline: TextStyle,
    val subheadline: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
    val caption: TextStyle
)

val AppTypography = SwipeHomeTypography(
    headline = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    ),
    subheadline = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    body = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
    label = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
    ),
    caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
    )
)

val LocalSwipeHomeTypography = staticCompositionLocalOf<SwipeHomeTypography> {
    error("SwipeHomeTypography not found")
}
