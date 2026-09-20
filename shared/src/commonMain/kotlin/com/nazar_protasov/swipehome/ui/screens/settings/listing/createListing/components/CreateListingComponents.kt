package com.nazar_protasov.swipehome.ui.screens.settings.listing.createListing.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.screens.settings.listing.CreateListingScreenModel
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import mymultiplatformproject.shared.generated.resources.ic_badge
import mymultiplatformproject.shared.generated.resources.ic_balcony
import mymultiplatformproject.shared.generated.resources.ic_buildindings
import mymultiplatformproject.shared.generated.resources.ic_check_circle_outline
import mymultiplatformproject.shared.generated.resources.ic_close
import mymultiplatformproject.shared.generated.resources.ic_diia_logo
import mymultiplatformproject.shared.generated.resources.ic_document
import mymultiplatformproject.shared.generated.resources.ic_dollar
import mymultiplatformproject.shared.generated.resources.ic_dumbbell
import mymultiplatformproject.shared.generated.resources.ic_elevator
import mymultiplatformproject.shared.generated.resources.ic_hint
import mymultiplatformproject.shared.generated.resources.ic_home_area
import mymultiplatformproject.shared.generated.resources.ic_image_upload
import mymultiplatformproject.shared.generated.resources.ic_location_pin
import mymultiplatformproject.shared.generated.resources.ic_parking
import mymultiplatformproject.shared.generated.resources.ic_pets_allowed
import mymultiplatformproject.shared.generated.resources.ic_pool
import mymultiplatformproject.shared.generated.resources.ic_save_floppy
import mymultiplatformproject.shared.generated.resources.ic_shield_security
import mymultiplatformproject.shared.generated.resources.ic_snowflake
import mymultiplatformproject.shared.generated.resources.ic_sofa
import mymultiplatformproject.shared.generated.resources.ic_user_verified
import org.jetbrains.compose.resources.painterResource

enum class PropertyType{
    APARTMENT,
    HOUSE,
    STUDIO
}
enum class VerificationMethods{
    DIIA,
    ID,
    DOC
}

// --- НАВІГАЦІЯ ТА ПРОГРЕС ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WizardTopBar(currentStep: Int, totalSteps: Int, onBackClick: () -> Unit, onSaveClick: () -> Unit){
    val titles = listOf("Основна інформація", "Деталі та особливості", "Медіа", "Фінальний крок")

    Column(modifier = Modifier.background(SwipeHomeTheme.colors.background)) {
        TopAppBar(
            title = {
                Text(
                    text = if (currentStep == 1) "Створення оголошення" else "Додати оголошення",
                    style = SwipeHomeTheme.typography.subheadline,
                    color = SwipeHomeTheme.colors.onSurfaceSecondary
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = null, tint = SwipeHomeTheme.colors.onSurfaceSecondary)
                }
            },
            actions = {
                if (currentStep > 1 && currentStep != totalSteps){
                    TextButton(onClick = onSaveClick) {
                        Text("Зберегти", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.primary)
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = SwipeHomeTheme.colors.background)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("КРОК ${currentStep}/${totalSteps}", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onSurfaceSecondary)
            Text(titles[currentStep - 1], style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
        }
        SegmentedProgressBar(currentStep = currentStep, totalSteps = totalSteps, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
    }
}

@Composable
fun SegmentedProgressBar(currentStep: Int, totalSteps: Int, modifier: Modifier = Modifier){
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
        for(i in 1..totalSteps){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (i <= currentStep) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.neutral)
            )
        }
    }
}

