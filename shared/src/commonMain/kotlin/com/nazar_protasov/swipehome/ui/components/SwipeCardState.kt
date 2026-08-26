package com.nazar_protasov.swipehome.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SwipeCardState {
    val offsetX = Animatable(0f)
    val offsetY = Animatable(0f)

    val targetValue = 3000f // Виліт на 3000 пікселів
    val duration = 500      // Час вильоту

    suspend fun swipeRight(){
        // Анімуємо виліт далеко вправо (на 3000 пікселів) за 300 мілісекунди
        offsetX.animateTo(targetValue, animationSpec = tween(duration))
    }

    suspend fun swipeLeft(){
        // Анімуємо виліт далеко вправо (на 3000 пікселів) за 300 мілісекунди
        offsetX.animateTo(-targetValue, animationSpec = tween(duration))
    }

    suspend fun reset() = coroutineScope{
        // Повертаємося назад до початкового стану
        launch { offsetX.animateTo(0f) }
        launch { offsetY.animateTo(0f) }
    }

    suspend fun snapToCenter(){
        // Миттєво повертаємось назад до початкового стану
        offsetX.snapTo(0f)
        offsetY.snapTo(0f)
    }
}

@Composable
fun rememberSwipeCardState(): SwipeCardState {
    return remember { SwipeCardState() }
}