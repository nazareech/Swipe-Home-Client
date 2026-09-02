package com.nazar_protasov.swipehome.ui.screens.hub

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.btn_filter
import mymultiplatformproject.shared.generated.resources.ic_filtres_tune
import mymultiplatformproject.shared.generated.resources.ic_hub_compare
import mymultiplatformproject.shared.generated.resources.search_hub_btn_all
import mymultiplatformproject.shared.generated.resources.search_hub_btn_comparison
import mymultiplatformproject.shared.generated.resources.search_hub_btn_new
import mymultiplatformproject.shared.generated.resources.search_hub_count_objects
import mymultiplatformproject.shared.generated.resources.search_hub_name
import mymultiplatformproject.shared.generated.resources.search_hub_rejected
import mymultiplatformproject.shared.generated.resources.search_hub_rejected_options
import mymultiplatformproject.shared.generated.resources.search_hub_saved
import mymultiplatformproject.shared.generated.resources.search_hub_saved_options
import mymultiplatformproject.shared.generated.resources.search_hub_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class SearchHubTab : Tab {
    override val key = uniqueScreenKey
    override val options: TabOptions
    @Composable
    get() = TabOptions(index = 1u, title = stringResource(Res.string.search_hub_name), icon = painterResource(Res.drawable.ic_hub_compare) ) // TODO: Додати іконку

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
                            stringResource(Res.string.search_hub_title),
                            style = SwipeHomeTheme.typography.headline,
                            color = SwipeHomeTheme.colors.neutral
                        )
                    },
                    windowInsets = WindowInsets(0.dp),
                    actions = {
                        IconButton(onClick = { /*TODO: Фільтри*/ }) {
                            Icon(
                                painterResource(Res.drawable.ic_filtres_tune),
                                contentDescription = stringResource(Res.string.btn_filter)
                            )
                        }
                    }
                )
            },
            containerColor = SwipeHomeTheme.colors.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
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
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        StatisticsSection()
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { screenModel.toggleCompareMode() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCompareMode) SwipeHomeTheme.colors.primary.copy(alpha = 0.8f) else SwipeHomeTheme.colors.primary
                                ),
                                shape = SwipeHomeTheme.shapes.largeShape,
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text(stringResource(Res.string.search_hub_btn_comparison), color = SwipeHomeTheme.colors.onPrimary)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            SecondaryTabRow(
                                selectedTabIndex = selectedTab,
                                containerColor = Color.Transparent,
                                indicator = {
                                    TabRowDefaults.SecondaryIndicator(
                                        modifier = Modifier.tabIndicatorOffset(selectedTab),
                                        color = SwipeHomeTheme.colors.primary
                                    )
                                }
                            ) {
                                Tab(
                                    selected = selectedTab == 0,
                                    onClick = { screenModel.setTabIndex(0) },
                                    text = {
                                        Text(
                                            stringResource(Res.string.search_hub_saved),
                                            style = SwipeHomeTheme.typography.label,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedTab == 0) SwipeHomeTheme.colors.neutral else SwipeHomeTheme.colors.onSurfaceSecondary
                                        )
                                    }
                                )
                                Tab(
                                    selected = selectedTab == 1,
                                    onClick = { screenModel.setTabIndex(1) },
                                    text = {
                                        Text(
                                            stringResource(Res.string.search_hub_rejected),
                                            style = SwipeHomeTheme.typography.label,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedTab == 1) SwipeHomeTheme.colors.neutral else SwipeHomeTheme.colors.onSurfaceSecondary
                                        )
                                    }
                                )
                            }
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    if (selectedTab == 0) stringResource(Res.string.search_hub_saved_options) else stringResource(Res.string.search_hub_rejected_options),
                                    style = SwipeHomeTheme.typography.body,
                                    fontWeight = FontWeight.Medium,
                                    color = SwipeHomeTheme.colors.neutral
                                )
                                Text(
                                    "${currentList.size} ${stringResource(Res.string.search_hub_count_objects)}",
                                    style = SwipeHomeTheme.typography.caption,
                                    color = SwipeHomeTheme.colors.onSurfaceSecondary
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                BadgeChip(stringResource(Res.string.search_hub_btn_all), true)
                                BadgeChip(stringResource(Res.string.search_hub_btn_new), false)
                            }
                        }
                    }

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

        if (showComparisonSheet) {
            ModalBottomSheet(
                onDismissRequest = { showComparisonSheet = false },
                containerColor = SwipeHomeTheme.colors.surface,
                contentColor = SwipeHomeTheme.colors.neutral,
                dragHandle = { BottomSheetDefaults.DragHandle(color = SwipeHomeTheme.colors.onSurfaceSecondary) }
            ) {
                ComparisonTableContent(
                    selectedProperties = savedProperties.filter { it.id in selectedForCompare }
                )
            }
        }
    }
}
