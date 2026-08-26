package com.nazar_protasov.swipehome.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetDefaults.properties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.nazar_protasov.swipehome.ui.components.PropertyCard
import com.nazar_protasov.swipehome.ui.components.SwipeableCard
import com.nazar_protasov.swipehome.ui.models.Property
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_search_cards
import mymultiplatformproject.shared.generated.resources.main_bottom_nav_search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory // Або ktor2, залежно від того, що ви обрали
import com.nazar_protasov.swipehome.ui.components.rememberSwipeCardState
import kotlinx.coroutines.launch
import kotlin.math.abs

object HomeTab: Tab {
    override val options: TabOptions
    @Composable
    get() {
        val title = stringResource(Res.string.main_bottom_nav_search)
        val icon = painterResource(Res.drawable.ic_search_cards)
        return remember {
            TabOptions(
                index = 0u,
                title = title,
                icon = icon
            )
        }
    }

    @Composable
    override fun Content() {
        Navigator(HomeScreen)
    }
}

internal object HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // Стан та скоуп для анімації
        val swipeCardState = rememberSwipeCardState()
        val coroutineScope = rememberCoroutineScope()

        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .build()
        }

        // Тимчасовий список для тестування (імітація відповіді бекенду)
        var properties by remember {
            mutableStateOf(
                listOf(
                    Property("1", "http://192.168.0.78:8080/uploads/application-number-1/687a274c-b193-4b84-918e-23c054d17bc5.jpg", "$120,000", "Халупа з видом на озеро", "Люблін", "4 кімнати - 120м"),
                    Property("2", "http://192.168.0.78:8080/uploads/application-number-1/5239453d-88ab-48bc-b0ae-8b8d8e41cb36.jpg", "$350,000", "Сучасна Вілла", "Варшава", "5 кімнат - 200м"),
                    Property("3", "http://192.168.0.78:8080/uploads/application-number-1/bd3e357c-f613-42d3-b420-9d0bc285fcd1.jpg", "$85,000", "Затишна квартира", "Краків", "2 кімнати - 45м")
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Верхня панель
            HomeTopBar()

            // Центральна зона для карток
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (properties.isEmpty()) {
                    // Стан, коли картки закінчилися
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎉", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ви переглянули всі пропозиції!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    // Беремо максимум 2 верхні картки
                    // reversed() потрібен, щоб перша картка малювалася ОСТАННЬОЮ (поверх усіх інших у Box)
                    val visibleCards = properties.take(2).reversed()

                    visibleCards.forEach { property ->
                        val isTopCard = property.id == properties.first().id

                        // key допомагає Comppose розуміти, яка саме картка видалилася, щоб не забивати анімації
                        key(property.id) {
                            if (isTopCard) {
                                // Активна картка (можна свайпати)
                                // Свайп обгортка
                                SwipeableCard(
                                    state = swipeCardState,
                                    onSwipeLeft = {
                                        coroutineScope.launch {
                                            swipeCardState.swipeLeft()
                                            properties = properties.drop(1)
                                            swipeCardState.snapToCenter()
                                        }
                                    },
                                    onSwipeRight = {
                                        coroutineScope.launch {
                                            swipeCardState.swipeRight()
                                            properties = properties.drop(1)
                                            swipeCardState.snapToCenter()
                                        }
                                    }
                                ) {
                                    PropertyCard(property = property)
                                }
                            } else {
                                PropertyCard(
                                    property = property,
                                    modifier = Modifier
                                        .graphicsLayer {
                                            // Беремо значення зсуву по модулю (щоб працювало і вліво, і вправо)
                                            // Ділимо на 400f (приблизна дистанція свайпу), щоб отримати прогрес від 0 до 1
                                            val progress = ( abs(swipeCardState.offsetX.value) / 400f).coerceIn(0f, 1f)

                                            // Рахуємо масштаб: починаємо з 0.95 і плавно до 1.0
                                            val animationScale = 0.95f + (0.05f * progress)

                                            scaleX = animationScale
                                            scaleY = animationScale
                                            alpha = 0.95f + (0.05f * progress)
                                        }
//                                        .padding(vertical = 16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Нижні кнопки дій (автоматично притиснуться до низу над Bottom Navigation)
            HomeBottomActionButtons(
                onDislikeClick = {
                    if (properties.isNotEmpty()) {
                        coroutineScope.launch {
                            swipeCardState.swipeLeft()
                            properties = properties.drop(1)
                            swipeCardState.snapToCenter()
                        }
                    }
                },
                onLikeClick = {
                    if (properties.isNotEmpty()) {
                        coroutineScope.launch {
                            swipeCardState.swipeRight()
                            properties = properties.drop(1)
                            swipeCardState.snapToCenter()
                        }
                    }
                }
            )
        }
    }
}
