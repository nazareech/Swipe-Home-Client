package com.nazar_protasov.swipehome.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.app_name
import mymultiplatformproject.shared.generated.resources.ic_filtres_tune
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeTopBar(homeScreenModel: HomeScreenModel) {
    val navigator = LocalNavigator.currentOrThrow

    // Шукаємо кореневий навігатор, щоб FilterScreen відкривався на весь екран (поверх BottomBar)
    // і щоб уникнути помилки ClassCastException в TabNavigator
    var rootNavigator = navigator
    while (rootNavigator.parent != null) {
        rootNavigator = rootNavigator.parent!!
    }

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
            text = stringResource(Res.string.app_name),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        // Кнопка фільтрів
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                .clickable{
                    rootNavigator.push(FilterScreen(homeScreenModel))
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(Res.drawable.ic_filtres_tune), contentDescription = null)
        }
    }
}
