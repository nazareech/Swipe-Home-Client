package com.nazar_protasov.swipehome.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import mymultiplatformproject.shared.generated.resources.btn_accept_code
import mymultiplatformproject.shared.generated.resources.btn_back
import mymultiplatformproject.shared.generated.resources.btn_resend_code
import mymultiplatformproject.shared.generated.resources.btn_reset_paswd
import mymultiplatformproject.shared.generated.resources.btn_send_code
import mymultiplatformproject.shared.generated.resources.fogot_paswd_confirm_password_label
import mymultiplatformproject.shared.generated.resources.fogot_paswd_confirm_password_placeholder
import mymultiplatformproject.shared.generated.resources.fogot_paswd_header_step1
import mymultiplatformproject.shared.generated.resources.fogot_paswd_header_step2
import mymultiplatformproject.shared.generated.resources.fogot_paswd_header_step3
import mymultiplatformproject.shared.generated.resources.fogot_paswd_hint
import mymultiplatformproject.shared.generated.resources.fogot_paswd_method_email_subtitle
import mymultiplatformproject.shared.generated.resources.fogot_paswd_method_email_title
import mymultiplatformproject.shared.generated.resources.fogot_paswd_method_sms_subtitle
import mymultiplatformproject.shared.generated.resources.fogot_paswd_method_sms_title
import mymultiplatformproject.shared.generated.resources.fogot_paswd_new_password_label
import mymultiplatformproject.shared.generated.resources.fogot_paswd_new_password_placeholder
import mymultiplatformproject.shared.generated.resources.fogot_paswd_strength_prefix
import mymultiplatformproject.shared.generated.resources.fogot_paswd_subtitle_email_step2
import mymultiplatformproject.shared.generated.resources.fogot_paswd_subtitle_sms_step2
import mymultiplatformproject.shared.generated.resources.fogot_paswd_subtitle_step1
import mymultiplatformproject.shared.generated.resources.fogot_paswd_subtitle_step2
import mymultiplatformproject.shared.generated.resources.fogot_paswd_subtitle_step3
import mymultiplatformproject.shared.generated.resources.fogot_paswd_title_step1
import mymultiplatformproject.shared.generated.resources.fogot_paswd_title_step2
import mymultiplatformproject.shared.generated.resources.fogot_paswd_title_step3
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_email
import mymultiplatformproject.shared.generated.resources.ic_eye_hidden
import mymultiplatformproject.shared.generated.resources.ic_eye_visible
import mymultiplatformproject.shared.generated.resources.ic_hint
import mymultiplatformproject.shared.generated.resources.ic_sms
import mymultiplatformproject.shared.generated.resources.password_strength_medium
import mymultiplatformproject.shared.generated.resources.password_strength_strong
import mymultiplatformproject.shared.generated.resources.password_strength_very_strong
import mymultiplatformproject.shared.generated.resources.password_strength_weak
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class ForgotPasswordScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        // Стани екрана
        var currentStep by rememberSaveable { mutableStateOf(1) }
        var recoveryMethod by rememberSaveable { mutableStateOf("sms") } // "sms" або "email"
        var otpCode by rememberSaveable { mutableStateOf("") }
        var newPassword by rememberSaveable { mutableStateOf("") }
        var confirmPassword by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 24.dp)
                .verticalScroll(scrollState)
        ) {
            // --- ВЕРХНЯ ПАНЕЛЬ ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        if (currentStep > 1) currentStep-- else navigator.pop()
                    }
                ){
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = stringResource(Res.string.btn_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = when(currentStep){
                        1 -> stringResource(Res.string.fogot_paswd_title_step1)
                        2 -> stringResource(Res.string.fogot_paswd_title_step2)
                        else -> stringResource(Res.string.fogot_paswd_title_step3)
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(48.dp))
            }

            Spacer(modifier = Modifier.padding(32.dp))

            // --- ВМІСТ КРОКІВ ---
            when(currentStep){
                1 -> Step1RecoveryMethod(
                    selectedMethod = recoveryMethod,
                    onMethodSelected = { recoveryMethod = it }
                )
                2 -> Step2OutpInput(
                    otpCode = otpCode,
                    onOtpCodeChange = { otpCode = it },
                    selectedMethod = recoveryMethod,
                )
                3 -> Step3NewPassword(
                    newPassword = newPassword,
                    onNewPasswordChange = { newPassword = it },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = { confirmPassword = it }
                )
            }
            Spacer(modifier = Modifier.weight(1f)) // Висота для залишку
            Spacer(modifier = Modifier.height(32.dp))

            // --- НИЖНЯ КНОПКА ---
            Button(
                onClick = {
                    if (currentStep < 3) currentStep++ else {/*TODO: Оновлення пароля*/}
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ){
                Text(
                    text = when (currentStep){
                        1 -> stringResource(Res.string.btn_send_code)
                        2 -> stringResource(Res.string.btn_accept_code)
                        else -> stringResource(Res.string.btn_reset_paswd)
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // --- Крок 1: Вибір способу ---
    @Composable
    fun Step1RecoveryMethod(selectedMethod: String, onMethodSelected: (String) -> Unit) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = stringResource(Res.string.fogot_paswd_header_step1),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.fogot_paswd_subtitle_step1),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Start,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Картка SMS
            RecoveryMethodCard(
                icon = painterResource(Res.drawable.ic_sms),
                title = stringResource(Res.string.fogot_paswd_method_sms_title),
                subtitle = stringResource(Res.string.fogot_paswd_method_sms_subtitle),
                isSelected = selectedMethod == "sms",
                onClick = { onMethodSelected("sms") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Картка Email
            RecoveryMethodCard(
                icon = painterResource(Res.drawable.ic_email),
                title = stringResource(Res.string.fogot_paswd_method_email_title),
                subtitle = stringResource(Res.string.fogot_paswd_method_email_subtitle),
                isSelected = selectedMethod == "email",
                onClick = { onMethodSelected("email") }
            )
        }
    }

    @Composable
    fun RecoveryMethodCard(icon: Painter, title: String, subtitle: String, isSelected: Boolean, onClick: () -> Unit) {
        val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
        val bgColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(bgColor)
                .border(BorderStroke(2.dp, borderColor), RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)){
                Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Text(subtitle, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), fontSize = 14.sp)
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
            )
        }
    }

    // --- Крок 2: Введення OTP ---
    @Composable
    fun Step2OutpInput(otpCode: String, onOtpCodeChange: (String) -> Unit, selectedMethod: String) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.fogot_paswd_header_step2),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (selectedMethod == "sms") stringResource(Res.string.fogot_paswd_subtitle_sms_step2) else if (selectedMethod == "email") stringResource(Res.string.fogot_paswd_subtitle_email_step2) else "",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Кастомне поле для OTP
            BasicTextField(
                value = otpCode,
                onValueChange = { input ->
                    // Фільтруємо текст, залишаючи тільки цифри
                    val digitsOnly = input.filter { it.isDigit() }
                    // Перевіряємо довжину відфільтрованого тексту
                    if (digitsOnly.length <= 6) onOtpCodeChange(digitsOnly)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(6) { index ->
                            val char = when {
                                index >= otpCode.length -> ""
                                else -> otpCode[index].toString()
                            }
                            // Візуальний квадрат для кожної цифри
                            val isFocused = otpCode.length == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                                    .border(
                                        2.dp,
                                        if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                // Якщо символ є, показуємо його або крапку
                                Text(
                                    text = if (char.isNotEmpty()) char else "-",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(
                onClick = {/*TODO: Отримати новий код*/}) {
                Text(
                    text = stringResource(Res.string.btn_resend_code),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }

    // --- КРОК 3: Новий пароль ---
    @Composable
    fun Step3NewPassword(
        newPassword: String,
        onNewPasswordChange: (String) -> Unit,
        confirmPassword: String,
        onConfirmPasswordChange: (String) -> Unit ){
        var newPasswordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }

        // Логіка визначення надійності пароля (від 0 до 4)
        val strength = remember(newPassword) {
            var score = 0
            if (newPassword.isNotEmpty()) {
                if (newPassword.length >= 8) score++
                if (newPassword.any { it.isLetter() }) score++
                if (newPassword.any { it.isDigit() }) score++
                if (newPassword.any { !it.isLetterOrDigit() }) score++
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
            1 -> Color(0xFFE53935) // Червоний
            2 -> Color(0xFFFB8C00) // Помаранчевий
            3 -> Color(0xFF7CB342) // Світло-зелений
            4 -> Color(0xFF43A047) // Темно-зелений
            else -> Color.Transparent
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.fogot_paswd_header_step3),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.fogot_paswd_subtitle_step3),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Поле "Новий пароль"
            Text(
                text = stringResource(Res.string.fogot_paswd_new_password_label),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                placeholder = { Text(stringResource(Res.string.fogot_paswd_new_password_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            painterResource(
                                if (newPasswordVisible) Res.drawable.ic_eye_visible else  Res.drawable.ic_eye_hidden
                            ),
                            contentDescription = null, modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Індикатор надійності
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (newPassword.isNotEmpty() && index < strength) strengthColor
                                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                            )
                    )
                }
            }

            if(newPassword.isNotEmpty()){
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.fogot_paswd_strength_prefix) + strengthText,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Поле "Підтвердити пароль"
            Text(
                text = stringResource(Res.string.fogot_paswd_confirm_password_label),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                placeholder = { Text(stringResource(Res.string.fogot_paswd_confirm_password_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            painterResource(
                                if (newPasswordVisible) Res.drawable.ic_eye_visible else  Res.drawable.ic_eye_hidden
                            ),
                            contentDescription = null, modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Інформаційний блок
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                    .padding(16.dp),
            ){
                Row{
                    Icon(painterResource(Res.drawable.ic_hint), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(Res.string.fogot_paswd_hint),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        lineHeight = 20.sp
                    )
                }
            }
        }

    }
}

