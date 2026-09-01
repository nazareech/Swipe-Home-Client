package com.nazar_protasov.swipehome.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.models.Property
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_heart
import mymultiplatformproject.shared.generated.resources.ic_location_pin
import org.jetbrains.compose.resources.painterResource

@Composable
fun StatisticsSection(){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Text("СТАТИСТИКА ПОШУКУ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem("150", "Переглянуто", true, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatItem("12", "Вподобано", false, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatItem("138", "Відхилено", false, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String, isHighlighted: Boolean, modifier: Modifier = Modifier){
    val bgColor = if (isHighlighted) Color(0xFFB2DFDB) else Color(0xFFF5F5F5)
    val textColor = if (isHighlighted) Color(0xFF00695C) else Color.Black

    Box(modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .background(bgColor)
        .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(label, color = if(isHighlighted) textColor else Color.Gray , fontSize = 11.sp)
        }
    }
}

@Composable
fun BadgeChip(text: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ){
        Text(text, color = if (isSelected) Color.White else Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun HubGridCard(
    property: Property,
    isCompareMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF00695C) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = property.imagesUrl,
                contentDescription = "Фото нерухомості",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            // Градієнт для читабельності тексту
            Box(
                modifier = Modifier.fillMaxSize().background(
                    verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 100f
                    )
                )
            )

            // Чекбокс і бейдж "Нове"
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    if (isCompareMode) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onClick() },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF00695C), uncheckedColor = Color.White)
                        )
                    }
                    if(property.isNew == true){ // Припускаємо, що додано поле
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ){
                            Text("Нове", color = Color(0xFF00695C), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Сердечко
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painterResource(Res.drawable.ic_heart), contentDescription = "Збережене", tint = Color.Red)
                }
            }

            // Текст ціни та локалізації
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ){
              Text(property.price, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painterResource(Res.drawable.ic_location_pin), contentDescription = null, modifier = Modifier.size(10.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${ property.buildingType }, ${ property.location }", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CompareFloatingBar(
    selectedCount: Int,
    selectedProperties: List<Property>,
    onCompareClick: () -> Unit
){
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            selectedProperties.take(3).forEach { property ->
                AsyncImage(
                    model = property.imagesUrl,
                    contentDescription = "Фото нерухомості",
                    modifier = Modifier.size(40.dp).clip(CircleShape).border(2.dp, Color.White, CircleShape)
                )
            }
        }

        Button(
            onClick = onCompareClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
            shape = RoundedCornerShape(8.dp),
        ){
            Text("Порівняти ($selectedCount) об'єкти")
        }
    }
}

@Composable
fun ComparisonTableContent(selectedProperties: List<Property>){
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        // Заголовок таблиці
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(
                "Характеристика",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1.5f)
            )
            selectedProperties.fastForEachIndexed { index, _ ->
                Text(
                    "Об'єкт ${index + 1}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF00695C)
                )
            }
        }
        Divider(color = Color.LightGray)

        // Рядки таблиці
        ComparisonRow("Тип", selectedProperties.map { it.buildingType })
        ComparisonRow("Ціна", selectedProperties.map { it.price })
        ComparisonRow("Локалізація", selectedProperties.map { it.location })
        ComparisonRow("Площа", selectedProperties.map { it.area.toString() })

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ComparisonRow(label: String, values: List<String>){
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)){
        Text(label, modifier = Modifier.weight(1.5f), color = Color.DarkGray, fontSize = 14.sp)
        values.forEach { value ->
            Text(value, modifier = Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
    Divider(color = Color.Gray, thickness = 0.5.dp)
}