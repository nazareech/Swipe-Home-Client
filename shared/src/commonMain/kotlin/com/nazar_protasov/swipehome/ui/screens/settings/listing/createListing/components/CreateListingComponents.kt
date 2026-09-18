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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import mymultiplatformproject.shared.generated.resources.ic_badge
import mymultiplatformproject.shared.generated.resources.ic_balcony
import mymultiplatformproject.shared.generated.resources.ic_buildindings
import mymultiplatformproject.shared.generated.resources.ic_check_circle_outline
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


// --- НАВІГАЦІЯ ТА ПРОГРЕС ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WizardTopBar(currentStep: Int, totalSteps: Int, onBackClick: () -> Unit){
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
                if (currentStep > 1){
                    TextButton(onClick = {/*TODO: Save draft*/}) {
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
fun WizardBottomBar(currentStep: Int, totalSteps: Int, onNext: () -> Unit, onPrev: () -> Unit){
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
                onClick = { /*TODO:*/ },
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
    selectedType: String, onTypeSelected: (String) -> Unit,
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
            PropertyTypeCard("Квартира", painterResource(Res.drawable.ic_buildindings), selectedType == "Квартира", Modifier.weight(1f)) { onTypeSelected("Квартира") }
            PropertyTypeCard("Будинок", painterResource(Res.drawable.ic_buildindings), selectedType == "Будинок", Modifier.weight(1f)) { onTypeSelected("Будинок") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        PropertyTypeCard("Студія", painterResource(Res.drawable.ic_sofa), selectedType == "Студія", Modifier.fillMaxWidth()) { onTypeSelected("Студія") }

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
            Text("Підтверджено: ${address}", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
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
        modifier = modifier.height(100.dp).clickable{ onClick() }.border(1.dp, if (isSelected) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.neutral, SwipeHomeTheme.shapes.mediumShape),
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
    Text(label, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
fun Step2Details(){
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
                ContentRow("Спальні", "Кількість окремих спалень", 2)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SwipeHomeTheme.colors.neutral)
                ContentRow("Ванні кімнати", "Суміжні або роздільні санвузли", 1)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SwipeHomeTheme.colors.neutral)
                ContentRow("Поверх", "Поверх / поверховість", "4")
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
            FeatureChip(painterResource(Res.drawable.ic_pets_allowed), "Дозволено з тваринами")
            FeatureChip(painterResource(Res.drawable.ic_parking), "Паркомісце включено")
            FeatureChip(painterResource(Res.drawable.ic_sofa), "З меблями")
            FeatureChip(painterResource(Res.drawable.ic_balcony), "Балкон / Тераса")
            FeatureChip(painterResource(Res.drawable.ic_snowflake), "Кондиціонер")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Зручності в будинку", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
            AmenityCard("Спортзал", painterResource(Res.drawable.ic_dumbbell), true, Modifier.weight(1f))
            AmenityCard("Басейн", painterResource(Res.drawable.ic_pool), false, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
            AmenityCard("Ліфт", painterResource(Res.drawable.ic_elevator), true, Modifier.weight(1f))
            AmenityCard("Охорона", painterResource(Res.drawable.ic_shield_security), false, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
            Text("Опис оголошення", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold)
            Text("0 / 1000 символів", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.surface)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Опишіть особливості квартири (наприклад перша здача після авторського ремонту...", color = SwipeHomeTheme.colors.onSurfaceSecondary) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = SwipeHomeTheme.shapes.mediumShape,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SwipeHomeTheme.colors.primary, unfocusedBorderColor = SwipeHomeTheme.colors.neutral)
        )
    }
}

@Composable
fun ContentRow(title: String, subtitle: String, value: Any){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
        Column(modifier = Modifier.weight(1f)){
            Text(title, style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onSurfaceSecondary)
            Text(subtitle, style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary)
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            Box(modifier = Modifier.size(32.dp).border(1.dp, SwipeHomeTheme.colors.neutral, CircleShape), contentAlignment = Alignment.Center){
                Text("-", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.neutral)
            }
        }

        Text(value.toString(), modifier = Modifier.padding(horizontal = 16.dp), style = SwipeHomeTheme.typography.body, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(SwipeHomeTheme.colors.primary).border(1.dp, SwipeHomeTheme.colors.neutral, CircleShape), contentAlignment = Alignment.Center){
                Text("+", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.onPrimary)
        }
    }
}

@Composable
fun FeatureChip(icon: Painter, text: String){
    Row(
        modifier = Modifier.border(1.dp, SwipeHomeTheme.colors.neutral, SwipeHomeTheme.shapes.largeShape).padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(icon, contentDescription = null, tint = SwipeHomeTheme.colors.neutral)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
    }
}

@Composable
fun AmenityCard(title: String, icon: Painter, isChecked: Boolean, modifier: Modifier = Modifier){
    Card(
        modifier = modifier.height(100.dp).border(1.dp, if(isChecked) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.neutral, SwipeHomeTheme.shapes.mediumShape),
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
fun Step3Media(){
    Column {
        Text("Завантажити фото", style = SwipeHomeTheme.typography.headline, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Якісні фото суттєво підвищують інтерес до нерухомості. Завантажте щонайменше 3 фото для проходження модерації.", style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary, lineHeight = 20.sp)

        Spacer(modifier = Modifier.height(24.dp))

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
                        Text("3 / 5 рекомендовано", style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.primary)
                        Text("(мінімум 3 фото)", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(SwipeHomeTheme.colors.neutral)){
                    Box(modifier = Modifier.fillMaxWidth(0.6f).height(6.dp).background(SwipeHomeTheme.colors.primary))
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

        // Зона Drop
        val stroke = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
        val strokeColor = SwipeHomeTheme.colors.neutral
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .drawBehind { drawRoundRect(color = strokeColor, style = stroke, cornerRadius = CornerRadius(16.dp.toPx())) }
                .clip(SwipeHomeTheme.shapes.mediumShape)
                .background(SwipeHomeTheme.colors.outline)
                .clickable { /*TODO: Open picker*/ },
            contentAlignment = Alignment.Center
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(SwipeHomeTheme.colors.primary), contentAlignment = Alignment.Center){
                    Icon(painterResource(Res.drawable.ic_image_upload), contentDescription = null, tint = SwipeHomeTheme.colors.onPrimary, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Натисни для завантаження фото", style = SwipeHomeTheme.typography.body, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.neutral, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text("JPG, PNG", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
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

// --- ВЕРИФІКАЦІЯ ---

@Composable
fun Step4Verification(verificationMethod: String, onVerificationMethod: (String) -> Unit){
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

        VerificationCard("Дія", "Верифікація через Дію\n(Рекомендовано)", "Миттєве підтвердження особи через застосунок Діч без сканування документів", "Підтвердити через Дію", verificationMethod =="Дія") { onVerificationMethod("Дія") }
        Spacer(modifier = Modifier.height(12.dp))
        VerificationCard("ID", "Закордонний паспорт або ID-картка", "Фото документа та селфі для перевірки", "Завантажити фото", verificationMethod == "ID") { onVerificationMethod("ID") }
        Spacer(modifier = Modifier.height(12.dp))
        VerificationCard("DOC", "Право власності", "Витяг з реєстру речових прав або договір", "Додати документи", verificationMethod == "DOC"  ) { onVerificationMethod("DOC") }

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
fun VerificationCard(iconText: String, title: String, subtitle: String, actionText: String, isPrimary: Boolean, onClick: () -> Unit){
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
                       when (iconText) {
                        "Дія" ->Icon(
                            painterResource(Res.drawable.ic_diia_logo),
                            contentDescription = null,
                            tint = if (isPrimary) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.neutral
                        )
                        "ID" -> Icon(
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
                if (iconText == "Дія"){
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