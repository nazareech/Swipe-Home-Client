package com.nazar_protasov.swipehome.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

@Composable
fun SwipeableCard(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    val coroutineScope = rememberCoroutineScope()

    // Анімовані значення зсуву по осях X та Y
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .graphicsLayer{
                translationX = offsetX.value
                translationY = offsetY.value
                // Обертання: чим далі тягнемо по Х, тим сильніше картинка нахиляється
                // Ділимо на 20f, щоб нахил був плавним і реалістичним
                rotationZ = offsetX.value / 15f
            }
            .pointerInput(Unit){
                detectDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            // Визначаємо поріг для успішного свайпу (третина ширини екрана)
                            val thershold = size.width / 3f

                            if(offsetX.value > thershold){
                                // Свайп вправо (Лайк) -> Анімуємо виліт далеко за правий край
                                offsetX.animateTo(size.width.toFloat() * 2)
                                onSwipeRight()
                            } else if (offsetX.value < -thershold){
                                // Свайп вліво (Дисайк) -> Анімуємо виліт далеко за лівий край
                                offsetX.animateTo(-size.width.toFloat() * 2)
                                onSwipeLeft()
                            } else {
                                // Не дотягнули -> Плавне повернення в центр
                                launch { offsetX.animateTo(0f) }
                                launch { offsetY.animateTo(0f) }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume() // Кажемо системі, що ми зробили цей жест
                        coroutineScope.launch {
                            // Миттєво оновлюємо координати слідом за пальцем (без анімації)
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    }
                )
            }
    ){
     content() // Тут буде наша картка PropertyCard
    }
}