package com.nazar_protasov.swipehome.ui.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.nazar_protasov.swipehome.ui.screens.home.HomeTab

class RootScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(HomeTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.background
                    ) {
                        TabNavigationItem(HomeTab)
                        // TODO: Додамо інші вкладки, коли будуть створені
                        // TabNavigationItem(FavoritesTab)
                        // TabNavigationItem(ChatsTab)
                        // TabNavigationItem(ProfileTab)
                    }
                }
            ) { paddingValues ->
                // CurrentTab автоматично відображає контент вибраної вкладки
                CurrentTab()
            }
        }
    }
}

// Допоміжна функція для малювання кнопки меню
@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let { icon ->
                Icon(
                    painter = icon,
                    contentDescription = tab.options.title
                )
            }
        },
        label = {Text(tab.options.title)},
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}