package com.nazar_protasov.swipehome.ui.screens.settings.analystics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.settings.analystics.components.AnalyticsTopBar
import com.nazar_protasov.swipehome.ui.screens.settings.analystics.components.ChartSection
import com.nazar_protasov.swipehome.ui.screens.settings.analystics.components.InsightsSection
import com.nazar_protasov.swipehome.ui.screens.settings.analystics.components.PropertyHeroSection
import com.nazar_protasov.swipehome.ui.screens.settings.analystics.components.ReactionRatioSection
import com.nazar_protasov.swipehome.ui.screens.settings.analystics.components.StatsGridSection
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource

class AnalyticsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = { AnalyticsTopBar(navigator)},
            containerColor = SwipeHomeTheme.colors.background,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ){
                // Картка об'єкта
                PropertyHeroSection()

                // Сітка статистики
                StatsGridSection()

                // Динаміка зацікавлення (стовпчиковий графік)
                ChartSection()

                // Співвідношення реакцій (Кільцевий графік)
                ReactionRatioSection()

                // Інсайти активності
                InsightsSection()
            }
        }
    }
}