@Composable
fun WizardBottomBar(currentStep: Int, totalSteps: Int,onSaveClick: () -> Unit, onNext: () -> Unit, onPrev: () -> Unit){
    Column(modifier = Modifier.background(SwipeHomeTheme.colors.background)) {
        HorizontalDivider( color = SwipeHomeTheme.colors.outline)
        Row(
            modifier = Modifier.fillMaxWidth().padding( 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                onClick = onPrev,
                colors = ButtonDefaults.buttonColors( SwipeHomeTheme.colors.primary, contentColor = SwipeHomeTheme.colors.onPrimary),
                shape = SwipeHomeTheme.shapes.mediumShape,
                modifier = Modifier.weight(1f).height(50.dp)
            ){
                if (currentStep > 1) {
                    Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = null, tint = SwipeHomeTheme.colors.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Назад", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold)
                } else {
                    Text("Скасувати", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors( SwipeHomeTheme.colors.primary, contentColor = SwipeHomeTheme.colors.onPrimary),
                shape = SwipeHomeTheme.shapes.mediumShape,
                modifier = Modifier.weight(1f).height(50.dp)
            ){
                Text(
                    text = if (currentStep == totalSteps) "Відправити на верифікацію" else "Продовжити",
                    style = SwipeHomeTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                if (currentStep < totalSteps) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painterResource(Res.drawable.ic_arrow_next),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = SwipeHomeTheme.colors.onPrimary
                    )
                }
            }
        }

        if (currentStep == 4) {
            OutlinedButton(
                onClick = { onSaveClick() },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
            ){
                Icon(painterResource(Res.drawable.ic_save_floppy), contentDescription = null, tint = SwipeHomeTheme.colors.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Пройти верифікацію пізніше\n(зберегти чернетку)", color = SwipeHomeTheme.colors.primary, style = SwipeHomeTheme.typography.caption)
            }
        }
    }
}

// --- ОСНОВНА ІНФОРМАЦІЯ ---
@Composable
fun Step1BasicInfo(
    selectedType: PropertyType, onTypeSelected: (PropertyType) -> Unit,
    address: String, onAddressChange: (String) -> Unit,
    area: String, onAreaChange: (String) -> Unit,
    price: String, onPriceChange: (String) -> Unit
){
    Column {
        Text("Який тип вашої нерухомості?", style = SwipeHomeTheme.typography.headline, fontWeight = FontWeight.Bold, lineHeight = 34.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Почнемо з базових деталей. Це допоможе правильно класифікувати та швидше знайти орендарів.",
            style = SwipeHomeTheme.typography.body, color = SwipeHomeTheme.colors.onSurfaceSecondary, lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Тип нерухомості", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)){
            PropertyTypeCard("Квартира", painterResource(Res.drawable.ic_buildindings), selectedType == PropertyType.APARTMENT, Modifier.weight(1f)) { onTypeSelected(PropertyType.APARTMENT) }
            PropertyTypeCard("Будинок", painterResource(Res.drawable.ic_buildindings), selectedType == PropertyType.HOUSE, Modifier.weight(1f)) { onTypeSelected(PropertyType.HOUSE) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        PropertyTypeCard("Студія", painterResource(Res.drawable.ic_sofa), selectedType == PropertyType.STUDIO, Modifier.fillMaxWidth()) { onTypeSelected(PropertyType.STUDIO) }

        Spacer(modifier = Modifier.height(24.dp))

        // Адреса
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text("Адреса об'єкта", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }){
                Icon(painterResource(Res.drawable.ic_location_pin), contentDescription = null, tint = SwipeHomeTheme.colors.primary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Обрати на карті", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.primary)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            leadingIcon = { Icon(painterResource(Res.drawable.ic_location_pin), contentDescription = null, tint = SwipeHomeTheme.colors.neutral) },
            trailingIcon = {
                Box(modifier = Modifier.padding(end = 8.dp).clip(SwipeHomeTheme.shapes.mediumShape).background(SwipeHomeTheme.colors.surface).padding(8.dp, 4.dp)){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painterResource(Res.drawable.ic_location_pin),
                            contentDescription = null,
                            tint = SwipeHomeTheme.colors.neutral
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Google Places", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
                    }
                }
           },
           modifier = Modifier.fillMaxWidth(),
           shape = SwipeHomeTheme.shapes.mediumShape,
           colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SwipeHomeTheme.colors.primary, unfocusedBorderColor = SwipeHomeTheme.colors.neutral)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically){
            Icon(painterResource(Res.drawable.ic_check_circle_outline), contentDescription = null, tint = SwipeHomeTheme.colors.primary, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Підтверджено: $address", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = SwipeHomeTheme.colors.neutral)
        Spacer(modifier = Modifier.height(24.dp))

        // Площа
        CustomInputField("Загальна площа", area, onAreaChange, "м²", painterResource(Res.drawable.ic_home_area))
        Spacer(modifier = Modifier.height(16.dp))
        CustomInputField("Вартість оренди / місяць", price, onPriceChange, "USD", painterResource(Res.drawable.ic_dollar))
    }
}

@Composable
fun PropertyTypeCard(title: String, icon: Painter, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit){
    Card(
        modifier = modifier.height(100.dp).border(1.dp, if (isSelected) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.neutral, SwipeHomeTheme.shapes.mediumShape).clickable{ onClick() },
        colors = CardDefaults.cardColors(containerColor = if(isSelected) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface),
        shape = SwipeHomeTheme.shapes.mediumShape
    ){
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Icon(icon, contentDescription = null, tint = if(isSelected) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = SwipeHomeTheme.typography.label, color = if(isSelected) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral)
        }
    }
}

@Composable
fun CustomInputField(label: String, value: String, onValueChange: (String) -> Unit, suffix: String, icon: Painter){
    // Стан для пам'яті позиції курсора в текстовому полі
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }

    // Якщо значення "прилетіло" ззовні
    if (textFieldValue.text != value) {
        textFieldValue = textFieldValue.copy(
            text = value,
            selection = TextRange(value.length) //Ставимо курсор в кінець
        )
    }

    Text(label, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val text = newValue.text

            // Залишаємо тільки цифри, крапки та коми
            val filtered = text.filter { it.isDigit() || it == '.'  || it == ','}

            // Шукаємо першу позицію крапки або коми
            val separatorIndex = filtered.indexOfFirst{ it == '.'  || it == ',' }

            val cleanString = if (separatorIndex != -1) {
                // Беремо ліву частину тексту із першим роздільником
                val before = filtered.substring(0, separatorIndex + 1)
                // З правої частини тексту примусова видаляємо всі інші крапки/коми
                val after = filtered.substring(separatorIndex + 1).filter { it.isDigit() }
                before + after
            } else {
                filtered
            }

            // Якщо результат починається з крапки або коми, додаємо "0" попереду
            val finalString = if (cleanString.startsWith(".") || cleanString.startsWith(",")) {
                "0$cleanString"
            } else {
                cleanString
            }

            onValueChange(finalString)
        },
        leadingIcon = { Icon(icon, contentDescription = null, tint = SwipeHomeTheme.colors.neutral) },
        trailingIcon = { Text(suffix, modifier = Modifier.padding(end = 16.dp), color = SwipeHomeTheme.colors.onSurfaceSecondary)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = SwipeHomeTheme.shapes.mediumShape,
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SwipeHomeTheme.colors.primary, unfocusedBorderColor = SwipeHomeTheme.colors.neutral)
    )
}

// --- ДЕТАЛІ ТА ОСОБЛИВОСТІ
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Details(
    description: String, onDescriptionChange: (String) -> Unit,

    countBedrooms: Int, onCountBedrooms: (Int) -> Unit,
    countBathrooms: Int, onCountBathrooms: (Int) -> Unit,
    floorNumber: Int, onFloorNumber: (Int) -> Unit,

    allowedPets: Boolean, onAllowedPets: (Boolean) -> Unit,
    isParking: Boolean, onIsParking: (Boolean) -> Unit,
    isFurnished: Boolean, onIsFurnished: (Boolean) -> Unit,
    isBalcony: Boolean, onIsBalcony: (Boolean) -> Unit,
    isCondition: Boolean, onIsCondition: (Boolean) -> Unit,

    gymIsSelect: Boolean, onGymIsSelect: (Boolean) -> Unit,
    poolIsSelect: Boolean, onPoolIsSelect: (Boolean) -> Unit,
    elevatorIsSelect: Boolean, onElevatorIsSelect: (Boolean) -> Unit,
    securityIsSelect: Boolean, onSecurityIsSelect: (Boolean) -> Unit,
){
    Column {
        Text("Кімнати та зручності", style = SwipeHomeTheme.typography.headline, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Вкажіть параметри та особливості, які вигідно виділяють ваше житло черед інших", style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary)

        Spacer(modifier = Modifier.height(24.dp))

        // Лічильники
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = SwipeHomeTheme.shapes.mediumShape,
            colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
            border = BorderStroke(1.dp, SwipeHomeTheme.colors.neutral)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ContentRow("Спальні", "Кількість окремих спалень", countBedrooms, onCountBedrooms)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SwipeHomeTheme.colors.neutral)
                ContentRow("Ванні кімнати", "Суміжні або роздільні санвузли", countBathrooms, onCountBathrooms)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SwipeHomeTheme.colors.neutral)
                ContentRow("Поверх", "Поверх / кількість поверхів", floorNumber, onFloorNumber)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Особливості житла", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureChip(painterResource(Res.drawable.ic_pets_allowed), "Дозволено з тваринами", allowedPets) { onAllowedPets(!allowedPets)}
            FeatureChip(painterResource(Res.drawable.ic_parking), "Паркомісце включено", isParking) {onIsParking(!isParking)}
            FeatureChip(painterResource(Res.drawable.ic_sofa), "З меблями", isFurnished) {onIsFurnished(!isFurnished)}
            FeatureChip(painterResource(Res.drawable.ic_balcony), "Балкон / Тераса", isBalcony) {onIsBalcony(!isBalcony)}
            FeatureChip(painterResource(Res.drawable.ic_snowflake), "Кондиціонер", isCondition) {onIsCondition(!isCondition)}
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Зручності в будинку", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
            AmenityCard("Спортзал", painterResource(Res.drawable.ic_dumbbell), gymIsSelect, Modifier.weight(1f)) {onGymIsSelect(!gymIsSelect)}
            AmenityCard("Басейн", painterResource(Res.drawable.ic_pool), poolIsSelect, Modifier.weight(1f)) {onPoolIsSelect(!poolIsSelect)}
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
            AmenityCard("Ліфт", painterResource(Res.drawable.ic_elevator), elevatorIsSelect, Modifier.weight(1f)) {onElevatorIsSelect(!elevatorIsSelect)}
            AmenityCard("Охорона", painterResource(Res.drawable.ic_shield_security), securityIsSelect, Modifier.weight(1f)) {onSecurityIsSelect(!securityIsSelect)}
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text("Опис оголошення", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
            Text("${description.length} / 1000 символів", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = { Text("Опишіть особливості квартири (наприклад перша здача після авторського ремонту...", color = SwipeHomeTheme.colors.onSurfaceSecondary) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = SwipeHomeTheme.shapes.mediumShape,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SwipeHomeTheme.colors.primary, unfocusedBorderColor = SwipeHomeTheme.colors.neutral)
        )
    }
}

@Composable
fun ContentRow(title: String, subtitle: String, value: Int, onValueChange: (Int) -> Unit){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
        Column(modifier = Modifier.weight(1f)){
            Text(title, style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onSurfaceSecondary)
            Text(subtitle, style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary)
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            Box(modifier = Modifier.size(32.dp).border(1.dp, SwipeHomeTheme.colors.neutral, CircleShape).clickable{ if (value > 1) onValueChange(value - 1) }, contentAlignment = Alignment.Center){
                Text("-", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.neutral)
            }
        }

        Text(value.toString(), modifier = Modifier.padding(horizontal = 16.dp), style = SwipeHomeTheme.typography.body, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(SwipeHomeTheme.colors.primary).border(1.dp, SwipeHomeTheme.colors.neutral, CircleShape).clickable{ onValueChange(value + 1) }, contentAlignment = Alignment.Center){
                Text("+", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.onPrimary)
        }
    }
}

