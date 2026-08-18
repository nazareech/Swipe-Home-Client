package com.nazar_protasov.swipehome.ui.screens.auth

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_apple_logo
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import mymultiplatformproject.shared.generated.resources.ic_camera
import mymultiplatformproject.shared.generated.resources.ic_eye_hidden
import mymultiplatformproject.shared.generated.resources.ic_eye_visible
import mymultiplatformproject.shared.generated.resources.ic_google_logo
import mymultiplatformproject.shared.generated.resources.ic_hint
import org.jetbrains.compose.resources.painterResource
import kotlin.repeat

class RegisterScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Головний стан майстра реєстрації
        var currentStep by remember { mutableStateOf(1) }

        // Стани для збереження даних користувача (будуть заповнюватися на різних кроках)
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "Swipe Home",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
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
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (stepNumber <= currentStep) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                            )
                    )
                }
            }
            Text(
                text = "Крок $currentStep з 4",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --------Вміст поточного кроку--------
            when (currentStep){
                1 -> Step1Content(email = email, onEmailChange = { email = it })
                2 -> Step2Content(email = email, onEmailChange = { email = it }, password = password, onPasswordChange = { password = it })
                3 -> Step3Content(firstName = firstName, onFirstNameChange = { firstName = it }, lastName = lastName, onLastNameChange = { lastName = it })
                4 -> Step4Content(phone = phone, onPhoneChange = { phone = it })
            }

            Spacer(modifier = Modifier.weight(1f)) // Виштовхує кнопки вниз

            // --------Нижні кнопки--------
            if (currentStep == 1) {
                Button(
                    onClick = { currentStep = 2 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Далі", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painterResource(Res.drawable.ic_arrow_next),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Посилання на політику використання
                Column (
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Натискання продовжити, ви погоджуєтесь з",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    TextButton(onClick = { /*TODO: Посилання на політику використання*/ })
                    {
                        Text(
                            text = "Умовами використання",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            } else {
                // Кнопки для кроків 2, 3, 4 (Далі + Назад)
                Button(
                    onClick = {
                        if (currentStep < 4) currentStep++ else {/*TODO: Фінальна реєстація*/}
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ){
                    Text(if (currentStep == 4) "Зареєструватися" else "Далі", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    if (currentStep != 4){
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(painterResource(Res.drawable.ic_arrow_next), contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { currentStep-- },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ){
                    Text("Назад", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    //  Функція для верстки вмісту Першого кроку
    @Composable
    fun Step1Content(email: String, onEmailChange: (String) -> Unit) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Створити акаунт",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Швидкий старт для пошуку ідеального дому",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопки соц. мереж
            OutlinedButton(
                onClick = {/*TODO*/},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ){
                Icon(painterResource(Res.drawable.ic_google_logo), contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Продовжити з Google", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {/*TODO*/},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ){
                Icon(painterResource(Res.drawable.ic_apple_logo), contentDescription = null, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Продовжити з Apple ID", fontSize = 16.sp, color = MaterialTheme.colorScheme.background)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Розділювач "АБО"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)))
                Text(
                    text = "АБО",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Email адреса",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                placeholder = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
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
                text = "Безпека",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Створіть надійниі облікові дані для захисту вашого акаунта.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле Email (користувач може його змінити якщо помилився на першому кроці)
            Text(
                text = "Email або Логін",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email або Логін") },
                placeholder = { Text("Введіть ваш Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Поле Пароль
            Text(
                text = "Пароль",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {Text("Введіть ваш пароль")},
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                // Маскуємо пароль крапочками, якщо passwordVisible == false
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                // Іконка для перемикання видимості пароля
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            painterResource(
                                if (passwordVisible) Res.drawable.ic_eye_visible else  Res.drawable.ic_eye_hidden
                            ),
                            contentDescription = null, modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Підказка про складність пароля
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(painterResource(Res.drawable.ic_hint), contentDescription = null, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Додайте щонайменше одну цифру та велику літеру",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )

            }

        }
    }

    @Composable
    fun Step3Content(
        firstName: String,
        onFirstNameChange: (String) -> Unit,
        lastName: String,
        onLastNameChange: (String) -> Unit
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            // Заголовок та опис
            Text(
                text = "Про себе",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

//            Text(
//                text = "Додайте інформацію про себе для пошуку",
//                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
//                fontSize = 16.sp
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))

            // Кнопка для додавання фотографії
            Column (
                modifier =  Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally // Центруємо фотографію
                ) {
                OutlinedButton(
                        onClick = { /*TODO: Доступ до галереї та завантаження фотографій*/},
                    modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                    ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painterResource(Res.drawable.ic_camera),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Додати фото",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ім'я",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = { Text("Ім'я") },
                placeholder = { Text("Ім'я") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Прізвище",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = { Text("Прізвище") },
                placeholder = { Text("Прізвище") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }

    @Composable
    fun Step4Content(
        phone: String,
        onPhoneChange: (String) -> Unit
    ){
        // Локальний стан для галочки "я погоджуюсь"
        var termsAccepted by remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Контакти",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Останній крок для створення вашого профілю",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле для номера телефону
            Text(
                text = "Номер телефону",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                placeholder = { Text("X XXX XX XXX") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                //
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                // Додаємо незмінний префікс країни
                leadingIcon = {
                    Text(
                        text = "+380",
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Блок згоди з правилами
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                    .padding(16.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Я погоджуюсь з Умовами використання та Політикою конфіденціальності",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 20.sp
                    )
                }
            }
        }

    }
}