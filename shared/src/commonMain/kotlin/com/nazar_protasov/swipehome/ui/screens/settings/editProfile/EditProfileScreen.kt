package com.nazar_protasov.swipehome.ui.screens.settings.editProfile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components.CustomTextFiles
import com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components.EditProfileTopBar
import com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components.EditableProfileAvatar
import com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components.IsConfirmData
import com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components.LinkedAccountItem
import com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components.SectionCard
import com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components.SectionTitle
import com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components.TipBanner
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import com.nazar_protasov.swipehome.ui.utils.CountryPhoneCode
import com.nazar_protasov.swipehome.ui.utils.PhoneVisualTransformation
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_apple_logo
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import mymultiplatformproject.shared.generated.resources.ic_arrow_triangle_down
import mymultiplatformproject.shared.generated.resources.ic_contact_card
import mymultiplatformproject.shared.generated.resources.ic_google_logo
import mymultiplatformproject.shared.generated.resources.ic_hint
import mymultiplatformproject.shared.generated.resources.ic_id_card
import mymultiplatformproject.shared.generated.resources.ic_lightbulb_outline
import mymultiplatformproject.shared.generated.resources.ic_security_key
import mymultiplatformproject.shared.generated.resources.ic_security_restore
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

class EditProfileScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()

        val screenModel: EditProfileScreenModel = koinInject()

        val snackbarHostState = remember { SnackbarHostState() }

        // Підписуємося на EditProfileScreenModel
        LaunchedEffect(Unit) {
            screenModel.snackbarEvent.collect { message ->
                snackbarHostState.showSnackbar(message)
            }
        }

        // Спостерігаємо за станами (дані для сервера)
        val isLoading by screenModel.isLoading.collectAsState()
        val userProfile by screenModel.userProfile.collectAsState()

        var login by remember { mutableStateOf("") }
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var aboutMe by remember { mutableStateOf("") }

        /*TODO: треба буде витягнути сі дані користувача*/
        var phone by rememberSaveable { mutableStateOf("") }
        var countryCode by rememberSaveable { mutableStateOf("+48") } // Код за замовчуванням
        var email by rememberSaveable { mutableStateOf("") }
        var photoByteArray by remember { mutableStateOf<ByteArray?>(null) }  // Готовий тип файл для POST-запиту

        // Заповнення даних при завантаженні
        LaunchedEffect(userProfile) {
            userProfile?.let { profile ->
                login = profile.login
                firstName = profile.firstName
                lastName = profile.lastName
                aboutMe = profile.aboutInfo
                phone = profile.phone
                email = profile.email
            }
        }

        // Створюємо лаунчер для галереї
        val singleImagePicker = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Single,
            scope = coroutineScope,
            onResult = { byteArrays ->
                // Отримуємо перше вибране фото
                val selectedPhoto = byteArrays.firstOrNull()
                if (selectedPhoto != null){
                    photoByteArray = selectedPhoto
                    screenModel.updateAvatar(photoByteArray)
                }
            }
        )
        var fullAvatarUrl = userProfile?.avatarUrl?.let {
            if (it.startsWith("http")) it else "${PropertyApiService.BASE_URL}/$it"
        }

        if (fullAvatarUrl == null) fullAvatarUrl = "${PropertyApiService.BASE_URL}/uploads/photo_not_found.jpg"

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                EditProfileTopBar(
                    onBackClicked = { navigator.pop() },
                    onSaveClick = {
                        screenModel.saveProfileInfo(
                            login = login,
                            firstName = firstName,
                            lastName = lastName,
                            aboutInfo = aboutMe,
                            fullPhone = "$countryCode $phone", // Збираємо повний номер телефону
                            email = email,
                            onSuccess = { navigator.pop() }
                        )
                    }
                )
            },
            containerColor = SwipeHomeTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                // Показуємо індикатор завантаження
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SwipeHomeTheme.colors.background.copy(alpha = 0.4f))
                            .pointerInput(Unit) {},
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = SwipeHomeTheme.colors.primary
                        )
                    }
                }

                // Фото профілю
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EditableProfileAvatar(
                        avatarUrl = fullAvatarUrl,
                        onEditClick = {
                            singleImagePicker.launch()
                        }
                    )
                }

                // Особиста інформація
                SectionCard {
                    SectionTitle(
                        painterResource(Res.drawable.ic_id_card),
                        title = "Особиста інформація",
                        tint = SwipeHomeTheme.colors.primary
                    )

                    CustomTextFiles(
                        label = "Ім'я",
                        value = firstName,
                        onValueChange = { firstName = it },
                        bgColor = SwipeHomeTheme.colors.outline
                    )
                    CustomTextFiles(
                        label = "Прізвище",
                        value = lastName,
                        onValueChange = { lastName = it },
                        bgColor = SwipeHomeTheme.colors.outline
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Про себе",
                                style = SwipeHomeTheme.typography.caption,
                                color = SwipeHomeTheme.colors.onSurfaceSecondary
                            )
                            Text(
                                text = "${aboutMe.length} / 200",
                                style = SwipeHomeTheme.typography.caption,
                                color = SwipeHomeTheme.colors.onSurfaceSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = aboutMe,
                            onValueChange = { if (it.length <= 200) aboutMe = it },
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            shape = SwipeHomeTheme.shapes.mediumShape
                        )

                    }
                    TipBanner(
                        icon = painterResource(Res.drawable.ic_lightbulb_outline),
                        text = "Порада: детальний опис про себе підвищує шанс успішного метчу на 65%",
                        bgColor = SwipeHomeTheme.colors.outline,
                        textColor = SwipeHomeTheme.colors.primary
                    )
                }

                // Контакти
                SectionCard {
                    SectionTitle(
                        painterResource(Res.drawable.ic_contact_card),
                        title = "Контакти",
                        tint = SwipeHomeTheme.colors.primary
                    )

                    // Телефон (ready-only стиль)
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "Номер телефону",
                            style = SwipeHomeTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                            color = SwipeHomeTheme.colors.onSurfaceSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Стан для відображення меню вибору країни
                        var isDropdownExpanded by remember { mutableStateOf(false) }

                        // Список доступних кодів з ENUM
                        val availableCountryCodes = CountryPhoneCode.getAllCodes()
                        // Визначаємо поточну маску на основі обраного коду (з дефолтним значенням)
                        val currentMask = CountryPhoneCode.getMaskByCode(countryCode)
                        // Рахуємо, скільки цифр дозволено вводити для цієї країни
                        val maxDigits = currentMask.count { it == '0' }

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { input ->
                                // Фільтруємо тільки цифри та обмежуємо максимум 9 символами
                                val digitsOnly = input.filter { it.isDigit() }
                                if (digitsOnly.length <= maxDigits) phone =
                                    digitsOnly // оновлюємо стан phone з екраном
                            },
                            // Динамічний плейсхолдер
                            placeholder = { Text(currentMask) },
                            label = { Text("Номер телефону") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = SwipeHomeTheme.shapes.mediumShape,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            // Підключаємо кастомне трансформування для форматування пробілів для номера
                            visualTransformation = PhoneVisualTransformation(currentMask),
                            // Додаємо красивий дизайн префікса країни з випадаючим списком
                            leadingIcon = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable { isDropdownExpanded = true }
                                        .padding(
                                            start = 16.dp,
                                            end = 8.dp,
                                            top = 8.dp,
                                            bottom = 8.dp
                                        )
                                ) {
                                    Text(
                                        text = countryCode,
                                        style = SwipeHomeTheme.typography.label,
                                        fontWeight = FontWeight.Bold,
                                        color = SwipeHomeTheme.colors.neutral
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        painterResource(Res.drawable.ic_arrow_triangle_down),
                                        contentDescription = null,
                                        modifier = Modifier.size(10.dp),
                                        tint = SwipeHomeTheme.colors.onSurfaceSecondary
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))

                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(24.dp)
                                            .background(SwipeHomeTheme.colors.outline)
                                    )

                                    // Меню вибору країни
                                    DropdownMenu(
                                        expanded = isDropdownExpanded,
                                        onDismissRequest = { isDropdownExpanded = false },
                                        modifier = Modifier.background(SwipeHomeTheme.colors.surface)
                                    ) {
                                        availableCountryCodes.forEach { code ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = code,
                                                        color = SwipeHomeTheme.colors.onSurface
                                                    )
                                                },
                                                onClick = {
                                                    // При зміні країни очищаємо введений номер
                                                    if (code != countryCode) phone = ""
                                                    countryCode = code
                                                    isDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        Spacer(Modifier.height(6.dp))
                        // Кнопка для підтвердження даних
                        IsConfirmData()
                    }

                    // Email (ready-only стиль)
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "Email адреса",
                            style = SwipeHomeTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                            color = SwipeHomeTheme.colors.onSurfaceSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Поле Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email адреса") },
                            placeholder = { Text("ваша Email адреса") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = SwipeHomeTheme.shapes.smallShape
                        )

                        Spacer(Modifier.height(6.dp))
                        // Кнопка для підтвердження даних
                        IsConfirmData()
                    }

                    TipBanner(
                        icon = painterResource(Res.drawable.ic_hint),
                        text = "Зміна контакту потребує додаткового підтвердження",
                        bgColor = SwipeHomeTheme.colors.secondary,
                        textColor = SwipeHomeTheme.colors.onSurface
                    )
                }

                // Безпека авторизації
                SectionCard {
                    SectionTitle(
                        painterResource(Res.drawable.ic_security_restore),
                        title = "Безпека авторизації",
                        tint = SwipeHomeTheme.colors.primary
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {

                        // Змінити пароль
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(SwipeHomeTheme.shapes.mediumShape)
                                .background(SwipeHomeTheme.colors.outline)
                                .clickable {/*TODO:*/ }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(45.dp).clip(CircleShape)
                                    .background(SwipeHomeTheme.colors.surface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painterResource(Res.drawable.ic_security_key),
                                    contentDescription = null,
                                    tint = SwipeHomeTheme.colors.primary
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Змінити пароль",
                                    style = SwipeHomeTheme.typography.label,
                                    fontWeight = FontWeight.Bold,
                                    color = SwipeHomeTheme.colors.onSurfaceSecondary
                                )
                                Text(
                                    "Останнє оновлений 3 місяців тому",
                                    style = SwipeHomeTheme.typography.caption,
                                    color = SwipeHomeTheme.colors.onSurfaceSecondary
                                )
                            }
                            Icon(
                                painterResource(Res.drawable.ic_arrow_next),
                                contentDescription = null,
                                tint = SwipeHomeTheme.colors.onSurfaceSecondary
                            )
                        }

                        Text(
                            "Прив'язані аккаунти",
                            style = SwipeHomeTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                            color = SwipeHomeTheme.colors.onSurfaceSecondary
                        )

                        LinkedAccountItem(
                            icon = painterResource(Res.drawable.ic_google_logo),
                            "Google",
                            "nzrprotasov@gmail.com",
                            true,
                            SwipeHomeTheme.colors.outline
                        )
                        LinkedAccountItem(
                            icon = painterResource(Res.drawable.ic_apple_logo),
                            "Apple ID",
                            "не прив'язано",
                            false,
                            SwipeHomeTheme.colors.outline
                        )
                    }
                }
            }
        }
    }
}