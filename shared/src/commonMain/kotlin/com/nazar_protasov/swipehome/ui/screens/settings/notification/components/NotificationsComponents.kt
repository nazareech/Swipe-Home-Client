package com.nazar_protasov.swipehome.ui.screens.settings.notification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_buildindings
import mymultiplatformproject.shared.generated.resources.ic_calendar_history
import mymultiplatformproject.shared.generated.resources.ic_chat_buble_outline
import mymultiplatformproject.shared.generated.resources.ic_chat_discussion
import mymultiplatformproject.shared.generated.resources.ic_envelipe_mail
import mymultiplatformproject.shared.generated.resources.ic_envelope_check
import mymultiplatformproject.shared.generated.resources.ic_heart_border
import mymultiplatformproject.shared.generated.resources.ic_home_in_hand
import mymultiplatformproject.shared.generated.resources.ic_lightning
import mymultiplatformproject.shared.generated.resources.ic_list
import mymultiplatformproject.shared.generated.resources.ic_megaphone
import mymultiplatformproject.shared.generated.resources.ic_moon_outline
import mymultiplatformproject.shared.generated.resources.ic_people
import mymultiplatformproject.shared.generated.resources.ic_search_star
import mymultiplatformproject.shared.generated.resources.ic_trending_down
import mymultiplatformproject.shared.generated.resources.ic_user_verified
import org.jetbrains.compose.resources.painterResource

@Composable
fun NotificationsTopBar(navigator: Navigator) {
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
            text = "Сповіщення",
            style = SwipeHomeTheme.typography.headline,
            color = SwipeHomeTheme.colors.neutral
        )

        // Міні-статус DND в шапці (завжди видимий)
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(SwipeHomeTheme.colors.outline)
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Icon(painterResource(Res.drawable.ic_moon_outline), contentDescription = "Add", tint = SwipeHomeTheme.colors.neutral)
            Spacer(Modifier.width(4.dp))
            Text(
                "DND: 22-08",
                style = SwipeHomeTheme.typography.caption,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(Modifier.width(6.dp))
            Box(
                modifier = Modifier.size(8.dp).clip(CircleShape)
                    .background(SwipeHomeTheme.colors.neutral)
            )
        }
    }
}

