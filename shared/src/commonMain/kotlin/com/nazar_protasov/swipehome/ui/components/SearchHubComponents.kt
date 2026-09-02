package com.nazar_protasov.swipehome.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.models.Property
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_heart
import mymultiplatformproject.shared.generated.resources.ic_location_pin
import mymultiplatformproject.shared.generated.resources.photo_description
import mymultiplatformproject.shared.generated.resources.search_hub_btn_new
import mymultiplatformproject.shared.generated.resources.search_hub_btn_to_comparsion
import mymultiplatformproject.shared.generated.resources.search_hub_count_objects
import mymultiplatformproject.shared.generated.resources.search_hub_detailed_comparsion_table_object
import mymultiplatformproject.shared.generated.resources.search_hub_detailed_comparsion_table_row_area
import mymultiplatformproject.shared.generated.resources.search_hub_detailed_comparsion_table_row_localization
import mymultiplatformproject.shared.generated.resources.search_hub_detailed_comparsion_table_row_price
import mymultiplatformproject.shared.generated.resources.search_hub_detailed_comparsion_table_row_type
import mymultiplatformproject.shared.generated.resources.search_hub_detailed_comparsion_table_specificstion
import mymultiplatformproject.shared.generated.resources.search_hub_detailed_comparsion_table_title
import mymultiplatformproject.shared.generated.resources.search_hub_statistic_likes
import mymultiplatformproject.shared.generated.resources.search_hub_statistic_rejected
import mymultiplatformproject.shared.generated.resources.search_hub_statistic_title
import mymultiplatformproject.shared.generated.resources.search_hub_statistic_views
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatisticsSection(){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = SwipeHomeTheme.shapes.mediumShape,
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(Res.string.search_hub_statistic_title),
                style = SwipeHomeTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                color = SwipeHomeTheme.colors.onSurfaceSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem("150", stringResource(Res.string.search_hub_statistic_views), true, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatItem("12", stringResource(Res.string.search_hub_statistic_likes), false, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                StatItem("138", stringResource(Res.string.search_hub_statistic_rejected), false, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String, isHighlighted: Boolean, modifier: Modifier = Modifier){
    val bgColor = if (isHighlighted) SwipeHomeTheme.colors.primary.copy(alpha = 0.15f) else SwipeHomeTheme.colors.background
    val textColor = if (isHighlighted) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.onSurface

    Box(modifier = modifier
        .clip(SwipeHomeTheme.shapes.smallShape)
        .background(bgColor)
        .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = textColor, style = SwipeHomeTheme.typography.subheadline, fontWeight = FontWeight.Bold)
            Text(label, color = if(isHighlighted) textColor else SwipeHomeTheme.colors.onSurfaceSecondary, style = SwipeHomeTheme.typography.caption)
        }
    }
}

@Composable
fun BadgeChip(text: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .clip(SwipeHomeTheme.shapes.mediumShape)
            .background(if (isSelected) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ){
        Text(
            text,
            color = if (isSelected) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.onSurface,
            style = SwipeHomeTheme.typography.caption,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun HubGridCard(
    property: Property,
    isCompareMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) SwipeHomeTheme.colors.primary else Color.Transparent,
                shape = SwipeHomeTheme.shapes.mediumShape
            ),
        shape = SwipeHomeTheme.shapes.mediumShape
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = property.imagesUrl,
                contentDescription = stringResource(Res.string.photo_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            // Градієнт для читабельності тексту
            Box(
                modifier = Modifier.fillMaxSize().background(
                    verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 100f
                    )
                )
            )

            // Чек бокс і бейдж "Нове"
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    if (isCompareMode) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onClick() },
                            colors = CheckboxDefaults.colors(
                                checkedColor = SwipeHomeTheme.colors.primary,
                                uncheckedColor = Color.White
                            )
                        )
                    }
                    if (property.isNew) {
                        Box(
                            modifier = Modifier
                                .clip(SwipeHomeTheme.shapes.smallShape)
                                .background(SwipeHomeTheme.colors.background)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ){
                            Text(
                                stringResource(Res.string.search_hub_btn_new),
                                color = SwipeHomeTheme.colors.primary,
                                style = SwipeHomeTheme.typography.caption,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Сердечко
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(SwipeHomeTheme.colors.background),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painterResource(Res.drawable.ic_heart), contentDescription = "Збережене", tint = SwipeHomeTheme.colors.secondary)
                }
            }

            // Текст ціни та локалізації
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ){
                Text(property.price, style = SwipeHomeTheme.typography.subheadline, fontWeight = FontWeight.Bold, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painterResource(Res.drawable.ic_location_pin), contentDescription = null, modifier = Modifier.size(10.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${ property.buildingType }, ${ property.location }", style = SwipeHomeTheme.typography.caption, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CompareFloatingBar(
    selectedCount: Int,
    selectedProperties: List<Property>,
    onCompareClick: () -> Unit
){
    Card(
        shape = SwipeHomeTheme.shapes.mediumShape,
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            selectedProperties.take(3).forEach { property ->
                AsyncImage(
                    model = property.imagesUrl,
                    contentDescription = stringResource(Res.string.photo_description),
                    modifier = Modifier.size(40.dp).clip(CircleShape).border(2.dp, SwipeHomeTheme.colors.background, CircleShape)
                )
            }
        }

        Button(
            onClick = onCompareClick,
            colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.primary),
            shape = SwipeHomeTheme.shapes.smallShape,
        ){
            Text(
                "${stringResource(Res.string.search_hub_btn_to_comparsion)} ($selectedCount) ${stringResource(Res.string.search_hub_count_objects)}",
                style = SwipeHomeTheme.typography.label
            )
        }
    }
}

