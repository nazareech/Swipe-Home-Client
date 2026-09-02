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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.RootScreen
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.btn_register
import mymultiplatformproject.shared.generated.resources.email_login_label
import mymultiplatformproject.shared.generated.resources.ic_apple_logo
import mymultiplatformproject.shared.generated.resources.ic_eye_hidden
import mymultiplatformproject.shared.generated.resources.ic_eye_visible
import mymultiplatformproject.shared.generated.resources.ic_google_logo
import mymultiplatformproject.shared.generated.resources.login_btn_enter
import mymultiplatformproject.shared.generated.resources.login_create_account
import mymultiplatformproject.shared.generated.resources.login_enter_with
import mymultiplatformproject.shared.generated.resources.login_fogot_password
import mymultiplatformproject.shared.generated.resources.login_subtitle
import mymultiplatformproject.shared.generated.resources.login_title
import mymultiplatformproject.shared.generated.resources.password_label
import mymultiplatformproject.shared.generated.resources.reg_continue_apple
import mymultiplatformproject.shared.generated.resources.reg_continue_google
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class LoginScreen: Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Змінні стану, які зберігають введений текст
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        // Змінна для перемикання видимості пароля
        var passwordVisible by remember { mutableStateOf(false) }
        // Змінна для прокручуваного екрана
        val scrollState = rememberScrollState()
        // Змінна для стану завантаження
        var isLoading by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(SwipeHomeTheme.colors.background)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            // Заголовок
            Text(
                text = stringResource(Res.string.login_title),
                style = SwipeHomeTheme.typography.headline,
                color = SwipeHomeTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.login_subtitle),
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                style = SwipeHomeTheme.typography.body
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле вводу для email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(Res.string.email_login_label)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле вводу для Пароля
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(Res.string.password_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape,
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
                                if (passwordVisible) Res.drawable.ic_eye_visible else Res.drawable.ic_eye_hidden
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = SwipeHomeTheme.colors.onSurfaceSecondary
                        )
                    }
                }
            )

            // Посилання "Забули пароль?"
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                TextButton(onClick = { navigator.push(ForgotPasswordScreen()) }) {
                    Text(
                        text = stringResource(Res.string.login_fogot_password),
                        color = SwipeHomeTheme.colors.primary,
                        style = SwipeHomeTheme.typography.subheadline
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Головна кнопка увійти
            Button(
                onClick = {
                    if (!isLoading) {
                        isLoading = true
                        /*TODO: Логіка авторизації Ktor*/
                        navigator.replaceAll(RootScreen())
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = SwipeHomeTheme.shapes.smallShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SwipeHomeTheme.colors.primary
                )
            ){
                Text(
                    stringResource(Res.string.login_btn_enter),
                    style = SwipeHomeTheme.typography.body,
                    fontWeight = FontWeight.Bold,
                    color = SwipeHomeTheme.colors.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Розділювач для соціальних мереж
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier.weight(1f).height(1.dp).background(SwipeHomeTheme.colors.outline))
                Text(
                    text = stringResource(Res.string.login_enter_with),
                    color = SwipeHomeTheme.colors.onSurfaceSecondary,
                    style = SwipeHomeTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier.weight(1f).height(1.dp).background(SwipeHomeTheme.colors.outline))
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
                    shape = SwipeHomeTheme.shapes.smallShape
                ){
                    Icon(painterResource(Res.drawable.ic_google_logo), contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Unspecified)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        stringResource(Res.string.reg_continue_google),
                        style = SwipeHomeTheme.typography.body,
                        fontWeight = FontWeight.Bold,
                        color = SwipeHomeTheme.colors.neutral
                    )
                }
                Button(
                    onClick = { /*TODO: Логіка авторизації Apple*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = SwipeHomeTheme.shapes.smallShape,
                    colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.neutral)
                ){
                    Icon(painterResource(Res.drawable.ic_apple_logo), contentDescription = null, modifier = Modifier.size(32.dp), tint = SwipeHomeTheme.colors.background)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        stringResource(Res.string.reg_continue_apple),
                        style = SwipeHomeTheme.typography.body,
                        fontWeight = FontWeight.Bold,
                        color = SwipeHomeTheme.colors.background
                    )
                }
            }

            // Spacer(modifier = Modifier.weight(1f)) // Виштовхуємо блок до самого низу

            // Посилання на реєстрацію
            Row(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(Res.string.login_create_account),
                    color = SwipeHomeTheme.colors.onSurfaceSecondary,
                    style = SwipeHomeTheme.typography.label
                )
                TextButton(onClick = { navigator.push(RegisterScreen()) })
                {
                    Text(
                        text = stringResource(Res.string.btn_register),
                        color = SwipeHomeTheme.colors.primary,
                        style = SwipeHomeTheme.typography.label,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