@Composable
fun FeatureChip(icon: Painter, text: String, isChecked: Boolean, onClick: () -> Unit){
    Row(
        modifier = Modifier.border(1.dp, if(isChecked) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.neutral, SwipeHomeTheme.shapes.largeShape).background(if(isChecked) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface, SwipeHomeTheme.shapes.largeShape).padding(horizontal = 12.dp, vertical = 8.dp).clickable{ onClick() },
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(icon, contentDescription = null, tint = SwipeHomeTheme.colors.neutral)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
    }
}

@Composable
fun AmenityCard(title: String, icon: Painter, isChecked: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit){
    Card(
        modifier = modifier.height(100.dp).border(1.dp, if(isChecked) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.neutral, SwipeHomeTheme.shapes.mediumShape).clickable{ onClick() },
        colors = CardDefaults.cardColors(containerColor = if(isChecked) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface),
        shape = SwipeHomeTheme.shapes.mediumShape
    ){
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically){
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(SwipeHomeTheme.shapes.verySmallShape)
                    .border(2.dp, if (isChecked) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral, SwipeHomeTheme.shapes.verySmallShape)
                    .background(if (isChecked) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface),
                contentAlignment = Alignment.Center
            ){
                if (isChecked) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check_circle_outline),
                        contentDescription = null,
                        tint = SwipeHomeTheme.colors.onPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)){
                Icon(icon, contentDescription = null, tint = if(isChecked) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral, modifier = Modifier.size(20.dp))
                Text(title, style = SwipeHomeTheme.typography.label, color = if(isChecked) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral)
            }
        }
    }
}

