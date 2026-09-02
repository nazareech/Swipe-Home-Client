package com.nazar_protasov.swipehome.ui.screens.home

import com.nazar_protasov.swipehome.network.dto.FilterRequestDTO
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
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

        // Localized strings
        val apartmentStr = stringResource(string.filter_property_apartment)
        val houseStr = stringResource(string.filter_property_house)
        val roomsStr = stringResource(string.filter_property_rooms)
        val allStr = stringResource(string.filter_deal_all)
        val rentStr = stringResource(string.filter_deal_rent)
        val saleStr = stringResource(string.filter_deal_sale)
        val allTypesStr = stringResource(string.filter_building_all)
        val anyFloorStr = stringResource(string.filter_floor_any)
        val studioStr = stringResource(string.filter_rooms_studio)

        // Отримуємо поточні (раніше збережені) фільтри
        val activeFilters = homeScreenModel.currentFilters.value

        // --- Стани фільтрів ---
        var selectedCategoryLabel by remember { mutableStateOf(apartmentStr) }
        var selectedDealLabel by remember { mutableStateOf(allStr) }

        var minPrice by remember { mutableStateOf(activeFilters.priceMin?.toInt()?.toString() ?: "") }
        var maxPrice by remember { mutableStateOf(activeFilters.priceMax?.toInt()?.toString() ?: "") }

        var minArea by remember { mutableStateOf(activeFilters.areaMin?.toInt()?.toString() ?: "") }
        var maxArea by remember { mutableStateOf(activeFilters.areaMax?.toInt()?.toString() ?: "") }

        var selectedRooms by remember { mutableStateOf(activeFilters.rooms?.toString() ?: "1") }

        var buildingTypeExpanded by remember { mutableStateOf(false) }
        var selectedTypeExpanded by remember { mutableStateOf(activeFilters.buildingType ?: allTypesStr) }

        var floorExpanded by remember { mutableStateOf(false) }
        var selectedFloor by remember { mutableStateOf(activeFilters.floor ?: anyFloorStr) }

        var selectedParking by remember { mutableStateOf(activeFilters.parking ?: allStr) }

        // Зручності
        var petsAllowed by remember { mutableStateOf(activeFilters.petsAllowed ?: false) }
        var hasElevator by remember { mutableStateOf(activeFilters.elevator ?: false) }
        var withFurniture by remember { mutableStateOf(activeFilters.furniture ?: false) }
        var hasBalcony by remember { mutableStateOf(activeFilters.balcony ?: false) }

        // Мапінг текстових назв категорій
        val labelToCategory = mapOf(
            apartmentStr to "APARTMENT",
            houseStr to "HOUSE",
            roomsStr to "ROOM"
        )

        val labelToDeal = mapOf(
            rentStr to "RENT",
            saleStr to "SALE"
        )

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
                                        selectedCategoryLabel = apartmentStr
                                        selectedDealLabel = allStr
                                        minPrice = ""; maxPrice = ""
                                        minArea = ""; maxArea = ""
                                        selectedRooms = "1"
                                        selectedTypeExpanded = allTypesStr
                                        selectedFloor = anyFloorStr
                                        selectedParking = allStr
                                        petsAllowed = false; hasElevator = false; withFurniture = false; hasBalcony = false
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
                                category = labelToCategory[selectedCategoryLabel],
                                subcategory = labelToDeal[selectedDealLabel],
                                localization = null,
                                priceMin = minPrice.toDoubleOrNull(),
                                priceMax = maxPrice.toDoubleOrNull(),
                                areaMin = minArea.toDoubleOrNull(),
                                areaMax = maxArea.toDoubleOrNull(),
                                rooms = if (selectedRooms == studioStr) null else selectedRooms.toIntOrNull(),
                                floor = if (selectedFloor == anyFloorStr) null else selectedFloor,
                                parking = if (selectedParking == allStr) null else selectedParking,
                                petsAllowed = petsAllowed,
                                elevator = hasElevator,
                                furniture = withFurniture,
                                balcony = hasBalcony,
                                buildingType = if (selectedTypeExpanded == allTypesStr) null else selectedTypeExpanded,
                            )
                            homeScreenModel.updateFilters(request)
                            navigator.pop()
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
                        val categories = listOf(apartmentStr, houseStr, roomsStr)
                        categories.forEach { category ->
                            CustomChip(
                                text = category,
                                isSelected = selectedCategoryLabel == category,
                                onClick = { selectedCategoryLabel = category },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // --- Тип угоди ---
                FilterSection(title = stringResource(string.filter_section_deal_type)){
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        val typeOfDeal = listOf(allStr, rentStr, saleStr)
                        typeOfDeal.forEach { type ->
                            CustomChip(
                                text = type,
                                isSelected = selectedDealLabel == type,
                                onClick = { selectedDealLabel = type },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // --- Ціна ---
                FilterSection(title = stringResource(string.filter_section_price)){
                    RangeInputRow(
                        minValue = minPrice, onMinChange = { minPrice = it }, minPlaceholder = stringResource(string.filter_price_placeholder_min),
                        maxValue = maxPrice, onMaxChange = { maxPrice = it }, maxPlaceholder = stringResource(string.filter_price_placeholder_max)
                    )
                }

                // --- Площа ---
                FilterSection(title = stringResource(string.filter_section_area)){
                    RangeInputRow(
                        minValue = minArea, onMinChange = { minArea = it }, minPlaceholder = stringResource(string.filter_area_placeholder_min),
                        maxValue = maxArea, onMaxChange = { maxArea = it }, maxPlaceholder = stringResource(string.filter_area_placeholder_max)
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
                                isSelected = selectedRooms == room,
                                onClick = { selectedRooms = room },
                                modifier = if (room == studioStr) Modifier.weight(1f) else Modifier.defaultMinSize(minWidth = 48.dp)
                            )
                        }
                    }
                }

                // --- Тип будівлі (Dropdown) ---
                FilterSection(title = stringResource(string.filter_section_building_type)){
                    CustomDropdown(
                        expanded = buildingTypeExpanded,
                        onExpandedChange = { buildingTypeExpanded = it },
                        selectedValue = selectedTypeExpanded,
                        onValueSelected = { selectedTypeExpanded = it; buildingTypeExpanded = false },
                        options = listOf(
                            allTypesStr,
                            stringResource(string.filter_building_new),
                            stringResource(string.filter_building_khrushchovka),
                            stringResource(string.filter_building_house),
                            stringResource(string.filter_building_apartment),
                            stringResource(string.filter_building_room)
                        )
                    )
                }

                // --- Поверх (Dropdown) ---
                FilterSection(title = stringResource(string.filter_section_floor)){
                    CustomDropdown(
                        expanded = floorExpanded,
                        onExpandedChange = { floorExpanded = it },
                        selectedValue = selectedFloor,
                        onValueSelected = { selectedFloor = it; floorExpanded = false },
                        options = listOf(
                            anyFloorStr,
                            stringResource(string.filter_floor_first),
                            stringResource(string.filter_floor_middle),
                            stringResource(string.filter_floor_last)
                        )
                    )
                }

                // --- Паркінг ---
                FilterSection(title = stringResource(string.filter_section_parking)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        val parkingOptions = listOf(
                            allStr,
                            stringResource(string.filter_parking_garage),
                            stringResource(string.filter_parking_street),
                            stringResource(string.filter_parking_secured)
                        )
                        parkingOptions.forEach { parking ->
                            CustomChip(
                                text = parking,
                                isSelected = selectedParking == parking,
                                onClick = { selectedParking = parking }
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
                            checked = petsAllowed, onCheckedChange = { petsAllowed = it}
                        )
                        HorizontalDivider(color = SwipeHomeTheme.colors.outline, modifier = Modifier.padding(vertical = 12.dp))

                        SwitchRowExpanded(
                            title = stringResource(string.filter_amenities_elevator),
                            subtitle = stringResource(string.filter_amenities_elevator_desc),
                            icon = painterResource(Res.drawable.ic_elevator),
                            checked = hasElevator, onCheckedChange = { hasElevator = it}
                        )
                        HorizontalDivider(color = SwipeHomeTheme.colors.outline, modifier = Modifier.padding(vertical = 12.dp))

                        SwitchRowExpanded(
                            title = stringResource(string.filter_amenities_furniture),
                            subtitle = stringResource(string.filter_amenities_furniture_desc),
                            icon = painterResource(Res.drawable.ic_sofa),
                            checked = withFurniture, onCheckedChange = { withFurniture = it}
                        )
                        HorizontalDivider(color = SwipeHomeTheme.colors.outline, modifier = Modifier.padding(vertical = 12.dp))

                        SwitchRowExpanded(
                            title = stringResource(string.filter_amenities_balcony),
                            subtitle = stringResource(string.filter_amenities_balcony_desc),
                            icon = painterResource(Res.drawable.ic_balcony),
                            checked = hasBalcony, onCheckedChange = { hasBalcony = it}
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Допоміжний компонент для заголовків секцій
@Composable
fun FilterSection(title: String, content: @Composable () -> Unit){
    Column {
        Text(
            text = title,
            style = SwipeHomeTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            color = SwipeHomeTheme.colors.neutral,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
fun CustomChip(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier){
    val bdColor = if (isSelected) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface
    val textColor = if (isSelected) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.onSurface

    Box(
        modifier = modifier
            .clip(SwipeHomeTheme.shapes.smallShape)
            .background(bdColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = text, style = SwipeHomeTheme.typography.label, color = textColor)
    }
}

@Composable
fun RangeInputRow(
    minValue: String, onMinChange: (String) -> Unit, minPlaceholder: String,
    maxValue: String, onMaxChange: (String) -> Unit, maxPlaceholder: String
){
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CustomTextField(
            value = minValue, onValueChange = onMinChange,
            label = stringResource(string.filter_price_from), placeholder = minPlaceholder, modifier = Modifier.weight(1f)
        )
        Text("-", color = SwipeHomeTheme.colors.onSurfaceSecondary)
        CustomTextField(
            value = maxValue, onValueChange = onMaxChange,
            label = stringResource(string.filter_price_to), placeholder = maxPlaceholder, modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String, modifier: Modifier = Modifier){
    TextField(
        value = value,
        onValueChange = { if (it.all { char -> char.isDigit() }) onValueChange(it) },
        label = { Text(label, color = SwipeHomeTheme.colors.onSurfaceSecondary) },
        placeholder = { Text(placeholder, color = SwipeHomeTheme.colors.onSurfaceSecondary) },
        modifier = modifier.height(64.dp),
        shape = SwipeHomeTheme.shapes.smallShape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SwipeHomeTheme.colors.surface,
            unfocusedContainerColor = SwipeHomeTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedValue: String,
    onValueSelected: (String) -> Unit,
    options: List<String>
){
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = Modifier.fillMaxWidth()
    ){
        TextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
                .height(56.dp),
            shape = SwipeHomeTheme.shapes.smallShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SwipeHomeTheme.colors.surface,
                unfocusedContainerColor = SwipeHomeTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.background(SwipeHomeTheme.colors.surface)
        ){
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = SwipeHomeTheme.colors.onSurface) },
                    onClick = { onValueSelected(option) }
                )
            }
        }
    }
}

// Допоміжний компонент для перемикачів
@Composable
fun SwitchRowExpanded(title: String, icon: Painter, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .background(SwipeHomeTheme.colors.primary, SwipeHomeTheme.shapes.verySmallShape)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = SwipeHomeTheme.typography.body, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onSurface)
            Text(subtitle, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SwipeHomeTheme.colors.primary,
                checkedTrackColor = SwipeHomeTheme.colors.primary.copy(alpha = 0.5f),
            )
        )
    }
}
