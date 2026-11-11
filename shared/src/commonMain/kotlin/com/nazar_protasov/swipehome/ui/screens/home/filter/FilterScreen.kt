package com.nazar_protasov.swipehome.ui.screens.home.filter

import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.home.filter.components.CustomChip
import com.nazar_protasov.swipehome.ui.screens.home.filter.components.CustomDropdown
import com.nazar_protasov.swipehome.ui.screens.home.filter.components.FilterSection
import com.nazar_protasov.swipehome.ui.screens.home.filter.components.RangeInputRow
import com.nazar_protasov.swipehome.ui.screens.home.filter.components.SwitchRowExpanded
import com.nazar_protasov.swipehome.ui.screens.home.main.HomeScreenModel
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.*
import mymultiplatformproject.shared.generated.resources.Res.string
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class FilterScreen(private val homeScreenModel: HomeScreenModel) : Screen {
    override val key = uniqueScreenKey

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()
        val screenModel = koinScreenModel<FilterScreenModel>()

        // Localized strings
        val studioStr = stringResource(string.filter_rooms_studio)

        var buildingTypeExpanded by remember { mutableStateOf(false) }
        var floorExpanded by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ){
                            Text(
                                text = stringResource(string.filter_screen_title),
                                style = SwipeHomeTheme.typography.headline,
                                color = SwipeHomeTheme.colors.neutral
                            )
                            Text(
                                text = stringResource(string.filter_clear),
                                style = SwipeHomeTheme.typography.label,
                                color = SwipeHomeTheme.colors.error,
                                modifier = Modifier
                                    .clickable {
                                        screenModel.selectedCategory = null
                                        screenModel.dealType = null
                                        screenModel.minPrice = null; screenModel.maxPrice = null
                                        screenModel.minArea = null; screenModel.maxArea = null
                                        screenModel.selectedRooms = null
                                        screenModel.buildingType = null
                                        screenModel.selectedFloor = null
                                        screenModel.selectedParking = null
                                        screenModel.petsAllowed = null; screenModel.hasElevator = null; screenModel.withFurniture = null; screenModel.hasBalcony = null
                                    }
                                    .padding(bottom = 2.dp)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = stringResource(string.btn_back))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SwipeHomeTheme.colors.background
                    )
                )
            },
            bottomBar = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(SwipeHomeTheme.colors.background)
                    .padding(16.dp)
                    .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    OutlinedButton(
                        onClick = { navigator.pop() },
                        modifier = Modifier.weight(1.5f).height(56.dp),
                        shape = SwipeHomeTheme.shapes.smallShape
                    ){
                        Text(
                            stringResource(string.filter_cancel),
                            style = SwipeHomeTheme.typography.body,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {
                            val request = FilterRequestDTO(
                                limit = 10,
                                offset = 0,
                                category = screenModel.selectedCategory,
                                dealType = screenModel.dealType,
                                localization = null,
                                priceMin = screenModel.minPrice?.toDoubleOrNull(),
                                priceMax = screenModel.maxPrice?.toDoubleOrNull(),
                                areaMin = screenModel.minArea?.toDoubleOrNull(),
                                areaMax = screenModel.maxArea?.toDoubleOrNull(),
                                rooms = if (screenModel.selectedRooms == studioStr) null else screenModel.selectedRooms?.toIntOrNull(),
                                floor = if (screenModel.selectedFloor == Floor.ALL.name) null else screenModel.selectedFloor,
                                parking = if (screenModel.selectedParking == Parking.ALL.name) null else screenModel.selectedParking,
                                petsAllowed = screenModel.petsAllowed,
                                elevator = screenModel.hasElevator,
                                furniture = screenModel.withFurniture,
                                balcony = screenModel.hasBalcony,
                                buildingType = if (screenModel.buildingType == BuildingType.ALL.name) null else screenModel.buildingType,
                            )
                            navigator.pop()
                            homeScreenModel.updateFilters(request)
                        },
                        modifier = Modifier.weight(1.5f).height(56.dp),
                        shape = SwipeHomeTheme.shapes.smallShape,
                        colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.primary)
                    ){
                        Text(
                            stringResource(string.filter_apply),
                            style = SwipeHomeTheme.typography.body,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            containerColor = SwipeHomeTheme.colors.background
        ){ paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ){
                Spacer(modifier = Modifier.height(12.dp))

                // --- Тип нерухомості ---
                FilterSection(title = stringResource(string.filter_section_property_type)){
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        PropertyType.entries.forEach { category ->
                            CustomChip(
                                text = category.getLabel(),
                                isSelected = screenModel.selectedCategory == category.name,
                                onClick = {
                                    val it = category.name
                                    screenModel.selectedCategory = it
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // --- Тип угоди ---
                FilterSection(title = stringResource(string.filter_section_deal_type)){
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        DealType.entries.forEach { type ->
                            CustomChip(
                                text = type.getName(),
                                isSelected = screenModel.dealType == type.name,
                                onClick = { screenModel.dealType = type.name },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // --- Ціна ---
                FilterSection(title = stringResource(string.filter_section_price)){
                    RangeInputRow(
                        minValue = screenModel.minPrice, onMinChange = { screenModel.minPrice = it }, minPlaceholder = stringResource(string.filter_price_placeholder_min),
                        maxValue = screenModel.maxPrice, onMaxChange = { screenModel.maxPrice = it }, maxPlaceholder = stringResource(string.filter_price_placeholder_max)
                    )
                }

                // --- Площа ---
                FilterSection(title = stringResource(string.filter_section_area)){
                    RangeInputRow(
                        minValue = screenModel.minArea, onMinChange = { screenModel.minArea = it }, minPlaceholder = stringResource(string.filter_area_placeholder_min),
                        maxValue = screenModel.maxArea, onMaxChange = { screenModel.maxArea = it }, maxPlaceholder = stringResource(string.filter_area_placeholder_max)
                    )
                }

                // --- Кількість кімнат ---
                FilterSection(title = stringResource(string.filter_section_rooms_count)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        val roomCounts = listOf("1", "2", "3", "4", "5", studioStr)
                        roomCounts.forEach { room ->
                            CustomChip(
                                text = room,
                                isSelected = screenModel.selectedRooms == room,
                                onClick = { screenModel.selectedRooms = room },
                                modifier = if (room == studioStr) Modifier.weight(1f) else Modifier.defaultMinSize(minWidth = 48.dp)
                            )
                        }
                    }
                }

                // --- Тип будівлі (Dropdown) ---
                FilterSection(title = stringResource(string.filter_section_building_type)){
                    // Отримуємо вибраний об'єкт
                    val currentBuildingType = BuildingType.entries.find { it.name == screenModel.buildingType } ?: BuildingType.ALL
                    // Створюємо список пар: об'єкт enum -> його локалізована назва
                    val buildingOptions = BuildingType.entries.map { it to stringResource(it.resource) }

                    CustomDropdown(
                        expanded = buildingTypeExpanded,
                        onExpandedChange = { buildingTypeExpanded = it },
                        selectedValue = stringResource(currentBuildingType.resource),
                        onValueSelected = { selectedLabel ->
                            val selectedEnum = buildingOptions.firstOrNull { it.second == selectedLabel }?.first ?: BuildingType.ALL
                            screenModel.buildingType = if (selectedEnum == BuildingType.ALL) null else selectedEnum.name
                            buildingTypeExpanded = false
                        },
                        options = buildingOptions.map { it.second }
                    )
                }

                // --- Поверх (Dropdown) ---
                FilterSection(title = stringResource(string.filter_section_floor)){
                    // Отримуємо вибраний об'єкт
                    val currentFloor = Floor.entries.find { it.name == screenModel.selectedFloor } ?: Floor.ALL
                    // Створюємо список пар: об'єкт enum -> його локалізована назва
                    val floorOptions = Floor.entries.map { it to stringResource(it.resource) }

                    CustomDropdown(
                        expanded = floorExpanded,
                        onExpandedChange = { floorExpanded = it },
                        selectedValue = stringResource(currentFloor.resource) ,
                        onValueSelected = { selectLabel ->
                            val selectEnum = floorOptions.firstOrNull { it.second == selectLabel }?.first ?: Floor.ALL
                            screenModel.selectedFloor =  if (selectEnum == Floor.ALL) null else selectEnum.name
                            floorExpanded = false
                        },
                        options = floorOptions.map { it.second }
                    )
                }

                // --- Паркінг ---
                FilterSection(title = stringResource(string.filter_section_parking)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Parking.entries.forEach { parking ->
                            CustomChip(
                                text = parking.getName(),
                                isSelected = screenModel.selectedParking == parking.name,
                                onClick = { screenModel.selectedParking = parking.name }
                            )
                        }
                    }
                }

                // --- Зручності (Світчі) ---
                Card(
                    shape = SwipeHomeTheme.shapes.smallShape,
                    colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column(modifier = Modifier.padding(16.dp)) {
                        SwitchRowExpanded(
                            title = stringResource(string.filter_amenities_pets),
                            subtitle = stringResource(string.filter_amenities_pets_desc),
                            icon = painterResource(Res.drawable.ic_pets_allowed),
                            checked = screenModel.petsAllowed ?: false,
                            onCheckedChange = { screenModel.petsAllowed = it}
                        )
                        HorizontalDivider(color = SwipeHomeTheme.colors.outline, modifier = Modifier.padding(vertical = 12.dp))

                        SwitchRowExpanded(
                            title = stringResource(string.filter_amenities_elevator),
                            subtitle = stringResource(string.filter_amenities_elevator_desc),
                            icon = painterResource(Res.drawable.ic_elevator),
                            checked = screenModel.hasElevator ?: false,
                            onCheckedChange = { screenModel.hasElevator = it}
                        )
                        HorizontalDivider(color = SwipeHomeTheme.colors.outline, modifier = Modifier.padding(vertical = 12.dp))

                        SwitchRowExpanded(
                            title = stringResource(string.filter_amenities_furniture),
                            subtitle = stringResource(string.filter_amenities_furniture_desc),
                            icon = painterResource(Res.drawable.ic_sofa),
                            checked = screenModel.withFurniture ?: false,
                            onCheckedChange = { screenModel.withFurniture = it}
                        )
                        HorizontalDivider(color = SwipeHomeTheme.colors.outline, modifier = Modifier.padding(vertical = 12.dp))

                        SwitchRowExpanded(
                            title = stringResource(string.filter_amenities_balcony),
                            subtitle = stringResource(string.filter_amenities_balcony_desc),
                            icon = painterResource(Res.drawable.ic_balcony),
                            checked = screenModel.hasBalcony ?: false,
                            onCheckedChange = { screenModel.hasBalcony = it}
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