// --- МЕДІА ---

@Composable
fun Step3Media(
    photos: List<String>,
    onAddPhoto: () -> Unit,
    onRemovePhoto: (Int) -> Unit,
    onMovePhoto: (Int, Int) -> Unit
){
    // Отримуємо доступ до контролера вібрації
    val hapticFeedback = LocalHapticFeedback.current

    Column {
        Text("Завантажити фото", style = SwipeHomeTheme.typography.headline, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Якісні фото суттєво підвищують інтерес до нерухомості. Завантажте щонайменше 3 фото для проходження модерації.", style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary, lineHeight = 20.sp)

        Spacer(modifier = Modifier.height(24.dp))

        // Прогрес завантаження
        val progress = (photos.size.toFloat() / 5f).coerceIn(0f, 1f)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = SwipeHomeTheme.shapes.mediumShape,
            colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
            border = BorderStroke(1.dp, SwipeHomeTheme.colors.neutral)
        ) {
            Column(modifier = Modifier.padding(16.dp)){
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Text("Завантажено \nфото", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
                    Column(horizontalAlignment = Alignment.End){
                        Text("${photos.size} / 5 рекомендовано", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.primary)
                        Text("(мінімум 3 фото)", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(SwipeHomeTheme.colors.neutral)){
                    Box(modifier = Modifier.fillMaxWidth(progress).height(6.dp).background(SwipeHomeTheme.colors.primary))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painterResource(Res.drawable.ic_user_verified), contentDescription = null, tint = SwipeHomeTheme.colors.primary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Для верифікованих власників публікація відбудеться одразу після цього кроку", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.neutral, lineHeight = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (photos.isEmpty()) {
            // Зона Drop
            val stroke = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
            val strokeColor = SwipeHomeTheme.colors.neutral
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = strokeColor,
                            style = stroke,
                            cornerRadius = CornerRadius(16.dp.toPx())
                        )
                    }
                    .clip(SwipeHomeTheme.shapes.mediumShape)
                    .background(SwipeHomeTheme.colors.outline)
                    .clickable {
                        onAddPhoto()
                        // Викликаємо вібрацію при кліку
                        // Доступні базові варіанти: LongPress або TextHandleMove
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                       },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(56.dp).clip(CircleShape)
                            .background(SwipeHomeTheme.colors.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource(Res.drawable.ic_image_upload),
                            contentDescription = null,
                            tint = SwipeHomeTheme.colors.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Натисни для завантаження фото",
                        style = SwipeHomeTheme.typography.body,
                        fontWeight = FontWeight.Bold,
                        color = SwipeHomeTheme.colors.neutral,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "JPG, PNG",
                        style = SwipeHomeTheme.typography.caption,
                        color = SwipeHomeTheme.colors.neutral
                    )
                }
            }
        } else {
            // Стани для Drag & Drop
            val lazyGridState = rememberLazyGridState()
            val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
                // Забороняємо перетягувати кнопку додати
                if (from.index < photos.size && to.index < photos.size) {
                     onMovePhoto(from.index, to.index)
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
            }
            // Сітка з фотографіями та кнопкою "Додати ще"
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = lazyGridState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // Використовуємо items з явним ключем
                items(photos.size, key = { photos[it] }) { index ->
                    ReorderableItem(reorderableLazyGridState, key = photos[index]) { isDragging ->
                        // Ефект збільшення при перетягуванні
                        val itemModifier = if (isDragging) {
                            Modifier.graphicsLayer { scaleX = 1.05f; scaleY = 1.05f; alpha = 0.9f }
                        } else {
                            Modifier
                        }

                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        Box(
                            modifier = itemModifier.longPressDraggableHandle()
                        ) {
                            PhotoItemCard(
                                photoUrl = photos[index], // Передаємо URL
                                isMain = index == 0,
                                hapticFeedback = hapticFeedback,
                                onRemove = {
                                    onRemovePhoto(index)
                                }
                            )
                        }
                    }
                }

                // Маленька кнопка додавання наступного фото
                item(key = "add_button_key") {
                    val stroke = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                    val color = SwipeHomeTheme.colors.neutral
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .drawBehind {
                                drawRoundRect(
                                    color = color,
                                    style = stroke,
                                    cornerRadius = CornerRadius(16.dp.toPx())
                                )
                            }
                            .clip(SwipeHomeTheme.shapes.mediumShape)
                            .background(SwipeHomeTheme.colors.outline)
                            .clickable {
                                onAddPhoto()
                                // Викликаємо вібрацію при кліку
                                // Доступні базові варіанти: LongPress або TextHandleMove
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                               },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painterResource(Res.drawable.ic_image_upload), contentDescription = null, tint = SwipeHomeTheme.colors.neutral)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().clip(SwipeHomeTheme.shapes.mediumShape).background(SwipeHomeTheme.colors.primary).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painterResource(Res.drawable.ic_hint), contentDescription = null, tint = SwipeHomeTheme.colors.onPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Затисніть фото, щоб змінити порядок", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Medium, color = SwipeHomeTheme.colors.onPrimary, modifier = Modifier.weight(1f))
//            Icon(painterResource(Res.drawable.DragIndicator), contentDescription = null, tint = SwipeHomeTheme.colors.onPrimary)
        }
    }
}

