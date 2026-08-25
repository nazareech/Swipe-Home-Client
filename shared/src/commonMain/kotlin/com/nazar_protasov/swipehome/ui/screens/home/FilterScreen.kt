package com.nazar_protasov.swipehome.ui.screens.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.theme.BackgroundLight
import com.nazar_protasov.swipehome.ui.theme.ErrorRed
import com.nazar_protasov.swipehome.ui.theme.PrimaryGreen
import com.nazar_protasov.swipehome.ui.theme.SecondaryGreen
import com.nazar_protasov.swipehome.ui.theme.SurfaceGray
import com.nazar_protasov.swipehome.ui.theme.SurfaceWhite
import com.nazar_protasov.swipehome.ui.theme.TextPrimary
import com.nazar_protasov.swipehome.ui.theme.TextSecondary
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.Res.string
import mymultiplatformproject.shared.generated.resources.btn_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_balcony
import mymultiplatformproject.shared.generated.resources.ic_elevator
import mymultiplatformproject.shared.generated.resources.ic_pets_allowed
import mymultiplatformproject.shared.generated.resources.ic_sofa
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class FilterScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        // --- Стани фільтрів ---
        var selectedCategory by remember { mutableStateOf("Квартира") }
        var selectedDealType by remember { mutableStateOf("Все") }
        var minPrice by remember { mutableStateOf("") }
        var maxPrice by remember { mutableStateOf("") }
        var minArea by remember { mutableStateOf("") }
        var maxArea by remember { mutableStateOf("") }
        var selectedRooms by remember { mutableStateOf("1") }

        // Стани для випадаючих списків
        var buildingTypeExpanded by remember { mutableStateOf(false) }
        var selectedTypeExpanded by remember { mutableStateOf("Всі типи") }

        var floorExpanded by remember { mutableStateOf(false) }
        var selectedFloor by remember { mutableStateOf("Будь-який") }

        var selectedParking by remember { mutableStateOf("Все") }

        // Світчі
        var petsAllowed by remember { mutableStateOf(false) }
        var withFurniture by remember { mutableStateOf(false) }
        var hasElevator by remember { mutableStateOf(false) }
        var hasBalcony by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        // --- Великий заголовок ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ){
                          Text(
                                text = "Фільтри",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary // Колір для заголовка
                            )
                            Text(
                                text = "Очистити",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ErrorRed, // Колір для очищення
                                modifier = Modifier
                                    .clickable {
                                        selectedCategory = "Квартира"
                                        selectedDealType = "Все"
                                        minPrice = ""; maxPrice = ""
                                        minArea = ""; maxArea = ""
                                        selectedRooms = "1"
                                        selectedTypeExpanded = "Всі типи"
                                        selectedFloor = "Будь-який"
                                        selectedParking = "Все"
                                        petsAllowed = false; hasElevator = false; withFurniture = false
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
                      containerColor = BackgroundLight // Колір фону для фону
                    )
                )
            },
            bottomBar = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight)
                    .padding(16.dp)
                    .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    Button(
                        onClick = {
                        // TODO: Зібрати всі дані, оновити глобальний стан і закрити екран
                            navigator.pop()
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ){
                        Text("Застосувати фільтри", fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                    Button(
                        onClick = {
                        // TODO: Зібрати всі дані, оновити глобальний стан і закрити екран
                            navigator.pop()
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ){
                        Text("Скасувати", fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }
            },
            containerColor = BackgroundLight // Загальний фон
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
                FilterSection(title = "ТИП НЕРУХОМОСТІ"){
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        val categories = listOf("Квартира", "Будинок", "Кімнати")
                        categories.forEach { category ->
                            CustomChip(
                                text = category,
                                isSelected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // --- Тип угоди ---
                FilterSection(title = "ТИП УГОДИ"){
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        val typeOfDeal = listOf("Все", "Винайм", "Продаж")
                        typeOfDeal.forEach { type ->
                            CustomChip(
                                text = type,
                                isSelected = selectedDealType == type,
                                onClick = { selectedDealType = type },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // --- Ціна ---
                FilterSection(title = "ЦІНА ($)"){
                    RangeInputRow(
                        minValue = minPrice, onMinChange = { minPrice = it }, minPlaceholder = "0",
                        maxValue = maxPrice, onMaxChange = { maxPrice = it }, maxPlaceholder = "500,000+"
                    )
                }

                // --- Площа ---
                FilterSection(title = "ПЛОЩА (М²)"){
                    RangeInputRow(
                        minValue = minArea, onMinChange = { minArea = it }, minPlaceholder = "20",
                        maxValue = maxArea, onMaxChange = { maxArea = it }, maxPlaceholder = "200+"
                    )
                }

                // --- Кількість кімнат ---
                FilterSection(title = "КІЛЬКІСТЬ КІМНАТ") {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        val roomCounts = listOf("1", "2", "3", "4", "5", "Студія")
                        roomCounts.forEach { room ->
                            CustomChip(
                                text = room,
                                isSelected = selectedRooms == room,
                                onClick = { selectedRooms = room },
                                modifier = if (room == "Студія") Modifier.weight(1f) else Modifier.defaultMinSize(minWidth = 48.dp)
                            )
                        }
                    }
                }

                // --- Тип будівлі (Dropdown) ---
                FilterSection(title = "ТИП БУДІВЛІ"){
                    CustomDropdown(
                        expanded = buildingTypeExpanded,
                        onExpandedChange = { buildingTypeExpanded = it },
                        selectedValue = selectedTypeExpanded,
                        onValueSelected = { selectedTypeExpanded = it; buildingTypeExpanded = false },
                        options = listOf("Всі типи", "Новобудова", "Хрущовка", "Будинок", "Квартира", "Кімната")
                    )
                }

                // --- Поверх (Dropdown) ---
                FilterSection(title = "ПОВЕРХ"){
                    CustomDropdown(
                        expanded = floorExpanded,
                        onExpandedChange = { floorExpanded = it },
                        selectedValue = selectedFloor,
                        onValueSelected = { selectedFloor = it; floorExpanded = false },
                        options = listOf("Будь-який", "Перший", "Середній", "Останній")
                    )
                }

                // --- Паркінг ---
                FilterSection(title = "ПАРКІНГ") {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        val parkingOptions = listOf("Все", "В гаражі", "На вулиці", "Під охороною")
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
                   shape = RoundedCornerShape(12.dp),
                   colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                   elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                   modifier = Modifier.fillMaxWidth()
               ){
                   Column(modifier = Modifier.padding(16.dp)) {
                       SwitchRowExpanded(
                           title = "Тварини", subtitle = "Можна з домашніми улюбленцями",
                           icon = painterResource(Res.drawable.ic_pets_allowed),
                           checked = petsAllowed, onCheckedChange = { petsAllowed = it}
                       )
                       HorizontalDivider(color = SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))

                       SwitchRowExpanded(
                           title = "Ліфт", subtitle = "Обов'язкова наявність ліфта",
                           icon = painterResource(Res.drawable.ic_elevator),
                           checked = hasElevator, onCheckedChange = { hasElevator = it}
                       )
                       HorizontalDivider(color = SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))

                       SwitchRowExpanded(
                           title = "Меблі", subtitle = "Повність мебльовано",
                           icon = painterResource(Res.drawable.ic_sofa),
                           checked = withFurniture, onCheckedChange = { withFurniture = it}
                       )
                       HorizontalDivider(color = SurfaceGray, modifier = Modifier.padding(vertical = 12.dp))

                       SwitchRowExpanded(
                           title = "Балкон", subtitle = "Наявність балкону",
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
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground, // Колір для підписів
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}
@Composable
fun CustomChip( text: String, isSelected: Boolean, onClick: () -> Unit){
    val bdColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bdColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

@Composable
fun CustomChip(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier){
    val bdColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bdColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

@Composable
fun RangeInputRow(
    minValue: String, onMinChange: (String) -> Unit, minPlaceholder: String,
    maxValue: String, onMaxChange: (String) -> Unit, maxPlaceholder: String
){
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CustomTextField(
            value = minValue, onMinChange,
            label = "Від", placeholder = minPlaceholder, modifier = Modifier.weight(1f)
        )
        Text("-", color = TextSecondary)
        CustomTextField(
            value = maxValue, onValueChange = onMaxChange,
            label = "До", placeholder = maxPlaceholder, modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String, modifier: Modifier = Modifier){
    TextField(
        value = value,
        onValueChange = { if (it.all { char -> char.isDigit() }) onValueChange(it) },
        label = { Text(label, color = TextSecondary) },
        placeholder = { Text(placeholder, color = TextSecondary) },
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SurfaceGray,
            unfocusedContainerColor = SurfaceGray,
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
                .menuAnchor()
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SurfaceGray,
                unfocusedContainerColor = SurfaceGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.background(SurfaceGray)
        ){
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = TextPrimary) },
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
        Icon(icon, contentDescription = null, modifier = Modifier
            .size(24.dp)
            .background(SecondaryGreen, RoundedCornerShape(4.dp))) // SecondaryGreen для фону іконок
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(subtitle, fontSize = 12.sp, color = TextSecondary)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            )
        )
    }
}