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
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.launch

@Composable
fun SwipeableCard(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit

){
    val coroutineScope = rememberCoroutineScope()
    // Отримуємо density для налаштування перспективи камери при 3D-обертанні
    // Цей параметр віддаляє віртуальну "камеру", роблячи перекручування реалістичним
    val density = LocalDensity.current.density

    // Анімовані значення зсуву по осях X та Y
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .graphicsLayer{
                translationX = offsetX.value
                translationY = offsetY.value

                // Головна змінна для перекручування навколо осі Y (3D-ефект фліпу)
                //Значення 8f можна змінити: чим воно менше, тим швидше/сильніше картка перекручуватиметься.
                rotationY = -offsetX.value / 8f

                // Легкий нахил по осі Z
                rotationZ = offsetX.value / 10f

                // Налаштування перспективи, щоб 3D-обертання не спотворювало картку
                cameraDistance = 12f * density
            }
            .pointerInput(Unit){
                detectDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            // Визначаємо поріг для успішного свайпу (1/4 ширини екрана)
                            val threshold = size.width / 4f

                            if(offsetX.value > threshold){
                                // Свайп вправо (Лайк) -> Анімуємо виліт далеко за правий край
                                launch { offsetX.animateTo(size.width.toFloat() * 2)}
                                onSwipeRight()
                            } else if (offsetX.value < -threshold){
                                // Свайп вліво (Дисайк) -> Анімуємо виліт далеко за лівий край
                                launch { offsetX.animateTo(-size.width.toFloat() * 2)}
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