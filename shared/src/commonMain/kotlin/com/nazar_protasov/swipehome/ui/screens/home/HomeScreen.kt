package com.nazar_protasov.swipehome.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.nazar_protasov.swipehome.ui.components.PropertyCard
import com.nazar_protasov.swipehome.ui.models.Property

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = { HomeTopBar() },
            bottomBar = { HomeBottomActionButtons() },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            // Центральна зона для карток
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Викликаємо картку з текстовими даними
                PropertyCard(
                    property = Property(
                        id = "1",
                        imageUrl = "http://localhost:8080/uploads/application-number-1/bd3e357c-f613-42d3-b420-9d0bc285fcd1.jpg",
                        price = "$120,000",
                        title = "Чьотка халупа з видом на озеро",
                        location = "Люблін, Віенява",
                        details = "4 кімтани - 120м - 2 поверх"
                    )
                )
            }
        }
    }
}