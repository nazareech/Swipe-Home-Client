package com.nazar_protasov.swipehome.ui.screens.details

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.models.PropertyDetails
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import kotlinx.coroutines.launch
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.Res.string
import mymultiplatformproject.shared.generated.resources.btn_back
import mymultiplatformproject.shared.generated.resources.btn_share
import mymultiplatformproject.shared.generated.resources.details_screen_description
import mymultiplatformproject.shared.generated.resources.details_screen_description_show_les
import mymultiplatformproject.shared.generated.resources.details_screen_description_show_more
import mymultiplatformproject.shared.generated.resources.details_screen_feauter_grid_area
import mymultiplatformproject.shared.generated.resources.details_screen_feauter_grid_floor
import mymultiplatformproject.shared.generated.resources.details_screen_feauter_grid_parking
import mymultiplatformproject.shared.generated.resources.details_screen_feauter_grid_rooms
import mymultiplatformproject.shared.generated.resources.details_screen_map_title
import mymultiplatformproject.shared.generated.resources.details_screen_owner_contact_section_call
import mymultiplatformproject.shared.generated.resources.details_screen_owner_contact_section_owner_phone_number
import mymultiplatformproject.shared.generated.resources.details_screen_owner_contact_section_title
import mymultiplatformproject.shared.generated.resources.details_screen_owner_contact_section_write
import mymultiplatformproject.shared.generated.resources.ic_apartment_floor
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import mymultiplatformproject.shared.generated.resources.ic_arrow_single_down
import mymultiplatformproject.shared.generated.resources.ic_arrow_single_up
import mymultiplatformproject.shared.generated.resources.ic_bedroom
import mymultiplatformproject.shared.generated.resources.ic_close
import mymultiplatformproject.shared.generated.resources.ic_home_area
import mymultiplatformproject.shared.generated.resources.ic_location_pin
import mymultiplatformproject.shared.generated.resources.ic_parking
import mymultiplatformproject.shared.generated.resources.ic_share
import mymultiplatformproject.shared.generated.resources.owner_description
import mymultiplatformproject.shared.generated.resources.search_card_section_area
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs
import kotlin.math.roundToInt