@Composable
fun DndCard(modifier: Modifier = Modifier, onSettingsChanged: () -> Unit){
    var isDndActive by remember { mutableStateOf(false) }
    // Стан для збереження вибраного часу
    var selectedPause by remember { mutableStateOf("") }
    val pauseOptions = listOf("1 год", "8 год", "24 год")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = SwipeHomeTheme.shapes.mediumShape,
        colors = CardDefaults.cardColors(SwipeHomeTheme.colors.surface)
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок і свічі
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if(isDndActive) SwipeHomeTheme.colors.primary.copy(alpha = 0.1f) else SwipeHomeTheme.colors.outline), contentAlignment = Alignment.Center){
                        Icon(painterResource(Res.drawable.ic_moon_outline), contentDescription = null, tint = if(isDndActive) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column{
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Text("Тихі години", style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.neutral)
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if(isDndActive) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.outline).padding(horizontal = 6.dp, vertical = 2.dp)){
                                if (isDndActive) {
                                    Text(
                                        "Ввімкнено",
                                        style = SwipeHomeTheme.typography.caption,
                                        color = SwipeHomeTheme.colors.onPrimary
                                    )
                                } else {
                                    Text(
                                        "Вимкнено",
                                        style = SwipeHomeTheme.typography.caption,
                                        color = SwipeHomeTheme.colors.onSurface
                                    )
                                }
                            }
                        }
                        Text("Без дзвінків та звукових сповіщень", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                    }
                }
                Switch(
                    checked = isDndActive,
                    onCheckedChange = {
                        isDndActive = it
                        onSettingsChanged()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SwipeHomeTheme.colors.onPrimary,
                        checkedTrackColor = SwipeHomeTheme.colors.primary,
                        uncheckedThumbColor = SwipeHomeTheme.colors.outline,
                        uncheckedTrackColor = SwipeHomeTheme.colors.background
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Розклад сну
            Column(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(SwipeHomeTheme.colors.outline).padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Розклад сну",
                        style = SwipeHomeTheme.typography.caption,
                        color = SwipeHomeTheme.colors.neutral
                    )
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            .background(SwipeHomeTheme.colors.outline)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "22:00 - 08:00",
                            style = SwipeHomeTheme.typography.caption,
                            color = SwipeHomeTheme.colors.neutral
                        )
                    }
                    Text(
                        "10 год щоночі",
                        style = SwipeHomeTheme.typography.caption,
                        color = SwipeHomeTheme.colors.neutral
                    )
                }

                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape)
                        .background(SwipeHomeTheme.colors.surface)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.26f).align(Alignment.CenterStart)
                            .height(10.dp).background(SwipeHomeTheme.colors.primary)
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(0.27f).align(Alignment.CenterEnd)
                            .height(10.dp).background(SwipeHomeTheme.colors.primary)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("00:00", "08:00", "14:00", "22:00", "24:00").forEach {
                        Text(
                            it,
                            style = SwipeHomeTheme.typography.caption,
                            color = SwipeHomeTheme.colors.neutral
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            // Швидка пауза
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Text("Швидка пауза", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pauseOptions.forEach { option ->
                        PauseChip(
                            text = option,
                            isSelected = selectedPause == option && isDndActive,
                            onClick = {
                                selectedPause = option

                                if(!isDndActive){
                                    isDndActive = true
                                }
                                onSettingsChanged()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PauseChip(text: String, isSelected: Boolean, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.outline)
            .clickable{ onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ){
        Text(text, style = SwipeHomeTheme.typography.caption, color = if (isSelected) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.onSurface)
    }
}

// Вкладка "Всі"
@Composable
fun AllSettingsTab(onSettingsChanged: () -> Unit){
    Column {
        SeekersSettingsTab(onSettingsChanged)
        OwnersSettingsTab(onSettingsChanged)
        ChannelsSettingsTab(onSettingsChanged)
    }
}

@Composable
fun SeekersSettingsTab(onSettingsChanged: () -> Unit){
    Column {
        SectionHeader("КОМУНІКАЦІЯ ТА ВЗАЄМОДІЯ", "З активності", painterResource(Res.drawable.ic_chat_discussion))
        SettingsCard {
            SettingsToggleItem(painterResource(Res.drawable.ic_chat_buble_outline), "Нові повідомлення в чаті", "Звук, банери та спливаючі повідомлення", "МИТТЄВО",SwipeHomeTheme.colors.primary, onSettingsChanged)
            HorizontalDivider(
                Modifier,
                color = SwipeHomeTheme.colors.outline,
                thickness = 1.dp
            )
            SettingsToggleItem(painterResource(Res.drawable.ic_heart_border), "Взаємні метчі (Super-Match)", "Миттєве сповіщення про взаємний збіг житла", "ПРІОРИТЕТ",SwipeHomeTheme.colors.error, onSettingsChanged)
            HorizontalDivider(
                Modifier,
                color = SwipeHomeTheme.colors.outline,
                thickness = 1.dp
            )
            SettingsToggleItem(painterResource(Res.drawable.ic_people), "Спільний ко-свайпінг та друзі", "Активність та схвалення квартир у спільній групі", null,null, onSettingsChanged)
        }

        Spacer(Modifier.height(16.dp))

        SectionHeader("РОЗУМНІ СПОВІЩЕННЯ ПОШУКУ", "2 налаштування", painterResource(Res.drawable.ic_search_star))
        SettingsCard {
            SettingsAdvancedItem(painterResource(Res.drawable.ic_buildindings), "Нові об'єкти за вашими фільтрами", "Оберіть зручну періодичність сповіщень", onSettingsChanged)
            HorizontalDivider(
                Modifier,
                color = SwipeHomeTheme.colors.outline,
                thickness = 1.dp
            )
            SettingsToggleItem(painterResource(Res.drawable.ic_trending_down), "Зниження ціни в обраному", "Коли орендодавець знижує вартість на збережені квартири","ВІД -5%", SwipeHomeTheme.colors.primary, onSettingsChanged)
        }
    }
}

@Composable
fun OwnersSettingsTab(onSettingsChanged: () -> Unit){
    Column {
        SectionHeader("УПРАВЛІННЯ ОГОЛОШЕННЯМИ", "Власникам", painterResource(Res.drawable.ic_home_in_hand))
        SettingsCard {
            SettingsToggleItem(painterResource(Res.drawable.ic_user_verified), "Статус модерації та верифікація", "Повідомлення про підтвердження документів та житла", "КРИТИЧНО", SwipeHomeTheme.colors.primary ,onSettingsChanged)
            HorizontalDivider(
                Modifier,
                color = SwipeHomeTheme.colors.outline,
                thickness = 1.dp
            )
            SettingsToggleItem(painterResource(Res.drawable.ic_calendar_history), "Термін дії оголошень", "Нагадування за 3 дні до автоматичної деактивації", null, null ,onSettingsChanged)
        }
    }
}

@Composable
fun ChannelsSettingsTab(onSettingsChanged: () -> Unit){
    Column {
        SectionHeader("EMAIL-КАНАЛИ ТА ДАЙДЖЕСТИ", "Дайджести", painterResource(Res.drawable.ic_envelope_check))
        SettingsCard {
            SettingsToggleItem(painterResource(Res.drawable.ic_envelipe_mail), "Щотижневий персональний дайджест", "Добірка топ-квартир тижня за вашими критеріями", null, null ,onSettingsChanged)
            HorizontalDivider(
                Modifier,
                color = SwipeHomeTheme.colors.outline,
                thickness = 1.dp
            )
            SettingsToggleItem(painterResource(Res.drawable.ic_megaphone), "Новини платформи та акційні пропозиції", "Оновлення функцій Swipe Home та корисні поради", null, null ,onSettingsChanged, initialChecked = false)
        }
    }
}

@Composable
fun SectionHeader(title: String, trailingText: String, icon: Painter){
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = SwipeHomeTheme.colors.primary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
        }
        Text(trailingText, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
    }
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = SwipeHomeTheme.shapes.mediumShape,
        colors = CardDefaults.cardColors(SwipeHomeTheme.colors.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ){
        Column(modifier = Modifier.padding(vertical = 8.dp)){
            content()
        }
    }
}

@Composable
fun SettingsToggleItem(
    icon: Painter, title: String, subtitle: String,
    badgeText: String?, badgeColor: Color?,
    onSettingsChanged: () -> Unit,
    initialChecked: Boolean= true
){
    var isChecked by remember { mutableStateOf(initialChecked) }

    val tintColor = badgeColor ?: SwipeHomeTheme.colors.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                isChecked = !isChecked
                onSettingsChanged()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(tintColor.copy(alpha = 0.1f)), contentAlignment = Alignment.Center){
            Icon(icon, contentDescription = null, tint = tintColor)
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                Text(title, style = SwipeHomeTheme.typography.body, color = SwipeHomeTheme.colors.onSurface, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                if(badgeText != null && badgeColor != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(badgeColor.copy(alpha = 0.1f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text(
                            badgeText,
                            style = SwipeHomeTheme.typography.caption,
                            color = badgeColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary, lineHeight = 16.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onSettingsChanged()
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = SwipeHomeTheme.colors.onPrimary,
                checkedTrackColor = SwipeHomeTheme.colors.primary,
                uncheckedThumbColor = SwipeHomeTheme.colors.outline,
                uncheckedTrackColor = SwipeHomeTheme.colors.background
            )
        )
    }
}

@Composable
fun SettingsAdvancedItem(icon: Painter, title: String, subtitle: String, onSettingsChanged: () -> Unit){
    var isFastNotifications by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(SwipeHomeTheme.colors.primary.copy(alpha = 0.1f)), contentAlignment = Alignment.Center){
                Icon(icon, contentDescription = null, tint = SwipeHomeTheme.colors.primary)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurface, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(SwipeHomeTheme.colors.outline).padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)){
            OutlinedButton(
                onClick = {
                    onSettingsChanged()
                    isFastNotifications = true
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isFastNotifications) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier = Modifier.height(36.dp).fillMaxWidth(0.5f)
            ){
                Icon(painterResource(Res.drawable.ic_lightning), contentDescription = null, tint = if (isFastNotifications) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.onSurface, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Миттєво", style = SwipeHomeTheme.typography.caption, color = if (isFastNotifications) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.onSurface, fontWeight = FontWeight.Medium)
            }
            OutlinedButton(
                onClick = {
                    onSettingsChanged()
                    isFastNotifications = false
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (!isFastNotifications) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.surface
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier = Modifier.height(36.dp).fillMaxWidth()
            ){
                Icon(painterResource(Res.drawable.ic_list), tint = if (!isFastNotifications) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.onSurface, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Раз на день", style = SwipeHomeTheme.typography.caption, color = if (!isFastNotifications) SwipeHomeTheme.colors.onPrimary else SwipeHomeTheme.colors.onSurface, fontWeight = FontWeight.Medium)
            }
        }
    }
}
