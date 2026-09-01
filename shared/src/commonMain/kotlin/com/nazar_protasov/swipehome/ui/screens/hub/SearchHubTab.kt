package com.nazar_protasov.swipehome.ui.screens.hub

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.nazar_protasov.swipehome.ui.components.BadgeChip
import com.nazar_protasov.swipehome.ui.components.CompareFloatingBar
import com.nazar_protasov.swipehome.ui.components.ComparisonTableContent
import com.nazar_protasov.swipehome.ui.components.HubGridCard
import com.nazar_protasov.swipehome.ui.components.StatisticsSection
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_filtres_tune
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

class SearchHubTab : Tab {
    override val key = uniqueScreenKey
    override val options: TabOptions
    @Composable
    get() = TabOptions(index = 1u, title = "Хаб") // TODO: Додати іконку

    @Composable
    override fun Content() {
        Navigator(SearchHubScreen)
    }
}

internal object SearchHubScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel: SearchHubScreenModel = koinInject()
        val selectedTab by screenModel.selectedTabIndex.collectAsState()
        val isCompareMode by screenModel.isCompareMode.collectAsState()
        val selectedForCompare by screenModel.selectedForCompare.collectAsState()

        val savedProperties by screenModel.savedProperties.collectAsState()
        val rejectedProperties by screenModel.rejectedProperties.collectAsState()

        val currentList = if (selectedTab == 0) savedProperties else rejectedProperties
        var showComparisonSheet by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Хаб пошуку",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    // Обнуляємо системні відступи зверху і з боків
                    windowInsets = WindowInsets(0.dp),
                    actions = {
                        IconButton(onClick = { /*TODO: Фільтри*/ }) {
                            Icon(
                                painterResource(Res.drawable.ic_filtres_tune),
                                contentDescription = "Фільтри"
                            )
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                // Один загальний список, який скролить весь екран
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    //padding застосовується до своєї сітки (bottom = 100.dp, щоб плаваюча кнопка не перекривала останні картки)
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 100.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Статистика
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        StatisticsSection()
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Кнопка порівняння та таби
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { screenModel.toggleCompareMode() },
                                colors = if (isCompareMode) ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00695C)
                                ) else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text("Порівняння", color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            SecondaryTabRow(
                                selectedTabIndex = selectedTab,
                                containerColor = Color.Transparent,
                                indicator = {
                                    TabRowDefaults.SecondaryIndicator(
                                        modifier = Modifier.tabIndicatorOffset(selectedTab),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            ) {
                                // Таб збережених варіантів
                                Tab(
                                    selected = selectedTab == 0,
                                    onClick = { screenModel.setTabIndex(0) },
                                    text = {
                                        Text(
                                            "Збережені",
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedTab == 0) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = 0.6f
                                            )
                                        )
                                    }
                                )
                                // Таб відхилених варіантів
                                Tab(
                                    selected = selectedTab == 1,
                                    onClick = { screenModel.setTabIndex(1) },
                                    text = {
                                        Text(
                                            "Відхилені",
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedTab == 1) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = 0.6f
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // Фільтри списку
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    if (selectedTab == 0) "Збережені варіанти" else "Відхилені варіанти",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    "${currentList.size} об'єктів",
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                BadgeChip("Усі", true)
                                BadgeChip("Нові", false)
                            }
                        }
                    }

                    // Сітка карток
                    items(currentList) { property ->
                        HubGridCard(
                            property = property,
                            isCompareMode = isCompareMode,
                            isSelected = selectedForCompare.contains(property.id),
                            onClick = {
                                if (isCompareMode) {
                                    screenModel.togglePropertySelection(property.id)
                                } else {
                                    // TODO: Перехід на PropertyDetailsScreen
                                }
                            }
                        )
                    }
                }

                // Плаваюча панель порівняння (з'являються при виборі >=2)
                AnimatedVisibility(
                    visible = selectedForCompare.size >= 2,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                ) {
                    CompareFloatingBar(
                        selectedCount = selectedForCompare.size,
                        selectedProperties = savedProperties.filter { it.id in selectedForCompare },
                        onCompareClick = {
                            showComparisonSheet = true
                        }
                    )
                }
            }
        }

        // Bottom Sheet для детального порівняння
        if (showComparisonSheet) {
            ModalBottomSheet(
                onDismissRequest = { showComparisonSheet = false },
                containerColor = Color.White
            ) {
                ComparisonTableContent(
                    selectedProperties = savedProperties.filter { it.id in selectedForCompare }
                )
            }
        }
    }
}