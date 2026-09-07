package com.nazar_protasov.swipehome.ui.screens.settings.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.settings.notification.components.AllSettingsTab
import com.nazar_protasov.swipehome.ui.screens.settings.notification.components.ChannelsSettingsTab
import com.nazar_protasov.swipehome.ui.screens.settings.notification.components.DndCard
import com.nazar_protasov.swipehome.ui.screens.settings.notification.components.NotificationsTopBar
import com.nazar_protasov.swipehome.ui.screens.settings.notification.components.OwnersSettingsTab
import com.nazar_protasov.swipehome.ui.screens.settings.notification.components.SeekersSettingsTab
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import kotlinx.coroutines.launch
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_check_circle_outline
import org.jetbrains.compose.resources.painterResource

class NotificationsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        var selectedTab by remember { mutableStateOf(0) }
        val tabs = listOf("Всі", "Шукачам", "Власникам", "Канали")

        // Функція для виклику неблокуючого спливаючого повідомлення
        val triggerAutoSaveToast: () -> Unit = {
            coroutineScope.launch {
                // Скасовуємо попередній снекбар, якщо юзер швидко клацає
                snackBarHostState.currentSnackbarData?.dismiss()
                snackBarHostState.showSnackbar(
                    message = "Усі зміни збережено автоматично!",
                    duration = SnackbarDuration.Short
                )
            }
        }

        Scaffold(
            topBar = {
                Column(modifier = Modifier.background(SwipeHomeTheme.colors.background)) {
                    NotificationsTopBar(navigator = navigator)

                    // Таби
                    SecondaryTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = SwipeHomeTheme.colors.background,
                        indicator = {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(selectedTab),
                                color = SwipeHomeTheme.colors.primary
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        title,
                                        fontSize = if(selectedTab == index) 12.sp else 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if(selectedTab == index) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.onSurfaceSecondary,
                                    )
                                },
                            )
                        }
                    }
                }
            },
            // Кастомний Snackbar (Плашка збереження)
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) {data ->
                    Box(
                        modifier = Modifier
                            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(SwipeHomeTheme.colors.background)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painterResource(Res.drawable.ic_check_circle_outline), contentDescription = null, tint = SwipeHomeTheme.colors.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(data.visuals.message, style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.neutral)
                        }
                    }
                }
            },
            containerColor = SwipeHomeTheme.colors.background
        ){ paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ){
                // Показуємо велику картку DND ТІЛЬКИ на вкладках "Всі" (0) та "Канали" (3)
                AnimatedVisibility(
                    visible = selectedTab == 0 || selectedTab == 3,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    DndCard(modifier = Modifier.padding(16.dp), onSettingsChanged = triggerAutoSaveToast)
                }

                if (selectedTab != 0 && selectedTab != 3) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Вміст вкладок
                when(selectedTab){
                    0 -> AllSettingsTab(triggerAutoSaveToast)
                    1 -> SeekersSettingsTab(triggerAutoSaveToast)
                    2 -> OwnersSettingsTab(triggerAutoSaveToast)
                    3 -> ChannelsSettingsTab(triggerAutoSaveToast)
                }
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}