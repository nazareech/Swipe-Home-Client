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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_apple_logo
import mymultiplatformproject.shared.generated.resources.ic_eye_hidden
import mymultiplatformproject.shared.generated.resources.ic_eye_visible
import mymultiplatformproject.shared.generated.resources.ic_google_logo
import org.jetbrains.compose.resources.painterResource

class LoginScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Змінні стану, які зберігають введений текст
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        // Змінна для перемикання видимості пароля
        var passwordVisible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp), // Відступи з боків
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Центруємо весь контент по вертикалі
        ){
            // Заголовок
            Text(
                text = "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Увійдіть до свого акаунта",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле вводу для email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email або Логін") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле вводу для Пароля
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
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

            // Посилання "Забили пароль?"
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                TextButton(onClick = { /*TODO: Навігація на екрані відновлення пароля*/ }) {
                    Text(
                        text = "Забули пароль?",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Головна кнопка увійти
            Button(
                onClick = { /*TODO: Логіка авторизації Ktor*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ){
                Text("Увійти", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Розділювач для соціальних мереж
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)))
                Text(
                    text = "Увійти через",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки швидкого входу (Google / Apple)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Відстань між кнопками
            ) {
                OutlinedButton(
                    onClick = { /*TODO: Логіка авторизації Google*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ){
                    Icon(painterResource(Res.drawable.ic_google_logo), contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Продовжити з Google", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                }
                Button(
                    onClick = { /*TODO: Логіка авторизації Apple*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ){
                    Icon(painterResource(Res.drawable.ic_apple_logo), contentDescription = null, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Продовжити з Apple ID", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background)
                }
            }

            // Spacer(modifier = Modifier.weight(1f)) // Виштовхуємо блок до самого низу

            // Посилання на реєстрацію
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Ще немає акаунта?",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                TextButton(onClick = { navigator.push(RegisterScreen()) })
                {
                    Text(
                        text = "Зареєструватися",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}