@Composable
fun ComparisonTableContent(selectedProperties: List<Property>){
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            stringResource(Res.string.search_hub_detailed_comparsion_table_title),
            style = SwipeHomeTheme.typography.label,
            fontWeight = FontWeight.Bold,
            color = SwipeHomeTheme.colors.onSurfaceSecondary
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Заголовок таблиці
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(
                stringResource(Res.string.search_hub_detailed_comparsion_table_specificstion),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.Bold,
                color = SwipeHomeTheme.colors.neutral,
                modifier = Modifier.weight(1.5f)
            )
            selectedProperties.fastForEachIndexed { index, _ ->
                Text(
                    "${stringResource(Res.string.search_hub_detailed_comparsion_table_object)} ${index + 1}",
                    style = SwipeHomeTheme.typography.label,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = SwipeHomeTheme.colors.primary
                )
            }
        }
        HorizontalDivider(color = SwipeHomeTheme.colors.outline)

        // Рядки таблиці
        ComparisonRow(stringResource(Res.string.search_hub_detailed_comparsion_table_row_type), selectedProperties.map { it.buildingType })
        ComparisonRow(stringResource(Res.string.search_hub_detailed_comparsion_table_row_price), selectedProperties.map { it.price })
        ComparisonRow(stringResource(Res.string.search_hub_detailed_comparsion_table_row_localization), selectedProperties.map { it.location })
        ComparisonRow(stringResource(Res.string.search_hub_detailed_comparsion_table_row_area), selectedProperties.map { it.area.toString() })

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ComparisonRow(label: String, values: List<String>){
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)){
        Text(
            label,
            modifier = Modifier.weight(1.5f),
            color = SwipeHomeTheme.colors.onSurfaceSecondary,
            style = SwipeHomeTheme.typography.label
        )
        values.forEach { value ->
            Text(
                value,
                modifier = Modifier.weight(1f),
                color = SwipeHomeTheme.colors.neutral,
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.Medium
            )
        }
    }
    HorizontalDivider(color = SwipeHomeTheme.colors.outline, thickness = 0.5.dp)
}
