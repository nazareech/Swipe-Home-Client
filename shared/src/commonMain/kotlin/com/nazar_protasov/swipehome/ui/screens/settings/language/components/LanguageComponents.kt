package com.nazar_protasov.swipehome.ui.screens.settings.language.components

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_check_circle_outline
import org.jetbrains.compose.resources.painterResource

data class LanguageOption(
    val id: String,
    val name: String,
    val subtitle: String,
    val abbreviation: String,
    val isSystemDefault: Boolean = false
)
@Composable
fun LanguageTopBar(navigator: Navigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SwipeHomeTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = { navigator.pop() }
        ) {
            Icon(
                painterResource(Res.drawable.ic_arrow_back),
                contentDescription = "Back"
            )
        }

        Text(
            text = "Мова",
            style = SwipeHomeTheme.typography.headline,
            color = SwipeHomeTheme.colors.neutral
        )

        Box(
            modifier = Modifier
                .size(36.dp)
        )
    }
}

@Composable
fun LanguageBottomBar(navigator: Navigator){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SwipeHomeTheme.colors.background)
            .padding(16.dp),
    ){
        Button(
            onClick = { navigator.pop() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.primary),
            shape = SwipeHomeTheme.shapes.mediumShape
        ){
            Icon(
                painterResource(Res.drawable.ic_check_circle_outline),
                contentDescription = null,
                tint = SwipeHomeTheme.colors.onPrimary
            )
            Spacer(Modifier.width(8.dp))
            Text("Застосувати зміни", style = SwipeHomeTheme.typography.body, color = SwipeHomeTheme.colors.onPrimary)
        }
    }
}


@Composable
fun SectionHeader(title: String, trailingContent: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(title, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
        trailingContent()
    }
}

@Composable
fun LanguageItemRow(
    item: LanguageOption,
    isSelected: Boolean,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        // Абревіатура
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(SwipeHomeTheme.shapes.smallShape)
                .background(SwipeHomeTheme.colors.background),
            contentAlignment = Alignment.Center
        ){
            Text(item.abbreviation, style = SwipeHomeTheme.typography.headline, color = SwipeHomeTheme.colors.onSurface)
        }

        Spacer(Modifier.width(16.dp))

        // Текст
        Column(modifier = Modifier.weight(1f)){
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(item.name, style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.onSurface)
                if(item.isSystemDefault){
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(SwipeHomeTheme.shapes.mediumShape)
                            .background(SwipeHomeTheme.colors.primary)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ){
                        Text("Системний", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onPrimary)
                    }
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(item.subtitle, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
        }
        Spacer(Modifier.width(16.dp))

        // Радіо-кнопка / Чекбокс
        if(isSelected){
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(SwipeHomeTheme.shapes.smallShape)
                    .background(SwipeHomeTheme.colors.primary),
                contentAlignment = Alignment.Center
            ){
                Icon(painterResource(Res.drawable.ic_check_circle_outline), contentDescription = null, tint = SwipeHomeTheme.colors.onPrimary)
            }
        }else{
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(SwipeHomeTheme.shapes.smallShape)
                    .background(SwipeHomeTheme.colors.outline)
            )
        }
    }
}