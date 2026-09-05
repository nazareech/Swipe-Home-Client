package com.nazar_protasov.swipehome.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_close
import mymultiplatformproject.shared.generated.resources.ic_edit_pencil
import mymultiplatformproject.shared.generated.resources.ic_heart
import mymultiplatformproject.shared.generated.resources.ic_list
import org.jetbrains.compose.resources.painterResource

// Модель даних для оголошення
enum class ListingStaus {
    ACTIVE,
    MODERATION,
    HIDDEN
}

data class ListingItem(
    val id: String,
    val title: String,
    val location: String,
    val imagesUrl: List<String>,
    val status: ListingStaus,
    val views: String,
    val likes: String
)

@Composable
fun ListingsTopBar() {
    Row(
       modifier = Modifier
           .fillMaxWidth()
           .background(SwipeHomeTheme.colors.background)
           .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = {/*TOD: Menu*/}){
            Icon(painterResource(Res.drawable.ic_list), contentDescription = "Menu", tint = SwipeHomeTheme.colors.neutral)
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
                .clickable { /*TODO: Додати нове оголошення */ },
            contentAlignment = Alignment.Center
        ){
            Icon(painterResource(Res.drawable.ic_close), contentDescription = "Add", tint = SwipeHomeTheme.colors.neutral)
        }
    }
}

@Composable
fun ListingsHeader(count: Int){
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

            // Кнопка фільтрації/сортування
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SwipeHomeTheme.colors.background)
                    .clickable{/*TODO: Сортування*/},
                contentAlignment = Alignment.Center
            ){
                Text("=", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.neutral, modifier = Modifier.offset(y = (-2).dp))
            }
        }
    }
}

@Composable
fun ListingCard (listing: ListingItem){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = SwipeHomeTheme.shapes.mediumShape,
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            // Фотографія об'єкта
            AsyncImage(
                model = listing.imagesUrl[0],
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
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = listing.title,
                        style = SwipeHomeTheme.typography.body,
                        color = SwipeHomeTheme.colors.neutral,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(1f)
                    )

                    Icon(
                        painter = painterResource(Res.drawable.ic_edit_pencil),
                        contentDescription = "Edit",
                        tint = SwipeHomeTheme.colors.neutral,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {/*TODO: Редагувати*/ }
                    )
                }

                Spacer(modifier = Modifier.size(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(Res.drawable.ic_list), contentDescription = null, tint = SwipeHomeTheme.colors.neutral, modifier = Modifier.size(14.dp))
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

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically){
                        // Перегляди
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(Res.drawable.ic_list), contentDescription = "Views", tint = SwipeHomeTheme.colors.neutral, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = listing.views,
                                style = SwipeHomeTheme.typography.label,
                                color = SwipeHomeTheme.colors.neutral
                            )
                        }
                        // Лайки
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(Res.drawable.ic_heart), contentDescription = "Likes", tint = SwipeHomeTheme.colors.neutral, modifier = Modifier.size(14.dp))
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

}

@Composable
fun StatusBadge(status: ListingStaus){
    val (bgColor, textColor, text) = when(status){
        ListingStaus.ACTIVE -> Triple(SwipeHomeTheme.colors.primary, SwipeHomeTheme.colors.onPrimary, "Активний")
        ListingStaus.MODERATION -> Triple(SwipeHomeTheme.colors.secondary, SwipeHomeTheme.colors.onSurfaceSecondary, "В модерації")
        ListingStaus.HIDDEN -> Triple(SwipeHomeTheme.colors.error, SwipeHomeTheme.colors.neutral, "Прихований")
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