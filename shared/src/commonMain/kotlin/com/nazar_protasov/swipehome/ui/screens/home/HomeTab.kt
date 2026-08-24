package com.nazar_protasov.swipehome.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .build()
        }

        Scaffold(
            topBar = { HomeTopBar() },
            bottomBar = { HomeBottomActionButtons() },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
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

            // Центральна зона для карток
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if(properties.isEmpty()){
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
                            if(isTopCard){
                                // Активна картка (можна свайпати)
                                // Свайп обгортка
                                SwipeableCard(
                                    onSwipeLeft = {
                                        println("Swipe Left (Dislike)")
                                        properties = properties.drop(1)
                                        /*TODO*/
                                    },
                                    onSwipeRight = {
                                        println("Swipe Right (Like)")
                                        properties = properties.drop(1)
                                        /*TODO*/
                                    }
                                ){
                                    PropertyCard( property = property)
                                }
                            }else{
                                PropertyCard(
                                    property = property,
                                    modifier = Modifier
                                        .scale(0.95f)
                                        .padding(vertical = 16.dp))
                            }
                        }
                    }
                }

            }
        }
    }
}