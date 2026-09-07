package com.nazar_protasov.swipehome.ui.screens.settings.analystics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_single_down
import mymultiplatformproject.shared.generated.resources.ic_close
import mymultiplatformproject.shared.generated.resources.ic_eye_visible
import mymultiplatformproject.shared.generated.resources.ic_heart_border
import mymultiplatformproject.shared.generated.resources.ic_lightning
import mymultiplatformproject.shared.generated.resources.ic_support
import org.jetbrains.compose.resources.painterResource

@Composable
fun AnalyticsTopBar(navigator: Navigator){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SwipeHomeTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = { navigator.pop() }
        ) {
            Icon(
                painterResource(Res.drawable.ic_arrow_back),
                contentDescription = "Back"
            )
        }
        Text(
            "Аналітика",
            style = SwipeHomeTheme.typography.headline,
            color = SwipeHomeTheme.colors.neutral
        )

        Spacer(Modifier.width(36.dp))
    }
}
@Composable
fun PropertyHeroSection(){
    Column{
        Text("ВАШ ОБ'ЄКТ", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.primary, modifier = Modifier.padding(bottom = 8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ){
            Box(modifier = Modifier.fillMaxSize()){
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1502672260266-1c1e52409818?q=80&w=600",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Градієнт
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    SwipeHomeTheme.colors.neutral,
                                    SwipeHomeTheme.colors.neutral.copy(alpha = 0.8f)
                                ),
                                startY = 150f
                            )
                        )
                )

                // Бейдж
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SwipeHomeTheme.colors.primary)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ){
                    Text("Активно", style = SwipeHomeTheme.typography.body, color = SwipeHomeTheme.colors.onPrimary)
                }

                // Текст знизу
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)){
                    Text("Люблін, Вєнява", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.neutral)
                    Text("2 кімн. • 65м²", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
fun StatsGridSection(){
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
               StatCard(
                   modifier = Modifier.weight(1f),
                   icon = painterResource(Res.drawable.ic_eye_visible),
                   title = "Загальна кількість...",
                iconColor = SwipeHomeTheme.colors.primary,
                   value = "1,245",
                   trend = "+12%" ,
                   isPositive = true
               )
                StatCard(
                   modifier = Modifier.weight(1f),
                   icon = painterResource(Res.drawable.ic_heart_border),
                   title = "Зацікавленість...",
                   iconColor = SwipeHomeTheme.colors.error,
                   value = "312",
                   trend = "+5%" ,
                   isPositive = true
               )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
               StatCard(
                    modifier = Modifier.weight(1f),
                    icon = painterResource(Res.drawable.ic_close),
                    title = "Відмови (свайпи...",
                    iconColor = SwipeHomeTheme.colors.primary,
                    value = "840",
                    trend = "-2%" ,
                    isPositive = true
               )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = painterResource(Res.drawable.ic_support),
                    title = "Конверсія у контакти...",
                    iconColor = SwipeHomeTheme.colors.primary,
                    value = "15",
                    trend = "4.8%" ,
                    isPositive = true,
                    subtitle = "повідомлень"
               )
            }
        }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: Painter,
    iconColor: Color,
    title: String,
    value: String,
    trend: String?,
    isPositive: Boolean,
    subtitle: String? = null,
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.background),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top){
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                if(trend != null){
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if(isPositive) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.error)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ){
                        Text(trend, style = SwipeHomeTheme.typography.caption, color = if(isPositive) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurface, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    value,
                    style = SwipeHomeTheme.typography.headline,
                    color = SwipeHomeTheme.colors.neutral
                )
                if (subtitle != null) {
                    Text(
                        subtitle,
                        style = SwipeHomeTheme.typography.caption,
                        color = SwipeHomeTheme.colors.onSurfaceSecondary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun ChartSection(){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.background),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)){
            // Рядок заголовка (Текст + Кнопка)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Динаміка\nзацікавленості",
                    style = SwipeHomeTheme.typography.body,
                    color = SwipeHomeTheme.colors.primary
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SwipeHomeTheme.colors.surface)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row {
                        Text(
                            "Останні 7 днів",
                            style = SwipeHomeTheme.typography.caption,
                            color = SwipeHomeTheme.colors.onSurface
                        )
                        Icon(
                            painterResource(Res.drawable.ic_arrow_single_down),
                            contentDescription = null,
                            tint = SwipeHomeTheme.colors.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

                Spacer(modifier = Modifier.width(24.dp))

                // Стовпчиковий графік
                val data = listOf(0.4f, 0.6f, 0.3f, 1.0f, 0.5f, 0.7f, 0.45f)
                val labels = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Нд")

                Row(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ){
                    data.forEachIndexed { _, fraction ->
                        val isPeak = fraction == 1.0f
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)){
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .fillMaxHeight(fraction)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 4.dp))
                                    .background(if(isPeak) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    labels.forEachIndexed { index, label ->
                        val isPeak = data[index] == 1.0f
                        Text(
                            label,
                            style = SwipeHomeTheme.typography.caption,
                            color = if (isPeak) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.onSurfaceSecondary,
                            fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

        }
    }
}

@Composable
fun ReactionRatioSection(){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.background),
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Співвідношення реакцій", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.primary)

            Spacer(modifier = Modifier.height(24.dp))

            // Кільцевий графік
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
                contentAlignment = Alignment.Center
            ){
                Canvas(modifier = Modifier.size(120.dp)){
                    val strokeWidth = 20.dp.toPx()

                    // Відхилення (сірий, 73%)
                    drawArc(
                        color = Color(0xFFE0E0E0),
                        startAngle = 270f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Лайки (червоний, 27%)
                    drawArc(
                        color = Color(0xFFD32F2F),
                        startAngle = 270f,
                        sweepAngle = 97.2f, // 27% від 360 градусів
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("27%", style = SwipeHomeTheme.typography.headline, color = SwipeHomeTheme.colors.primary)
                    Text("Match", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Легенда
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(SwipeHomeTheme.colors.background))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Свайпи вправо", style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                    }
                    Text("312", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(SwipeHomeTheme.colors.background))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Свайпи вліво", style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                    }
                    Text("840", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                }
            }
        }
    }
}

@Composable
fun InsightsSection(){
    Column {
        Text("Інсайти активності",
            style = SwipeHomeTheme.typography.subheadline,
            color = SwipeHomeTheme.colors.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.background),
            elevation = CardDefaults.cardElevation(2.dp)
        ){
            Column(modifier = Modifier.padding(16.dp)){
                // Текстовий блок
                Column {
                    Text("Коли ваше оголошення переглядають найчастіше", style = SwipeHomeTheme.typography.body, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Оптимальний час для оновлення фото чи ціни.", style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                }

                // Відступ між текстом і картками
                Spacer(modifier = Modifier.height(16.dp))

                // Ряд з картками
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
                    InsightTimeCard(title = "Будні дні", time = "19:00 - 20:00", modifier = Modifier.weight(1f))
                    InsightTimeCard(title = "Вихідні", time = "11:00 - 15:00", modifier = Modifier.weight(1f))
                    InsightTimeCard(title = "Пік: четвер", time = "20:00", isHighlight = true, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun InsightTimeCard(title: String, time: String, isHighlight: Boolean = false, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if(isHighlight) SwipeHomeTheme.colors.background else SwipeHomeTheme.colors.surface)
            .border(1.dp, if(isHighlight) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isHighlight) {
                Icon(
                    painter = painterResource(Res.drawable.ic_lightning),
                    contentDescription = null,
                    tint = SwipeHomeTheme.colors.primary,
                    modifier = Modifier.size(12.dp)
                )
            }
            Text(title, style = SwipeHomeTheme.typography.caption, color = if(isHighlight) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.onSurfaceSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(time, style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.primary, textAlign = TextAlign.Center)
        }
    }
}
