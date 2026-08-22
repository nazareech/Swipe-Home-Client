package com.nazar_protasov.swipehome.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_dislike
import mymultiplatformproject.shared.generated.resources.ic_filtres_tune
import mymultiplatformproject.shared.generated.resources.ic_heart
import mymultiplatformproject.shared.generated.resources.ic_location_pin
import org.jetbrains.compose.resources.painterResource

data class Property(
    val id: String,
    val imageUrl: String,
    val price: String,
    val title: String,
    val location: String,
    val details: String // Наприклад: "3 кімнати • 85 м² • 5 поверх"
)

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = { HomeTopBar() },
            bottomBar = { HomeBottomActionButtons() },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            // Центральна зона для карток
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Викликаємо картку з текстовими даними
                PropertyCard(
                    property = Property(
                        id = "1",
                        imageUrl = "http://localhost:8080/uploads/application-number-1/bd3e357c-f613-42d3-b420-9d0bc285fcd1.jpg",
                        price = "$120,000",
                        title = "Чьотка халупа з видом на озеро",
                        location = "Люблін, Віенява",
                        details = "4 кімтани - 120м - 2 поверх"
                    )
                )
            }
        }
    }
}

@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .statusBarsPadding(), // Відступ від статус-бару системи
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(48.dp))

        // Логотип / Заголовок
        Text(
            text = "Swipe Home",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        // Кнопка фільтрів
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(Res.drawable.ic_filtres_tune), contentDescription = null, modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun HomeBottomActionButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, top = 16.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Кнопка Dislike (Пропустити)
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Icon(painterResource(Res.drawable.ic_dislike), contentDescription = null, modifier = Modifier.size(32.dp))
        }

        // Кнопка Like (В обране)
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Icon(painterResource(Res.drawable.ic_heart), contentDescription = null, modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun PropertyCard(
    property: Property,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Box(modifier = Modifier.fillMaxSize()){
            // Фотографія на весь фон картки
            AsyncImage(
                property.imageUrl,
                contentDescription = "Фото нерухомості",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Градієнтне затемнення знизу
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, // Зверху прозоре
                                Color.Black.copy(alpha = 0.1f), // Легке затемнення посередині
                                Color.Black.copy(alpha = 0.8f) // Темний низ
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY // Адаптується під високі карточки
                        )
                    )
            )

            // Текстові дані на картці (притиснути до низу)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    text = property.price,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = property.title,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painterResource(Res.drawable.ic_location_pin), contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White.copy(alpha = 0.9f))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = property.location,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Блок з деталями (кімнати, площа)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ){
                    Text(
                        text = property.details,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}