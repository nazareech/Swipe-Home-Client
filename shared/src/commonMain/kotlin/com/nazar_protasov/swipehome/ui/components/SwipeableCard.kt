package com.nazar_protasov.swipehome.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

@Composable
fun SwipeableCard(
    state: SwipeCardState, // Параметр стану
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .graphicsLayer{
                translationX = state.offsetX.value
                translationY = state.offsetY.value
                // Обертання: чим далі тягнемо по Х, тим сильніше картинка нахиляється
                // Ділимо на 20f, щоб нахил був плавним і реалістичним
                rotationZ = state.offsetX.value / 15f
            }
            .pointerInput(Unit){
                detectDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            // Визначаємо поріг для успішного свайпу (третина ширини екрана)
                            val threshold = size.width / 3f

                            if(state.offsetX.value > threshold){
                                // Свайп вправо (Лайк) -> Анімуємо виліт далеко за правий край
                                onSwipeRight()
                            } else if (state.offsetX.value < -threshold){
                                // Свайп вліво (Дислайк) -> Анімуємо виліт далеко за лівий край
                                onSwipeLeft()
                            } else {
                                // Не дотягнули -> Плавне повернення в центр
                                launch { state.reset() }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume() // Кажемо системі, що ми зробили цей жест
                        coroutineScope.launch {
                            // Миттєво оновлюємо координати слідом за пальцем (без анімації)
                            state.offsetX.snapTo(state.offsetX.value + dragAmount.x)
                            state.offsetY.snapTo(state.offsetY.value + dragAmount.y)
                        }
                    }
                )
            }
    ){
     content() // Тут буде наша картка PropertyCard
    }
}