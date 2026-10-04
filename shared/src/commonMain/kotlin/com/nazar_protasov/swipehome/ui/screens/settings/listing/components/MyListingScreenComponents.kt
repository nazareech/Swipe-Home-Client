package com.nazar_protasov.swipehome.ui.screens.settings.listing.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.CreateListingScreen
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_add_circle
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_edit_pencil
import mymultiplatformproject.shared.generated.resources.ic_eye_visible
import mymultiplatformproject.shared.generated.resources.ic_filtres_tune
import mymultiplatformproject.shared.generated.resources.ic_heart
import mymultiplatformproject.shared.generated.resources.ic_location_pin
import org.jetbrains.compose.resources.painterResource

// Модель даних для оголошення
enum class ListingStatus {
    ACTIVE,
    MODERATION,
    DRAFT,
    HIDDEN
}

data class ListingItem(
    val id: String,
    val title: String,
    val location: String,
    val imagesUrl: List<String>,
    val status: ListingStatus,
    val views: String,
    val likes: String,
    val onClick: () -> Unit // Обробник натискання
)

@Composable
fun ListingsTopBar(navigator: Navigator) {
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
            text = "Swipe Home",
            style = SwipeHomeTheme.typography.headline,
            color = SwipeHomeTheme.colors.neutral
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SwipeHomeTheme.colors.background)
                .clickable { navigator.push(CreateListingScreen()) },
            contentAlignment = Alignment.Center
        ){
            Icon(painterResource(Res.drawable.ic_add_circle), contentDescription = "Add", tint = SwipeHomeTheme.colors.neutral)
        }
    }
}

@Composable
fun ListingsHeader(count: Int, isLoading: Boolean){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Text(
                text = "Ваші оголошення",
                style = SwipeHomeTheme.typography.label,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "$count оголошень",
                style = SwipeHomeTheme.typography.headline,
                color = SwipeHomeTheme.colors.neutral
            )

        }
        // Кнопка фільтрації/сортування
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SwipeHomeTheme.colors.background)
                .clickable{/*TODO: Сортування*/},
            contentAlignment = Alignment.Center
        ){
            if(isLoading) {
                CircularProgressIndicator(
                    color = SwipeHomeTheme.colors.primary
                )
            } else {
                Icon(
                    painterResource(Res.drawable.ic_filtres_tune),
                    contentDescription = "Filter",
                    tint = SwipeHomeTheme.colors.neutral
                )
            }
        }
    }
}

@Composable
fun ListingCard (listing: ListingItem, onEditClick: () -> Unit, onAnalyticClick: () -> Unit, onDeleteClick: () -> Unit){
    // Стани для контекстного меню
    var showMenu by remember { mutableStateOf(false) }
    // Зберігаємо координату дотику у Dp
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }

    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
            .pointerInput(Unit){
                detectTapGestures(
                    onTap = { listing.onClick() },
                    onLongPress = { offset ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                        // Конвертуємо пікселі (px) у Dp, бо Compose Layout працює з Dp
                        pressOffset = DpOffset(
                            with(density) { offset.x.toDp() },
                            with(density) { offset.y.toDp() }
                        )
                        showMenu = true
                    }
                )
            },
            shape = SwipeHomeTheme.shapes.mediumShape,
            colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Фотографія об'єкта
                AsyncImage(
                    model = listing.imagesUrl.firstOrNull() ?: "",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(SwipeHomeTheme.shapes.mediumShape)
                        .background(SwipeHomeTheme.colors.background)
                )

                Spacer(modifier = Modifier.size(16.dp))

                // Інформація
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = listing.title,
                            style = SwipeHomeTheme.typography.body,
                            color = SwipeHomeTheme.colors.onSurfaceSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = onEditClick) {
                            Icon(
                                painterResource(Res.drawable.ic_edit_pencil),
                                contentDescription = "Edit",
                                tint = SwipeHomeTheme.colors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_location_pin),
                            contentDescription = "localization",
                            tint = SwipeHomeTheme.colors.neutral,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))

                        Text(
                            text = listing.location,
                            style = SwipeHomeTheme.typography.label,
                            color = SwipeHomeTheme.colors.neutral,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.size(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(status = listing.status)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Перегляди
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_eye_visible),
                                    contentDescription = "Views",
                                    tint = SwipeHomeTheme.colors.neutral,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = listing.views,
                                    style = SwipeHomeTheme.typography.label,
                                    color = SwipeHomeTheme.colors.neutral
                                )
                            }
                            // Лайки
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_heart),
                                    contentDescription = "Likes ",
                                    tint = SwipeHomeTheme.colors.neutral,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = listing.likes,
                                    style = SwipeHomeTheme.typography.label,
                                    color = SwipeHomeTheme.colors.neutral
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.offset(x = pressOffset.x, y = pressOffset.y)
        ) {
            // Контекстне меню, яке випадає при довгому натисканні
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Аналітика") },
                    onClick = {
                        showMenu = false
                        onAnalyticClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Видалити", color = SwipeHomeTheme.colors.error) },
                    onClick = {
                        showMenu = false
                        onDeleteClick()
                    }
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: ListingStatus){
    val (bgColor, textColor, text) = when(status){
        ListingStatus.ACTIVE -> Triple(SwipeHomeTheme.colors.primary, SwipeHomeTheme.colors.onPrimary, "Активний")
        ListingStatus.MODERATION -> Triple(SwipeHomeTheme.colors.secondary, SwipeHomeTheme.colors.onSurfaceSecondary, "В модерації")
        ListingStatus.DRAFT -> Triple(SwipeHomeTheme.colors.surface, SwipeHomeTheme.colors.onSurfaceSecondary, "Чернетка")
        ListingStatus.HIDDEN -> Triple(SwipeHomeTheme.colors.error, SwipeHomeTheme.colors.neutral, "Прихований")
    }

    Row(
        modifier = Modifier
            .clip(SwipeHomeTheme.shapes.mediumShape)
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(textColor)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = text,
            style = SwipeHomeTheme.typography.caption,
            color = textColor
        )
    }
}