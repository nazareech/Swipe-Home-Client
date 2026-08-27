package com.nazar_protasov.swipehome.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.models.Property
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_location_pin
import mymultiplatformproject.shared.generated.resources.search_card_rooms_count
import mymultiplatformproject.shared.generated.resources.search_card_section_area
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PropertyCard(
    property: Property,
    modifier: Modifier = Modifier
){
    // Стан для відстеження поточної фотографії
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val imagesCount = property.imagesUrl.size

    Card(
        modifier = modifier.fillMaxWidth().fillMaxHeight(0.95f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Box(modifier = Modifier.fillMaxSize()){
            // Фотографія на весь фон картки
            AsyncImage(
                property.imagesUrl.getOrNull(currentImageIndex),
                contentDescription = "Фото нерухомості",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                onError = { error ->
                    // Виведемо помилку в консоль (Logcat)
                    println("COIL ERROR: ${error.result.throwable.message}")
                    error.result.throwable.printStackTrace()
                }
            )

            // Зони для кліку (ліва та права половина екрана)
            Row(modifier = Modifier.fillMaxSize()){
                // Ліва частина (попереднє фото)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ){
                            if (currentImageIndex > 0){
                                currentImageIndex--
                            }
                        }
                )
                // Права частина (наступне фото)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ){
                            if (currentImageIndex < imagesCount - 1){
                                currentImageIndex++
                            }
                        }
                )
            }

            // Затемнення верху картки
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )
                    )
                            .align(Alignment.TopCenter)
            )

            // Білі лінії індикації
            if (imagesCount > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    for (i in 0 until imagesCount) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (i == currentImageIndex) Color.White
                                    else Color.White.copy(alpha = 0.4f)
                                )
                        )
                    }
                }
            }

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
                    val detailsText = "${property.rooms} " + stringResource(Res.string.search_card_rooms_count) +
                            " • ${property.area} " + stringResource(Res.string.search_card_section_area) +
                            " • ${property.buildingType}"
                    Text(
                        text = detailsText,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}