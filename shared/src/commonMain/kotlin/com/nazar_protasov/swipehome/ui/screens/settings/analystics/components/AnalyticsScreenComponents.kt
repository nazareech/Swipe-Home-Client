package com.nazar_protasov.swipehome.ui.screens.settings.analystics.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.screens.settings.listing.components.ListingStatus
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import kotlinx.coroutines.launch
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_single_down
import mymultiplatformproject.shared.generated.resources.ic_close
import mymultiplatformproject.shared.generated.resources.ic_eye_visible
import mymultiplatformproject.shared.generated.resources.ic_heart_border
import mymultiplatformproject.shared.generated.resources.ic_lightning
import mymultiplatformproject.shared.generated.resources.ic_support
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun AnalyticsTopBar(navigator: Navigator){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SwipeHomeTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(top = 18.dp),
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
fun PropertyHeroSection(listOfPhotos: List<String>, address: String, countOfRooms: Int, area: String, status: ListingStatus){
    // Стани для відстеження поточної фотографії
    val images = listOfPhotos
    val imagesCount = images.size

    // PagerState = HorizontalPager
    val pagerState = rememberPagerState(pageCount = { imagesCount })
    val coroutineScope = rememberCoroutineScope()

    // Стан для повноекранного режиму
    var isFullScreen by remember { mutableStateOf(false) }

    Column{
        Text("ВАШ ОБ'ЄКТ", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.primary, modifier = Modifier.padding(bottom = 8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ){
            Box(Modifier.fillMaxSize()) {
                // Зображення зі свайпом
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = listOfPhotos.getOrNull(page),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { isFullScreen = !isFullScreen },
                        contentScale = ContentScale.Crop,
                        onError = { error ->
                            println("COIL ERROR: ${error.result.throwable.message}")
                            error.result.throwable.printStackTrace()
                        }
                    )
                }

                // Білі лінії індикації
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 0 until imagesCount) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(SwipeHomeTheme.shapes.smallShape)
                                .background(
                                    if (i == pagerState.currentPage) SwipeHomeTheme.colors.primary
                                    else SwipeHomeTheme.colors.tertiary.copy(alpha = 0.4f)
                                )
                        )
                    }
                }

                // Бейдж
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SwipeHomeTheme.colors.primary)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "$status",
                        style = SwipeHomeTheme.typography.body,
                        color = SwipeHomeTheme.colors.onPrimary
                    )
                }

                // Текст знизу
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                    Text(
                        address,
                        style = SwipeHomeTheme.typography.subheadline,
                        color = SwipeHomeTheme.colors.neutral
                    )
                    Text(
                        "$countOfRooms кімн. • $area m²",
                        style = SwipeHomeTheme.typography.caption,
                        color = SwipeHomeTheme.colors.neutral.copy(alpha = 0.8f)
                    )
                }

                // --- ДІАЛОГ ДЛЯ ПОВНОЕКРАННОГО ПЕРЕГЛЯДУ ---
                if (isFullScreen) {
                    FullScreenGallery(
                        images = images,
                        initialPage = pagerState.currentPage,
                        onDismiss = { finalPage ->
                            isFullScreen = false
                            // При закритті гортаємо маленьку карусель на те фото, на якому зупинилися у фулскріні
                            coroutineScope.launch {
                                pagerState.scrollToPage(finalPage)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FullScreenGallery(
    images: List<String>,
    initialPage: Int,
    onDismiss: (Int) -> Unit // Передаємо індекс назад
){
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = {images.size})

    // Стани для вертикального зсуву (свайп вниз/вгору)
    val offsetY = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = { onDismiss(pagerState.currentPage) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false, // Займає весь екран, а не віконце
            dismissOnBackPress = true
        )
    ){
        val backgroundAlpha = (1f - (abs(offsetY.value) / 1000f)).coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SwipeHomeTheme.colors.background.copy(alpha = backgroundAlpha))
                // Обробка жестів
                .pointerInput(Unit){
                    detectVerticalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                // Якщо відтягнули більше ніж на 200 пікселів - закриваємо
                                if (abs(offsetY.value) > 200f){
                                    onDismiss(pagerState.currentPage)
                                } else {
                                    // Повертаємо на місце
                                    offsetY.animateTo(0f)
                                }
                            }
                        },
                        // Обробка переривання/скасування жесту системою
                        onDragCancel = {
                            coroutineScope.launch {
                                offsetY.animateTo(0f)
                            }
                        },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume() // Споживаємо текст
                            coroutineScope.launch {
                                // Миттєво змінюємо позицію слідом за пальцем
                                offsetY.snapTo(offsetY.value + dragAmount)
                            }
                        }
                    )
                }
        ){
            // Обгортка вмісту, яка фізично буде зміщуватись по осі Y
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, offsetY.value.roundToInt()) }
            ){
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ){ page ->
                    AsyncImage(
                        model = images.getOrNull(page),
                        contentDescription = "Повноекранне фото нерухомості",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Індикатор (1/5)
                Text(
                    text = "${pagerState.currentPage + 1}/${images.size}",
                    style = SwipeHomeTheme.typography.body,
                    color = SwipeHomeTheme.colors.neutral,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )

                // Кнопка закриття
                IconButton(
                    onClick = { onDismiss(pagerState.currentPage) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 32.dp, end = 16.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ){
                    Icon(painterResource(Res.drawable.ic_close), contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun StatsGridSection(viewsNumber: Int, likesNumber: Int, rejectsNumber: Int, goToContactsNumber: Int){
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
               StatCard(
                   modifier = Modifier.weight(1f),
                   icon = painterResource(Res.drawable.ic_eye_visible),
                   title = "Загальна кількість...",
                   iconColor = SwipeHomeTheme.colors.primary,
                   value = viewsNumber.toString(),
                   trend = "+12%" ,
                   isPositive = true
               )
                StatCard(
                   modifier = Modifier.weight(1f),
                   icon = painterResource(Res.drawable.ic_heart_border),
                   title = "Зацікавленість...",
                   iconColor = SwipeHomeTheme.colors.error,
                   value = likesNumber.toString(),
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
                    value = rejectsNumber.toString(),
                    trend = "-2%" ,
                    isPositive = true
               )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = painterResource(Res.drawable.ic_support),
                    title = "Конверсія у контакти...",
                    iconColor = SwipeHomeTheme.colors.primary,
                    value = goToContactsNumber.toString(),
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
fun ReactionRatioSection(likesNumber: Int, rejectsNumber: Int){
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
                    Text(likesNumber.toString(), style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(SwipeHomeTheme.colors.background))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Свайпи вліво", style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                    }
                    Text(rejectsNumber.toString(), style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
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
