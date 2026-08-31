package com.nazar_protasov.swipehome.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.nazar_protasov.swipehome.ui.components.PropertyCard
import com.nazar_protasov.swipehome.ui.components.SwipeableCard
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_search_cards
import mymultiplatformproject.shared.generated.resources.main_bottom_nav_search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.nazar_protasov.swipehome.network.toUIDetailsProperty
import com.nazar_protasov.swipehome.ui.components.rememberSwipeCardState
import com.nazar_protasov.swipehome.ui.screens.details.PropertyDetailsScreen
import com.nazar_protasov.swipehome.network.toUIProperty
import kotlinx.coroutines.launch
import mymultiplatformproject.shared.generated.resources.ic_refresh
import org.koin.compose.koinInject
import kotlin.math.abs

class HomeTab : Tab {
    override val key = uniqueScreenKey

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
        val homeScreen = remember { HomeScreen() }
        Navigator(homeScreen)
    }
}

internal class HomeScreen : Screen {
    override val key = uniqueScreenKey

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // Стан та скоуп для анімації
        val swipeCardState = rememberSwipeCardState()
        val coroutineScope = rememberCoroutineScope()

        // Отримуємо синглтон ScreenModel з Koin
        val screenModel: HomeScreenModel = koinInject()

        val navigator = LocalNavigator.currentOrThrow

        // Шукаємо кореневий навігатор, щоб деталі відкривалися на весь екран (поверх BottomBar)
        // і щоб уникнути помилки ClassCastException в TabNavigator
        var rootNavigator = navigator
        while (rootNavigator.parent != null) {
            rootNavigator = rootNavigator.parent!!
        }

        // Спостерігаємо за станами (дані для сервера)
        val properties by screenModel.properties.collectAsState()
        val isLoading by screenModel.isLoading.collectAsState()

        // --- Налаштування Coil
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .build()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Верхня панель
            HomeTopBar(screenModel)

            // Центральна зона для карток
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // ПОКАЗУЄМО ІНДИКАТОР ЗАВАНТАЖЕННЯ
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                // АБО ПОКАЗУЄМО ПОРОЖНІЙ СТАН
                else if (properties.isEmpty()) {
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Спробуйте змінити фільтри або оновити список",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        androidx.compose.foundation.layout.Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    screenModel.fetchProperties()
                                }
                            ){
                                Icon(painterResource(Res.drawable.ic_refresh), contentDescription = "Оновити")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            androidx.compose.material3.TextButton(
                                onClick = {
                                    rootNavigator.push(FilterScreen(screenModel))
                                }
                            ) {
                                Text("Змінити фільтри", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                // АБО МАЛЮЄМО СТОПКУ КАРТИНОК
                } else {
                    // Беремо максимум 2 верхні картки
                    // reversed() потрібен, щоб перша картка малювалася ОСТАННЬОЮ (поверх усіх інших у Box)
                    val visibleCards = properties.take(2).reversed()

                    visibleCards.forEach { property ->
                        val isTopCard = property.id_property == properties.first().id_property

                        // key допомагає Compose розуміти, яка саме картка видалилася, щоб не забивати анімації
                        key(property.id_property) {
                            if (isTopCard) {
                                // Активна картка (можна свайпати)
                                // Свайп обгортка
                                SwipeableCard(
                                    state = swipeCardState,
                                    onSwipeLeft = {
                                        coroutineScope.launch {
                                            swipeCardState.swipeLeft()
                                            screenModel.onCardSwiped()
                                            swipeCardState.snapToCenter()
                                        }
                                    },
                                    onSwipeRight = {
                                        coroutineScope.launch {
                                            swipeCardState.swipeRight()
                                            screenModel.onCardSwiped()
                                            swipeCardState.snapToCenter()
                                        }
                                    }
                                ) {
                                    // Додали клік
                                    PropertyCard(
                                        property = property.toUIProperty(),
                                    )
                                }
                            } else {
                                PropertyCard(
                                    property = property.toUIProperty(),
                                    modifier = Modifier
                                        .graphicsLayer {
                                            // Беремо значення зсуву по модулю (щоб працювало і вліво, і вправо)
                                            // Ділимо на 400f (приблизна дистанція свайпу), щоб отримати прогрес від 0 до 1
                                            val progress = ( abs(swipeCardState.offsetX.value) / 400f).coerceIn(0f, 1f)

                                            // Рахуємо масштаб: починаємо з 0.95 і плавно до 1.0
                                            val animationScale = 0.95f + (0.05f * progress)

                                            scaleX = animationScale
                                            scaleY = animationScale
                                            alpha = 0.5f + (0.5f * progress)
                                        }
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
                            screenModel.onCardSwiped()
                            swipeCardState.snapToCenter()
                        }
                    }
                },
                // Кнопка детальної інформації про оголошення
                onDetailsClick = {
                    if (properties.isNotEmpty()) {
                        rootNavigator.push(PropertyDetailsScreen(properties.first().toUIDetailsProperty()))
                    }
                },
                onLikeClick = {
                    if (properties.isNotEmpty()) {
                        coroutineScope.launch {
                            swipeCardState.swipeRight()
                            screenModel.onCardSwiped()
                            swipeCardState.snapToCenter()
                        }
                    }
                }
            )
        }
    }
}