@Composable
fun PhotoItemCard(photoUrl: String, isMain: Boolean, hapticFeedback: HapticFeedback, onRemove: () -> Unit){
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(SwipeHomeTheme.shapes.smallShape)
            .background(SwipeHomeTheme.colors.neutral.copy(alpha = 0.3f))
            .border(
                width = if (isMain) 2.dp else 0.dp,
                color = if (isMain) SwipeHomeTheme.colors.neutral else SwipeHomeTheme.colors.surface,
                shape = SwipeHomeTheme.shapes.smallShape
            )
    ) {
        // Тут буде AsyncImage для реального фото
        AsyncImage(
            model = photoUrl, // Coli автоматично читає ByteArray
            contentDescription = null,
            modifier = Modifier.fillMaxSize(), // Займаємо всі 120.dp нашого Box
            contentScale = ContentScale.Crop // Масштабуємо фото, щоб воно заповнило весь Box без спотворень
        )

        // Бейдж "Головне" для першого фото
        if(isMain){
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(SwipeHomeTheme.colors.primary.copy(alpha = 0.8f))
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Головне", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onPrimary)
            }
        }

        // Кнопка видалення
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(SwipeHomeTheme.colors.surface.copy(alpha = 0.8f))
                .clickable {
                    onRemove()
                    // Викликаємо вібрацію при кліку
                    // Доступні базові варіанти: LongPress або TextHandleMove
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                   },
            contentAlignment = Alignment.Center
        ){
            Icon(painterResource(Res.drawable.ic_close), contentDescription = null, tint = SwipeHomeTheme.colors.onSurfaceSecondary, modifier = Modifier.size(14.dp))
        }
    }
}

