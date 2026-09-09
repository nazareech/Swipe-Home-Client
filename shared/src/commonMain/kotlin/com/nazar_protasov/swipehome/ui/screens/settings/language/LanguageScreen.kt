package com.nazar_protasov.swipehome.ui.screens.settings.language

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.settings.language.components.LanguageBottomBar
import com.nazar_protasov.swipehome.ui.screens.settings.language.components.LanguageItemRow
import com.nazar_protasov.swipehome.ui.screens.settings.language.components.LanguageOption
import com.nazar_protasov.swipehome.ui.screens.settings.language.components.LanguageTopBar
import com.nazar_protasov.swipehome.ui.screens.settings.language.components.SectionHeader
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_search_star
import mymultiplatformproject.shared.generated.resources.ic_translate
import org.jetbrains.compose.resources.painterResource

class LanguageScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        var searchQuery by remember { mutableStateOf("") }
        var selectedLanguageId by remember { mutableStateOf("ua") }

        val languages = listOf(
            LanguageOption("ua", "Українська", "Українська мова", "UA"),
            LanguageOption("en", "English (US)", "Англійська • Сполучені Штати", "EN"),
            LanguageOption("pl", "Polski", "Польська • Polska", "PL"),
            LanguageOption("de", "Deutsch", "Німецька • Deutschland", "DE"),
            LanguageOption("fr", "Français", "Французька • France", "FR"),
            LanguageOption("es", "Español", "Іспанська • España", "ES")
        )

        Scaffold(
            topBar = { LanguageTopBar(navigator) },
            bottomBar = { LanguageBottomBar(navigator) },
            containerColor = SwipeHomeTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ){
                // Рядок пошуку
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Пошук мови або країни...", color = SwipeHomeTheme.colors.onSurfaceSecondary) },
                    leadingIcon = {Icon(painterResource(Res.drawable.ic_search_star), contentDescription = null, tint = SwipeHomeTheme.colors.onSurfaceSecondary)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = SwipeHomeTheme.shapes.mediumShape,
                    singleLine = true,
                )

                Spacer(modifier = Modifier.padding(16.dp))

                // Поточна мова
                SectionHeader("ПОТОЧНА МОВА", trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SwipeHomeTheme.colors.primary))
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text("Активна зараз", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.primary)
                    }
                })

                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = SwipeHomeTheme.shapes.mediumShape,
                    colors = CardDefaults.cardColors( SwipeHomeTheme.colors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ){
                    val currentLang = languages.find { it.id == selectedLanguageId } ?: languages.first()
                    LanguageItemRow(
                        item = currentLang,
                        isSelected = true,
                        onClick = { }
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Доступні мови
                SectionHeader("ДОСТУПНІ МОВИ", trailingContent = {
                    Text("${languages.size} варіантів", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary)
                })

                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = SwipeHomeTheme.shapes.mediumShape,
                    colors = CardDefaults.cardColors( SwipeHomeTheme.colors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ){
                    Column {
                        languages.forEachIndexed { index, language ->

                            if(language.id != selectedLanguageId) {
                                LanguageItemRow(
                                    item = language,
                                    isSelected = language.id == selectedLanguageId,
                                    onClick = { selectedLanguageId = language.id }
                                )
                            }
                            if (index < languages.size - 1) {
                                HorizontalDivider(color = SwipeHomeTheme.colors.outline, modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Інформаційний банер
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(SwipeHomeTheme.shapes.mediumShape)
                        .background(SwipeHomeTheme.colors.surface)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(SwipeHomeTheme.shapes.smallShape)
                            .background(SwipeHomeTheme.colors.background),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(painterResource(Res.drawable.ic_translate), contentDescription = null, tint = SwipeHomeTheme.colors.primary)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)){
                        Text(
                            "Автоматичний переклад оголошень",
                            style = SwipeHomeTheme.typography.label,
                            fontWeight = FontWeight.Bold,
                            color = SwipeHomeTheme.colors.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Зміна мови застосується до всіх екранів, сповіщень та системних повідомлень. Описи нерухомості від власників будуть миттєво адаптовані.",
                            style = SwipeHomeTheme.typography.caption,
                            color = SwipeHomeTheme.colors.onSurface,
                            lineHeight = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}