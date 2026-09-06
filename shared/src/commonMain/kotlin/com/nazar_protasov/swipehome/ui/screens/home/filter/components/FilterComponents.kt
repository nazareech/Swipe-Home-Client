package com.nazar_protasov.swipehome.ui.screens.home.filter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res.string
import mymultiplatformproject.shared.generated.resources.filter_price_from
import mymultiplatformproject.shared.generated.resources.filter_price_to
import org.jetbrains.compose.resources.stringResource

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