// Передаємо ID об'єкта або весь об'єкт. Для прикладу беремо ID,
// щоб потім завантажити деталі з сервера
/*TODO: Замінити передані дані на об`єкт Property*/
data class PropertyDetailsScreen(val property: PropertyDetails) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            property.localization,
                            style = SwipeHomeTheme.typography.subheadline,
                            color = SwipeHomeTheme.colors.neutral
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                painterResource(Res.drawable.ic_arrow_back),
                                contentDescription = stringResource(string.btn_back),
                                tint = SwipeHomeTheme.colors.neutral
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {/*TODO: Share*/}){
                            Icon(
                                painterResource(Res.drawable.ic_share),
                                contentDescription = stringResource(string.btn_share),
                                tint = SwipeHomeTheme.colors.neutral
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SwipeHomeTheme.colors.background
                    )
                )
            },
            containerColor = SwipeHomeTheme.colors.background
        ){ paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                HeroSection()

                Spacer(modifier = Modifier.height(16.dp))

                FeaturesGrid()

                Spacer(modifier = Modifier.height(16.dp))

                DescriptionSection()

                Spacer(modifier = Modifier.height(24.dp))

                LocationSection()

                Spacer(modifier = Modifier.height(24.dp))

                OwnerContactSection()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    @Composable
    fun HeroSection() {
        // Стан для відстеження поточної фотографії
        val images = property.imagesUrl ?: emptyList()
        val imagesCount = property.imagesUrl?.size ?: 0

        // PagerState = HorizontalPager
        val pagerState = rememberPagerState(pageCount = { imagesCount.coerceAtLeast(1) })
        val coroutineScope = rememberCoroutineScope()

        // Стан для повноекранного режиму
        var isFullScreenVisible by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            // Зображення зі свайпом
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                // Фото
                AsyncImage(
                    model = images.getOrNull(page),
                    contentDescription = "Фото нерухомості",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { isFullScreenVisible = !isFullScreenVisible }, // Відкриваємо фулскрін по кліку
                    contentScale = ContentScale.Crop,
                    onError = { error ->
                        // Виведемо помилку в консоль (Logcat)
                        println("COIL ERROR: ${error.result.throwable.message}")
                        error.result.throwable.printStackTrace()
                    }
                )
            }

            // Затемнення верху картки
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
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

            // Стрілки вліво/вправо (тільки якщо фоток більше ніж 1)
            if(imagesCount > 1) {
                if(pagerState.currentPage > 0){
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp, bottom = 70.dp)
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    ){
                        Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = "", tint = Color.White)
                    }
                }

                if(pagerState.currentPage < imagesCount - 1){
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(start = 8.dp, bottom = 70.dp)
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    ){
                        Icon(painterResource(Res.drawable.ic_arrow_next), contentDescription = "", tint = Color.White)
                    }
                }
            }

            // Білі лінії індикації
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
                            .clip(SwipeHomeTheme.shapes.smallShape)
                            .background(
                                if (i == pagerState.currentPage) Color.White
                                else Color.White.copy(alpha = 0.4f)
                            )
                    )
                }
            }

            // Картка, що наїжджає на фото
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = SwipeHomeTheme.shapes.largeShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Бейдж "Новобудова"
                        Box(
                            modifier = Modifier
                                .background(SwipeHomeTheme.colors.primary.copy(alpha = 0.15f), SwipeHomeTheme.shapes.smallShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                property.building_type,
                                color = SwipeHomeTheme.colors.primary,
                                style = SwipeHomeTheme.typography.caption,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            property.price,
                            style = SwipeHomeTheme.typography.headline,
                            fontWeight = FontWeight.ExtraBold,
                            color = SwipeHomeTheme.colors.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        property.title,
                        style = SwipeHomeTheme.typography.subheadline,
                        fontWeight = FontWeight.Bold,
                        color = SwipeHomeTheme.colors.neutral
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painterResource(Res.drawable.ic_location_pin),
                            contentDescription = null,
                            tint = SwipeHomeTheme.colors.onSurfaceSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            property.localization,
                            style = SwipeHomeTheme.typography.label,
                            color = SwipeHomeTheme.colors.onSurfaceSecondary
                        )
                    }
                }
            }
        }

        // --- ДІАЛОГ ДЛЯ ПОВНОЕКРАННОГО ПЕРЕГЛЯДУ ---
        if (isFullScreenVisible){
            FullScreenGallery(
                images = images,
                initialPage = pagerState.currentPage, // Відкриваємо на тій фото, яку клацнули
                onDismiss = { finalPage ->
                    isFullScreenVisible = false
                    // При закритті гортаємо маленьку карусель на те фото, на якому зупинилися в фулскріні
                    coroutineScope.launch {
                        pagerState.scrollToPage(finalPage)
                    }
                }
            )
        }
    }

    @Composable
    fun FullScreenGallery(
        images: List<String>,
        initialPage: Int,
        onDismiss: (Int) -> Unit // Передаємо індекс назад
    ) {
        val pagerState = rememberPagerState(
            initialPage = initialPage,
            pageCount = { images.size }
        )

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
            // Вираховуємо прозорість фону: що далі відтягнули, то прозорішим стає фон
            val backgroundAlpha = (1f - (abs(offsetY.value) / 1000f)).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = backgroundAlpha))
                    // Обробка жестів для вертикального свайпу
                    .pointerInput(Unit){
                        detectVerticalDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    // Якщо відтягнули більше ніж на 250 пікселів - закриваємо
                                    if (abs(offsetY.value) > 200f) {
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
                // Обгортка для вмісту, яка фізично зміщується по осі Y

                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset(x = 0, y = offsetY.value.roundToInt()) }
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ){ page ->
                        AsyncImage(
                            model = images.getOrNull(page),
                            contentDescription = "Повноекранне фото нерухомості",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit // Fir, щоб бачити всю картинку без обрізання
                        )
                    }

                    // Індикатор (текстовий 1/5)
                    Text(
                        text = "${pagerState.currentPage + 1}/${images.size}",
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(32.dp),
                        style = SwipeHomeTheme.typography.body,
                        fontWeight = FontWeight.Bold
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
    fun FeaturesGrid() {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FeatureItem(
                    icon = painterResource(Res.drawable.ic_bedroom),
                    value = property.rooms,
                    label = stringResource(string.details_screen_feauter_grid_rooms),
                    modifier = Modifier.weight(1f)
                )
                FeatureItem(
                    icon = painterResource(Res.drawable.ic_home_area),
                    value = property.area + stringResource(string.search_card_section_area),
                    label = stringResource(string.details_screen_feauter_grid_area),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FeatureItem(
                    icon = painterResource(Res.drawable.ic_apartment_floor),
                    value = "14/24",
                    label = stringResource(string.details_screen_feauter_grid_floor),
                    modifier = Modifier.weight(1f)
                )
                FeatureItem(
                    icon = painterResource(Res.drawable.ic_parking),
                    value = property.parking,
                    label = stringResource(string.details_screen_feauter_grid_parking),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    @Composable
    fun FeatureItem(icon: Painter, value: String, label: String, modifier: Modifier = Modifier) {
        Box(
            modifier = modifier
                .clip(SwipeHomeTheme.shapes.mediumShape)
                .background(SwipeHomeTheme.colors.surface)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = SwipeHomeTheme.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    value,
                    style = SwipeHomeTheme.typography.subheadline,
                    fontWeight = FontWeight.Bold,
                    color = SwipeHomeTheme.colors.neutral
                )
                Text(
                    label,
                    style = SwipeHomeTheme.typography.caption,
                    color = SwipeHomeTheme.colors.onSurfaceSecondary
                )
            }
        }
    }

    @Composable
    fun DescriptionSection() {
        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.padding(horizontal = 16.dp)){
            Text(
                text = stringResource(string.details_screen_description),
                style = SwipeHomeTheme.typography.subheadline,
                fontWeight = FontWeight.Bold,
                color = SwipeHomeTheme.colors.neutral,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = property.description,
                style = SwipeHomeTheme.typography.body,
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                lineHeight = 22.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else 5,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isExpanded) {
                        stringResource(string.details_screen_description_show_les)
                    } else {
                        stringResource(string.details_screen_description_show_more)
                    },
                    color = SwipeHomeTheme.colors.primary,
                    style = SwipeHomeTheme.typography.label,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { isExpanded = !isExpanded },
                )
                Spacer(modifier = Modifier.width(4.dp))
                if (isExpanded) {
                    Icon(
                        painterResource(Res.drawable.ic_arrow_single_up),
                        contentDescription = null,
                        tint = SwipeHomeTheme.colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Icon(
                        painterResource(Res.drawable.ic_arrow_single_down),
                        contentDescription = null,
                        tint = SwipeHomeTheme.colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun LocationSection() {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = property.localization,
                    style = SwipeHomeTheme.typography.subheadline,
                    fontWeight = FontWeight.Bold,
                    color = SwipeHomeTheme.colors.neutral
                )
                Text(
                    text = stringResource(string.details_screen_map_title),
                    style = SwipeHomeTheme.typography.label,
                    fontWeight = FontWeight.SemiBold,
                    color = SwipeHomeTheme.colors.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /*TODO:"Замінити на реальну мапу з Google Maps / MapBox*/
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(SwipeHomeTheme.shapes.mediumShape)
                    .background(SwipeHomeTheme.colors.surface),
                contentAlignment = Alignment.Center
            ){
                Text(
                    "Карта",
                    style = SwipeHomeTheme.typography.headline,
                    color = SwipeHomeTheme.colors.onSurfaceSecondary
                )
            }
        }
    }

    @Composable
    fun OwnerContactSection() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = SwipeHomeTheme.shapes.mediumShape,
            colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)){
                Text(
                    stringResource(string.details_screen_owner_contact_section_title),
                    style = SwipeHomeTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    color = SwipeHomeTheme.colors.onSurfaceSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Фото профілю з онлайн-статусом
                    Box{
                        AsyncImage(
                            model = "",
                            contentDescription = stringResource(string.owner_description),
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = (-2).dp, y = (-2).dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                                .border(2.dp, SwipeHomeTheme.colors.surface, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            "Петро Мафіознік",
                            style = SwipeHomeTheme.typography.subheadline,
                            fontWeight = FontWeight.Bold,
                            color = SwipeHomeTheme.colors.neutral
                        )
                        Text(
                            stringResource(string.details_screen_owner_contact_section_owner_phone_number),
                            style = SwipeHomeTheme.typography.caption,
                            color = SwipeHomeTheme.colors.onSurfaceSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = SwipeHomeTheme.shapes.smallShape,
                        colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.primary)
                    ){
                        Text(
                            stringResource(string.details_screen_owner_contact_section_write),
                            style = SwipeHomeTheme.typography.label,
                            fontWeight = FontWeight.Bold,
                            color = SwipeHomeTheme.colors.onPrimary
                        )
                    }

                    OutlinedButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = SwipeHomeTheme.shapes.smallShape,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SwipeHomeTheme.colors.primary)
                    ){
                        Text(
                            stringResource(string.details_screen_owner_contact_section_call),
                            style = SwipeHomeTheme.typography.label,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
