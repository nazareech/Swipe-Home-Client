package com.nazar_protasov.swipehome.ui.screens.settings.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.network.CreateUserInfoResponses
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.theme.LocalThemeIsDark
import com.nazar_protasov.swipehome.ui.theme.LocalThemeToggle
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.Res.string
import mymultiplatformproject.shared.generated.resources.btn_next
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import mymultiplatformproject.shared.generated.resources.ic_logout
import mymultiplatformproject.shared.generated.resources.ic_moon_outline
import mymultiplatformproject.shared.generated.resources.ic_theme_light
import mymultiplatformproject.shared.generated.resources.ic_trash_can
import mymultiplatformproject.shared.generated.resources.ic_user_verified
import mymultiplatformproject.shared.generated.resources.profile_tab_delete_account
import mymultiplatformproject.shared.generated.resources.profile_tab_logout
import mymultiplatformproject.shared.generated.resources.profile_tab_name
import mymultiplatformproject.shared.generated.resources.profile_tab_photo_avatar_description
import mymultiplatformproject.shared.generated.resources.profile_tab_subscription_management
import mymultiplatformproject.shared.generated.resources.profile_tab_toggle_theme_description_dark
import mymultiplatformproject.shared.generated.resources.profile_tab_toggle_theme_description_light
import mymultiplatformproject.shared.generated.resources.profile_tab_verified_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class MenuItemData(
    val icon: Painter,
    val title: String,
    val badgeCount: Int? = null,
    val onClick: () -> Unit // Обробник натискання
)

@Composable
fun ProfileTopBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(Modifier.width(36.dp))

        Text(stringResource(string.profile_tab_name), style = SwipeHomeTheme.typography.headline, color = SwipeHomeTheme.colors.neutral)

        IconButton(onClick = LocalThemeToggle.current) {
            if (LocalThemeIsDark.current) {
                Icon(
                    painterResource(Res.drawable.ic_moon_outline),
                    tint = SwipeHomeTheme.colors.neutral,
                    contentDescription = stringResource(string.profile_tab_toggle_theme_description_dark)
                )
            }else {
                Icon(
                    painterResource(Res.drawable.ic_theme_light),
                    tint = SwipeHomeTheme.colors.neutral,
                    contentDescription = stringResource(string.profile_tab_toggle_theme_description_light)
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(userProfile: State<CreateUserInfoResponses?>){

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val avatarUrl = "${PropertyApiService.BASE_URL}/${userProfile.value?.avatarUrl}"

        println("Avatar URL: $avatarUrl")
        // Аватарка з градієнтною рамкою редагувати
        Box(contentAlignment = Alignment.BottomEnd){
            AsyncImage(
                model = avatarUrl,
                contentDescription = stringResource(string.profile_tab_photo_avatar_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                SwipeHomeTheme.colors.primary,
                                SwipeHomeTheme.colors.neutral
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val fullName = "${userProfile.value?.firstName} ${userProfile.value?.lastName}"
        // Ім'я та галочка верифікації
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(fullName , style = SwipeHomeTheme.typography.headline, color = SwipeHomeTheme.colors.neutral)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(painterResource(Res.drawable.ic_user_verified), contentDescription = stringResource(string.profile_tab_verified_description), tint = SwipeHomeTheme.colors.primary, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(string.profile_tab_subscription_management),
            style = SwipeHomeTheme.typography.label,
            color = SwipeHomeTheme.colors.neutral,
            modifier = Modifier.clickable{/*TODO: Екран з підпискою*/} .padding(4.dp)
        )
    }
}


@Composable
fun ProfileMenuCard(items: List<MenuItemData>){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = SwipeHomeTheme.shapes.mediumShape,
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        Column {
            items.forEachIndexed { index, item ->
                ProfileMenuItem(
                    item = item,
                    onClick = { item.onClick() } // Викликаємо обробник натискання
                )

                // Додаємо роздільник між пунктами меню, крім останнього
                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = SwipeHomeTheme.colors.outline
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(item: MenuItemData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = item.icon,
            contentDescription = item.title,
            tint = SwipeHomeTheme.colors.neutral,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Текст
        Text(
            text = item.title,
            color = SwipeHomeTheme.colors.neutral,
            style = SwipeHomeTheme.typography.label,
            modifier = Modifier.weight(1f)
        )

        // Бейдж (якщо є)
        if (item.badgeCount != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SwipeHomeTheme.colors.primary)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.badgeCount.toString(), color = SwipeHomeTheme.colors.onPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Стрілочка вправо
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_next),
            contentDescription = stringResource(string.btn_next),
            tint = SwipeHomeTheme.colors.neutral,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ProfileActions(){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Кнопка виходу
        Button(
          onClick = { /*TODO: Вихід з аккаунту*/},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = SwipeHomeTheme.shapes.mediumShape,
            colors = ButtonDefaults.buttonColors(
                SwipeHomeTheme.colors.error,
                SwipeHomeTheme.colors.neutral
            ),
            elevation = ButtonDefaults.buttonElevation(2.dp)
        ){
            Icon(painterResource(Res.drawable.ic_logout), contentDescription = stringResource(string.profile_tab_logout), tint = SwipeHomeTheme.colors.neutral)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(string.profile_tab_logout), style = SwipeHomeTheme.typography.label)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка видалення акаунта
        Button(
            onClick = { /*TODO: Видалення акаунту*/},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = SwipeHomeTheme.shapes.mediumShape,
            colors = ButtonDefaults.buttonColors(
                SwipeHomeTheme.colors.surface,
                SwipeHomeTheme.colors.neutral
            ),
            elevation = ButtonDefaults.buttonElevation(2.dp)
        ){
            Icon(painterResource(Res.drawable.ic_trash_can), contentDescription = stringResource(string.profile_tab_delete_account), tint = SwipeHomeTheme.colors.error)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(string.profile_tab_delete_account), style = SwipeHomeTheme.typography.label)
        }
    }
}