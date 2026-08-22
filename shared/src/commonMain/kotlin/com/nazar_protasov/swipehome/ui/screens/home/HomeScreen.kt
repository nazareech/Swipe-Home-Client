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
import cafe.adriel.voyager.core.screen.Screen
import com.nazar_protasov.swipehome.ui.components.PropertyCard
import com.nazar_protasov.swipehome.ui.components.SwipeableCard
import com.nazar_protasov.swipehome.ui.models.Property

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = { HomeTopBar() },
            bottomBar = { HomeBottomActionButtons() },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            // Тимчасовий список для тестування (імітація відповіді бекенду)
            var properties by remember {
                mutableStateOf(
                    listOf(
                        Property("1", "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?q=80&w=1000", "$120,000", "Халупа з видом на озеро", "Люблін", "4 кімнати - 120м"),
                        Property("2", "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?q=80&w=1000", "$350,000", "Сучасна Вілла", "Варшава", "5 кімнат - 200м"),
                        Property("3", "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?q=80&w=1000", "$85,000", "Затишна квартира", "Краків", "2 кімнати - 45м")
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
                                        .scale(95f)
                                        .padding(vertical = 16.dp))
                            }
                        }
                    }
                }

            }
        }
    }
}