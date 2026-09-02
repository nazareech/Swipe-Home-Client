package com.nazar_protasov.swipehome.ui.screens.auth

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import com.nazar_protasov.swipehome.ui.utils.PhoneVisualTransformation
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.app_name
import mymultiplatformproject.shared.generated.resources.btn_back
import mymultiplatformproject.shared.generated.resources.btn_next
import mymultiplatformproject.shared.generated.resources.btn_register
import mymultiplatformproject.shared.generated.resources.email_login_label
import mymultiplatformproject.shared.generated.resources.fogot_paswd_strength_prefix
import mymultiplatformproject.shared.generated.resources.ic_apple_logo
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import mymultiplatformproject.shared.generated.resources.ic_arrow_triangle_down
import mymultiplatformproject.shared.generated.resources.ic_camera
import mymultiplatformproject.shared.generated.resources.ic_eye_hidden
import mymultiplatformproject.shared.generated.resources.ic_eye_visible
import mymultiplatformproject.shared.generated.resources.ic_google_logo
import mymultiplatformproject.shared.generated.resources.ic_hint
import mymultiplatformproject.shared.generated.resources.or_divider
import mymultiplatformproject.shared.generated.resources.password_label
import mymultiplatformproject.shared.generated.resources.password_strength_medium
import mymultiplatformproject.shared.generated.resources.password_strength_strong
import mymultiplatformproject.shared.generated.resources.password_strength_very_strong
import mymultiplatformproject.shared.generated.resources.password_strength_weak
import mymultiplatformproject.shared.generated.resources.reg_agree1
import mymultiplatformproject.shared.generated.resources.reg_agree2
import mymultiplatformproject.shared.generated.resources.reg_continue_apple
import mymultiplatformproject.shared.generated.resources.reg_continue_google
import mymultiplatformproject.shared.generated.resources.reg_email_label
import mymultiplatformproject.shared.generated.resources.reg_email_placeholder
import mymultiplatformproject.shared.generated.resources.reg_hiden_password
import mymultiplatformproject.shared.generated.resources.reg_last_name
import mymultiplatformproject.shared.generated.resources.reg_name
import mymultiplatformproject.shared.generated.resources.reg_password_placeholder
import mymultiplatformproject.shared.generated.resources.reg_phone_number
import mymultiplatformproject.shared.generated.resources.reg_photo_upload
import mymultiplatformproject.shared.generated.resources.reg_profile_photo
import mymultiplatformproject.shared.generated.resources.reg_rules_agree
import mymultiplatformproject.shared.generated.resources.reg_step_indicaton
import mymultiplatformproject.shared.generated.resources.reg_step_indicaton_of
import mymultiplatformproject.shared.generated.resources.reg_subtitle_step1
import mymultiplatformproject.shared.generated.resources.reg_subtitle_step2
import mymultiplatformproject.shared.generated.resources.reg_subtitle_step4
import mymultiplatformproject.shared.generated.resources.reg_title_step1
import mymultiplatformproject.shared.generated.resources.reg_title_step2
import mymultiplatformproject.shared.generated.resources.reg_title_step3
import mymultiplatformproject.shared.generated.resources.reg_title_step4
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class RegisterScreen: Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()

        // Головний стан майстра реєстрації
        var currentStep by rememberSaveable { mutableStateOf(1) }

        // Стани для збереження даних користувача (будуть заповнюватися на різних кроках)
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var firstName by rememberSaveable { mutableStateOf("") }
        var lastName by rememberSaveable { mutableStateOf("") }
        var phone by rememberSaveable { mutableStateOf("") }
        var countryCode by rememberSaveable { mutableStateOf("+380") } // Код за замовчуванням
        var photoByteArray by remember { mutableStateOf<ByteArray?>(null) }  // Готовий тип файл для POST-запиту

        // Створюємо лаунчер для галереї
        val singleImagePicker = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Single,
            scope = coroutineScope,
            onResult = { byteArrays ->
                // Отримуємо перше вибране фото
                photoByteArray = byteArrays.firstOrNull()
            }
        )

        // Змінна для прокручуваного екрана
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(SwipeHomeTheme.colors.background)
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 24.dp)
        ){
            //--------Верхня частина екран--------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        // Якщо ми далі першого кроку - повертаємось на крок назад, інакше виходимо з екрана
                        if (currentStep > 1) currentStep-- else navigator.pop()
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = stringResource(Res.string.btn_back),
                        tint = SwipeHomeTheme.colors.neutral
                    )
                }

                Text(
                    text = stringResource(Res.string.app_name),
                    style = SwipeHomeTheme.typography.subheadline,
                    color = SwipeHomeTheme.colors.primary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Порожній блок для балансу заголовка по центру
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            //--------Індикатор прогресу--------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4){index ->
                    val stepNumber = index + 1
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(4.dp)
                            .width(32.dp)
                            .clip(SwipeHomeTheme.shapes.smallShape)
                            .background(
                                if (stepNumber <= currentStep) SwipeHomeTheme.colors.primary
                                else SwipeHomeTheme.colors.outline
                            )
                    )
                }
            }
            Text(
                text = stringResource(Res.string.reg_step_indicaton) + " $currentStep " + stringResource(Res.string.reg_step_indicaton_of) +" 4",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                style = SwipeHomeTheme.typography.caption
            )

            Spacer(modifier = Modifier.height(32.dp))

            when (currentStep){
                1 -> Step1Content(email = email, onEmailChange = { email = it })
                2 -> Step2Content(email = email, onEmailChange = { email = it }, password = password, onPasswordChange = { password = it })
                3 -> Step3Content(
                    firstName = firstName,
                    onFirstNameChange = { firstName = it },
                    lastName = lastName,
                    onLastNameChange = { lastName = it },
                    photoData = photoByteArray,
                    onPhotoClick = {
                        // икликає галарею при натисканні
                        singleImagePicker.launch()
                    }
                )
                4 -> Step4Content(
                    phone = phone,
                    onPhoneChange = { phone = it },
                    countryCode = countryCode,
                    onCountryCodeChange = { countryCode = it }
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Виштовхує кнопки вниз

            // --------Нижні кнопки--------
            if (currentStep == 1) {
                Button(
                    onClick = { currentStep = 2 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = SwipeHomeTheme.shapes.smallShape,
                    colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.primary)
                ) {
                    Text(stringResource(Res.string.btn_next), style = SwipeHomeTheme.typography.body, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painterResource(Res.drawable.ic_arrow_next),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = SwipeHomeTheme.colors.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Посилання на політику використання
                Column (
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = stringResource(Res.string.reg_agree1),
                        color = SwipeHomeTheme.colors.onSurfaceSecondary,
                        style = SwipeHomeTheme.typography.caption,
                        textAlign = TextAlign.Center
                    )
                    TextButton(onClick = { /*TODO: Посилання на політику використання*/ })
                    {
                        Text(
                            text = stringResource(Res.string.reg_agree2),
                            fontSize = 12.sp,
                            color = SwipeHomeTheme.colors.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            } else {
                // Кнопки для кроків 2, 3, 4 (Далі + Назад)
                Button(
                    onClick = {
                        if (currentStep < 4) currentStep++ else {/*TODO: Фінальна реєстрація*/}
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = SwipeHomeTheme.shapes.smallShape,
                    colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.primary)
                ){
                    Text(
                        if (currentStep == 4) stringResource(Res.string.btn_register) else stringResource(Res.string.btn_next),
                        style = SwipeHomeTheme.typography.body,
                        fontWeight = FontWeight.Bold,
                        color = SwipeHomeTheme.colors.onPrimary
                    )
                    if (currentStep != 4){
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(painterResource(Res.drawable.ic_arrow_next), contentDescription = null, modifier = Modifier.size(20.dp), tint = SwipeHomeTheme.colors.onPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { currentStep-- },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = SwipeHomeTheme.shapes.smallShape
                ){
                    Text(stringResource(Res.string.btn_back), style = SwipeHomeTheme.typography.body, color = SwipeHomeTheme.colors.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    @Composable
    fun Step1Content(email: String, onEmailChange: (String) -> Unit) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.reg_title_step1),
                style = SwipeHomeTheme.typography.headline,
                color = SwipeHomeTheme.colors.neutral
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.reg_subtitle_step1),
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                style = SwipeHomeTheme.typography.body
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопки соц. мереж
            OutlinedButton(
                onClick = {/*TODO*/},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = SwipeHomeTheme.shapes.smallShape
            ){
                Icon(painterResource(Res.drawable.ic_google_logo), contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Unspecified)
                Spacer(modifier = Modifier.width(16.dp))
                Text(stringResource(Res.string.reg_continue_google), style = SwipeHomeTheme.typography.body, color = SwipeHomeTheme.colors.neutral)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {/*TODO*/},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = SwipeHomeTheme.shapes.smallShape,
                colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.neutral)
            ){
                Icon(painterResource(Res.drawable.ic_apple_logo), contentDescription = null, modifier = Modifier.size(32.dp), tint = SwipeHomeTheme.colors.background)
                Spacer(modifier = Modifier.width(16.dp))
                Text(stringResource(Res.string.reg_continue_apple), style = SwipeHomeTheme.typography.body, color = SwipeHomeTheme.colors.background)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Розділювач "АБО"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier.weight(1f).height(1.dp).background(SwipeHomeTheme.colors.outline))
                Text(
                    text = stringResource(Res.string.or_divider),
                    color = SwipeHomeTheme.colors.onSurfaceSecondary,
                    style = SwipeHomeTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier.weight(1f).height(1.dp).background(SwipeHomeTheme.colors.outline))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(Res.string.reg_email_label),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.SemiBold,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(Res.string.reg_email_label)) },
                placeholder = { Text(stringResource(Res.string.reg_email_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape
            )
        }
    }

    @Composable
    fun Step2Content(
        email: String,
        onEmailChange: (String) -> Unit,
        password: String,
        onPasswordChange: (String) -> Unit
    ){
        // Локальний стан для видимості пароля тільки для цього кроку
        var passwordVisible by remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.reg_title_step2),
                style = SwipeHomeTheme.typography.headline,
                color = SwipeHomeTheme.colors.neutral
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.reg_subtitle_step2),
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                style = SwipeHomeTheme.typography.body
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле Email (користувач може його змінити якщо помилився на першому кроці)
            Text(
                text = stringResource(Res.string.email_login_label),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.SemiBold,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(Res.string.email_login_label)) },
                placeholder = { Text(stringResource(Res.string.reg_email_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Поле Пароль
            Text(
                text = stringResource(Res.string.password_label),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.SemiBold,
                color = SwipeHomeTheme.colors.neutral
            )

            //region Нова логіка введення пароля
            // Логіка визначення надійності пароля (від 0 до 4)
            val strength = remember(password) {
                var score = 0
                if (password.isNotEmpty()) {
                    if (password.length >= 8) score++
                    if (password.any { it.isLetter() }) score++
                    if (password.any { it.isDigit() }) score++
                    if (password.any { !it.isLetterOrDigit() }) score++
                }
                score
            }

            // Визначаємо текст та колір залежно від рівня надійності
            val strengthText = when (strength) {
                1 -> stringResource(Res.string.password_strength_weak)
                2 -> stringResource(Res.string.password_strength_medium)
                3 -> stringResource(Res.string.password_strength_strong)
                4 -> stringResource(Res.string.password_strength_very_strong)
                else -> ""
            }
            val strengthColor = when (strength) {
                1 -> SwipeHomeTheme.colors.error    // Червоний
                2 -> Color(0xFFFB8C00)              // Помаранчевий
                3 -> Color(0xFF7CB342)              // Світло-зелений
                4 -> SwipeHomeTheme.colors.primary  // Темно-зелений
                else -> Color.Transparent
            }

            Column(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = { Text(stringResource(Res.string.reg_password_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = SwipeHomeTheme.shapes.smallShape,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painterResource(
                                    if (passwordVisible) Res.drawable.ic_eye_visible else Res.drawable.ic_eye_hidden
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = SwipeHomeTheme.colors.onSurfaceSecondary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Індикатор надійності
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(SwipeHomeTheme.shapes.smallShape)
                                .background(
                                    if (password.isNotEmpty() && index < strength) strengthColor
                                    else SwipeHomeTheme.colors.outline
                                )
                        )
                    }
                }

                if (password.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.fogot_paswd_strength_prefix) + strengthText,
                        color = SwipeHomeTheme.colors.onSurfaceSecondary,
                        style = SwipeHomeTheme.typography.caption
                    )
                }
            }

                //endregion
            Spacer(modifier = Modifier.height(8.dp))

            // Підказка про складність пароля
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    painterResource(Res.drawable.ic_hint),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = SwipeHomeTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(Res.string.reg_hiden_password),
                    color = SwipeHomeTheme.colors.onSurfaceSecondary,
                    style = SwipeHomeTheme.typography.caption
                )
            }
        }
    }

    // Про себе
    @Composable
    fun Step3Content(
        firstName: String,
        onFirstNameChange: (String) -> Unit,
        lastName: String,
        onLastNameChange: (String) -> Unit,
        photoData: ByteArray?,
        onPhotoClick: () -> Unit
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            // Заголовок та опис
            Text(
                text = stringResource(Res.string.reg_title_step3),
                style = SwipeHomeTheme.typography.headline,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(modifier = Modifier.height(16.dp))
/*
            Text(
                text = stringResource(Res.string.reg_subtitle_step3),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
*/
            // Кнопка для додавання фотографії
            Column (
                modifier =  Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        // Змінюємо фон, якщо фотографія завантажена
                        .background(
                            if (photoData != null) SwipeHomeTheme.colors.primary.copy(alpha = 0.2f)
                            else SwipeHomeTheme.colors.surface
                        )
                        .clickable{ onPhotoClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (photoData == null) {
                        // Стан "До вибору фотографії"
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painterResource(Res.drawable.ic_camera),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = SwipeHomeTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(Res.string.reg_photo_upload),
                                style = SwipeHomeTheme.typography.caption,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = SwipeHomeTheme.colors.primary
                            )
                        }
                    } else {
                        // Стан "Зображення завантажено"
                       AsyncImage(
                           model = photoData, // Coli автоматично читає ByteArray
                           contentDescription = stringResource(Res.string.reg_profile_photo),
                           modifier = Modifier
                               .fillMaxSize() // Займаємо всі 120.dp нашого Box
                               .clip(CircleShape), // Обрізаємо кути, щоб фото не вилазило за коло
                           contentScale = ContentScale.Crop // Маштабуємо фото, щоб воно заповнило весь Box без спотворень
                       )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.reg_name),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.SemiBold,
                color = SwipeHomeTheme.colors.neutral
            )
            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = { Text(stringResource(Res.string.reg_name)) },
                placeholder = { Text(stringResource(Res.string.reg_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.reg_last_name),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.SemiBold,
                color = SwipeHomeTheme.colors.neutral
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = { Text(stringResource(Res.string.reg_last_name)) },
                placeholder = { Text(stringResource(Res.string.reg_last_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape
            )
        }
    }

    @Composable
    fun Step4Content(
        phone: String,
        onPhoneChange: (String) -> Unit,
        countryCode: String,
        onCountryCodeChange: (String) -> Unit
    ){
        // Локальний стан для галочки "я погоджуюсь"
        var termsAccepted by remember { mutableStateOf(false) }
        // Стан для відображення меню вибору країни
        var isDropdownExpanded by remember { mutableStateOf(false) }
        // Список доступних кодів
        val availableCountryCodes = listOf("+380", "+48", "+1", "+44", "+49")

        // Словник масок для кожної країни
        val phoneMasks = mapOf(
            "+380" to "00 000 0000",// Україна
            "+48" to "000 000 000", // Польща
            "+1" to "000 000 0000", // США/Канада
            "+44" to "0000 000000", // Британія
            "+49" to "000 0000000"  // Німеччина
        )

        // Визначаємо поточну маску на основі обраного коду (з дефолтним значенням)
        val currentMask = phoneMasks[countryCode] ?: "00 000 0000"

        // Рахуємо, скільки цифр дозволено вводити для цієї країни
        val maxDigits = currentMask.count { it == '0' }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.reg_title_step4),
                style = SwipeHomeTheme.typography.headline,
                color = SwipeHomeTheme.colors.neutral
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.reg_subtitle_step4),
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                style = SwipeHomeTheme.typography.body
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле для номера телефону
            Text(
                text = stringResource(Res.string.reg_phone_number),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.SemiBold,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { input ->
                    // Фільтруємо тільки цифри та обмежуємо максимум 9 символами
                    val digitsOnly = input.filter { it.isDigit() }
                    if (digitsOnly.length <= maxDigits) onPhoneChange(digitsOnly)
                },
                // Динамічний плейсхолдер
                placeholder = { Text(currentMask) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                // Підключаємо кастомне трансформування для форматування пробілів для номера
                visualTransformation = PhoneVisualTransformation(currentMask),
                // Додаємо красивий дизайн префікса країни з випадаючим списком
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { isDropdownExpanded = true }
                            .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    ){
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

                        // Саме меню вибору
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            modifier = Modifier.background(SwipeHomeTheme.colors.surface)
                        ){
                            availableCountryCodes.forEach { code ->
                                DropdownMenuItem(
                                    text = { Text(text = code, color = SwipeHomeTheme.colors.onSurface) },
                                    onClick = {
                                        // При зміні країни очищаємо введений номер
                                        if(code != countryCode) onPhoneChange("")
                                        onCountryCodeChange(code)
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Блок згоди з правилами
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SwipeHomeTheme.shapes.smallShape)
                    .background(SwipeHomeTheme.colors.surface)
                    .padding(16.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = SwipeHomeTheme.colors.primary,
                            uncheckedColor = SwipeHomeTheme.colors.onSurfaceSecondary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.reg_rules_agree),
                        style = SwipeHomeTheme.typography.label,
                        color = SwipeHomeTheme.colors.neutral,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

/*
Перейдемо до бекенду:
Почнемо писати логіку Ktor-клієнта. Створимо функцію, яка збере всі дані з крокового майстра (Email, Пароль, Ім'я, Прізвище, Телефон та Фото) і відправить їх справжнім POST-запитом на сервер.
*/