// --- ВЕРИФІКАЦІЯ ---

@Composable
fun Step4Verification(verificationMethod: VerificationMethods, onVerificationMethod: (VerificationMethods) -> Unit){
    Column {
        Row(verticalAlignment = Alignment.CenterVertically){
            Icon(painterResource(Res.drawable.ic_user_verified), contentDescription = null, tint = SwipeHomeTheme.colors.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Фінальний крок", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Верифікація власника", style = SwipeHomeTheme.typography.headline, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Підтвердіть особу для безпеки спільноти та швидкого отримання статусу надійного орендодавця.", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold, lineHeight = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().clip(SwipeHomeTheme.shapes.mediumShape).background(SwipeHomeTheme.colors.primary).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painterResource(Res.drawable.ic_hint), contentDescription = null, tint = SwipeHomeTheme.colors.onPrimary)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Цей крок необхідний для першого розміщення. Якщо ви вже верифіковані, наступні оголошення публікуються миттєво", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
        }

        Spacer(modifier = Modifier.height(24.dp))

        VerificationCard(VerificationMethods.DIIA,"Верифікація через Дію\n(Рекомендовано)", "Миттєве підтвердження особи через застосунок Діч без сканування документів", "Підтвердити через Дію", verificationMethod ==VerificationMethods.DIIA) { onVerificationMethod(VerificationMethods.DIIA) }
        Spacer(modifier = Modifier.height(12.dp))
        VerificationCard(VerificationMethods.ID, "Закордонний паспорт або ID-картка", "Фото документа та селфі для перевірки", "Завантажити фото", verificationMethod == VerificationMethods.ID) { onVerificationMethod(VerificationMethods.ID) }
        Spacer(modifier = Modifier.height(12.dp))
        VerificationCard(VerificationMethods.DOC, "Право власності", "Витяг з реєстру речових прав або договір", "Додати документи", verificationMethod == VerificationMethods.DOC  ) { onVerificationMethod(VerificationMethods.DOC) }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth().clip(SwipeHomeTheme.shapes.mediumShape).background(SwipeHomeTheme.colors.surface).padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background( SwipeHomeTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(Res.drawable.ic_hint),
                    contentDescription = null,
                    tint = SwipeHomeTheme.colors.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Банківський рівень безпеки", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onPrimary)
                Spacer(Modifier.height(4.dp))
                Text("Усі дані шифруються за протоколом TLS 1.3 і передаються безпосередньо ліцензованому партнеру з верифікації. Ми не зберігаємо фото ваших документів.", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
            }
        }
    }
}

@Composable
fun VerificationCard(method: VerificationMethods, title: String, subtitle: String, actionText: String, isPrimary: Boolean, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ onClick() }
            .border(1.dp, if (isPrimary) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.neutral, SwipeHomeTheme.shapes.mediumShape),
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
        shape = SwipeHomeTheme.shapes.mediumShape
    ){
        Column(modifier = Modifier.padding(16.dp)){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isPrimary) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface),
                    contentAlignment = Alignment.Center
                ) {
                       when (method) {
                           VerificationMethods.DIIA ->Icon(
                            painterResource(Res.drawable.ic_diia_logo),
                            contentDescription = null,
                            tint = if (isPrimary) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral
                        )
                           VerificationMethods.ID -> Icon(
                            painterResource(Res.drawable.ic_badge),
                            contentDescription = null,
                            tint = if (isPrimary) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral
                        )
                        else -> Icon(
                            painterResource(Res.drawable.ic_document),
                            contentDescription = null,
                            tint = if (isPrimary) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral
                        )
                    }
                }
                if (method == VerificationMethods.DIIA){
                    Box(modifier = Modifier.clip(SwipeHomeTheme.shapes.mediumShape).background(SwipeHomeTheme.colors.primary).padding(horizontal = 8.dp, vertical = 4.dp)){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painterResource(Res.drawable.ic_check_circle_outline), contentDescription = null, tint = SwipeHomeTheme.colors.onPrimary, modifier = Modifier.size(12.dp))
                            Text("1 хвилина", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onPrimary)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.clickable{  }, verticalAlignment = Alignment.CenterVertically){
                Text(actionText, style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(painterResource(Res.drawable.ic_arrow_next), contentDescription = null, tint = SwipeHomeTheme.colors.primary, modifier = Modifier.size(14.dp))
            }
        }
    }
}