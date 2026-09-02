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
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.btn_accept_code
import mymultiplatformproject.shared.generated.resources.btn_back
import mymultiplatformproject.shared.generated.resources.btn_next
import mymultiplatformproject.shared.generated.resources.btn_resend_code
import mymultiplatformproject.shared.generated.resources.btn_reset_paswd
import mymultiplatformproject.shared.generated.resources.btn_send_code
import mymultiplatformproject.shared.generated.resources.email_login_label
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
import mymultiplatformproject.shared.generated.resources.fogot_paswd_subtitle_step0
import mymultiplatformproject.shared.generated.resources.fogot_paswd_subtitle_step1
import mymultiplatformproject.shared.generated.resources.fogot_paswd_subtitle_step3
import mymultiplatformproject.shared.generated.resources.fogot_paswd_title_step1
import mymultiplatformproject.shared.generated.resources.fogot_paswd_title_step2
import mymultiplatformproject.shared.generated.resources.fogot_paswd_title_step3
import mymultiplatformproject.shared.generated.resources.fogot_paswd_title_step4
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
import mymultiplatformproject.shared.generated.resources.reg_email_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class ForgotPasswordScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        var currentStep by rememberSaveable { mutableStateOf(1) }
        var loginOrEmail by rememberSaveable { mutableStateOf("") }
        var recoveryMethod by rememberSaveable { mutableStateOf("sms") }
        var otpCode by rememberSaveable { mutableStateOf("") }
        var newPassword by rememberSaveable { mutableStateOf("") }
        var confirmPassword by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SwipeHomeTheme.colors.background)
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
                        tint = SwipeHomeTheme.colors.neutral
                    )
                }

                Text(
                    text = when(currentStep){
                        1 -> stringResource(Res.string.fogot_paswd_title_step1)
                        2 -> stringResource(Res.string.fogot_paswd_title_step2)
                        3 -> stringResource(Res.string.fogot_paswd_title_step3)
                        else -> stringResource(Res.string.fogot_paswd_title_step4)
                    },
                    style = SwipeHomeTheme.typography.subheadline,
                    color = SwipeHomeTheme.colors.primary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- ВМІСТ КРОКІВ ---
            when(currentStep){
                1 -> Step1RecoveryMethod(
                    loginOrEmail = loginOrEmail,
                    onLoginOrEmailChange = { loginOrEmail = it }
                )
                2 -> Step2RecoveryMethod(
                    selectedMethod = recoveryMethod,
                    onMethodSelected = { recoveryMethod = it }
                )
                3 -> Step3OutpInput(
                    otpCode = otpCode,
                    onOtpCodeChange = { otpCode = it },
                    selectedMethod = recoveryMethod,
                )
                4 -> Step4NewPassword(
                    newPassword = newPassword,
                    onNewPasswordChange = { newPassword = it },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = { confirmPassword = it }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            // --- НИЖНЯ КНОПКА ---
            Button(
                onClick = {
                    if (currentStep < 4) currentStep++ else {/*TODO: Оновлення пароля*/}
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = SwipeHomeTheme.shapes.smallShape,
                colors = ButtonDefaults.buttonColors(containerColor = SwipeHomeTheme.colors.primary)
            ){
                Text(
                    text = when (currentStep){
                        1 -> stringResource(Res.string.btn_next)
                        2 -> stringResource(Res.string.btn_send_code)
                        3 -> stringResource(Res.string.btn_accept_code)
                        else -> stringResource(Res.string.btn_reset_paswd)
                    },
                    style = SwipeHomeTheme.typography.body,
                    fontWeight = FontWeight.Bold,
                    color = SwipeHomeTheme.colors.onPrimary
                )
            }
        }
    }

    // --- Крок 1: Ввід логіну ---
    @Composable
    fun Step1RecoveryMethod(loginOrEmail: String, onLoginOrEmailChange: (String) -> Unit){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.email_login_label),
                modifier = Modifier.fillMaxWidth(),
                style = SwipeHomeTheme.typography.headline,
                textAlign = TextAlign.Center,
                color = SwipeHomeTheme.colors.neutral,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.fogot_paswd_subtitle_step0),
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                style = SwipeHomeTheme.typography.body,
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = loginOrEmail,
                onValueChange = onLoginOrEmailChange,
                label = { Text(stringResource(Res.string.email_login_label)) },
                placeholder = { Text(stringResource(Res.string.reg_email_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // --- Крок 2: Вибір способу ---
    @Composable
    fun Step2RecoveryMethod(selectedMethod: String, onMethodSelected: (String) -> Unit) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.fogot_paswd_header_step1),
                style = SwipeHomeTheme.typography.headline,
                textAlign = TextAlign.Start,
                color = SwipeHomeTheme.colors.neutral,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.fogot_paswd_subtitle_step1),
                style = SwipeHomeTheme.typography.label,
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                textAlign = TextAlign.Start,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            RecoveryMethodCard(
                icon = painterResource(Res.drawable.ic_sms),
                title = stringResource(Res.string.fogot_paswd_method_sms_title),
                subtitle = stringResource(Res.string.fogot_paswd_method_sms_subtitle),
                isSelected = selectedMethod == "sms",
                onClick = { onMethodSelected("sms") }
            )

            Spacer(modifier = Modifier.height(16.dp))

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
        val borderColor = if (isSelected) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.outline
        val bgColor = SwipeHomeTheme.colors.surface

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(SwipeHomeTheme.shapes.mediumShape)
                .background(bgColor)
                .border(BorderStroke(2.dp, borderColor), SwipeHomeTheme.shapes.mediumShape)
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(SwipeHomeTheme.shapes.smallShape)
                    .background(SwipeHomeTheme.colors.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = SwipeHomeTheme.colors.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)){
                Text(title, style = SwipeHomeTheme.typography.body, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.neutral)
                Text(subtitle, color = SwipeHomeTheme.colors.onSurfaceSecondary, style = SwipeHomeTheme.typography.caption)
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = SwipeHomeTheme.colors.primary,
                    unselectedColor = SwipeHomeTheme.colors.onSurfaceSecondary
                )
            )
        }
    }

    // --- Крок 3: Введення OTP ---
    @Composable
    fun Step3OutpInput(otpCode: String, onOtpCodeChange: (String) -> Unit, selectedMethod: String) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.fogot_paswd_header_step2),
                modifier = Modifier.fillMaxWidth(),
                style = SwipeHomeTheme.typography.headline,
                textAlign = TextAlign.Center,
                color = SwipeHomeTheme.colors.neutral,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (selectedMethod == "sms") stringResource(Res.string.fogot_paswd_subtitle_sms_step2) else if (selectedMethod == "email") stringResource(Res.string.fogot_paswd_subtitle_email_step2) else "",
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                style = SwipeHomeTheme.typography.body,
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(48.dp))

            BasicTextField(
                value = otpCode,
                onValueChange = { input ->
                    val digitsOnly = input.filter { it.isDigit() }
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
                            val isFocused = otpCode.length == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(SwipeHomeTheme.shapes.smallShape)
                                    .background(SwipeHomeTheme.colors.surface)
                                    .border(
                                        2.dp,
                                        if (isFocused) SwipeHomeTheme.colors.primary else SwipeHomeTheme.colors.outline,
                                        SwipeHomeTheme.shapes.smallShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char.ifEmpty { "-" },
                                    style = SwipeHomeTheme.typography.headline,
                                    color = SwipeHomeTheme.colors.primary
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
                    color = SwipeHomeTheme.colors.primary,
                    style = SwipeHomeTheme.typography.label,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // --- КРОК 4: Новий пароль ---
    @Composable
    fun Step4NewPassword(
        newPassword: String,
        onNewPasswordChange: (String) -> Unit,
        confirmPassword: String,
        onConfirmPasswordChange: (String) -> Unit ){
        var newPasswordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }

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

        val strengthText = when (strength) {
            1 -> stringResource(Res.string.password_strength_weak)
            2 -> stringResource(Res.string.password_strength_medium)
            3 -> stringResource(Res.string.password_strength_strong)
            4 -> stringResource(Res.string.password_strength_very_strong)
            else -> ""
        }
        val strengthColor = when (strength) {
            1 -> SwipeHomeTheme.colors.error
            2 -> Color(0xFFFB8C00)
            3 -> Color(0xFF7CB342)
            4 -> SwipeHomeTheme.colors.primary
            else -> Color.Transparent
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.fogot_paswd_header_step3),
                style = SwipeHomeTheme.typography.headline,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.fogot_paswd_subtitle_step3),
                color = SwipeHomeTheme.colors.onSurfaceSecondary,
                style = SwipeHomeTheme.typography.body
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Поле "Новий пароль"
            Text(
                text = stringResource(Res.string.fogot_paswd_new_password_label),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.SemiBold,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                placeholder = { Text(stringResource(Res.string.fogot_paswd_new_password_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape,
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            painterResource(
                                if (newPasswordVisible) Res.drawable.ic_eye_visible else Res.drawable.ic_eye_hidden
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
            ){
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (newPassword.isNotEmpty() && index < strength) strengthColor
                                else SwipeHomeTheme.colors.outline
                            )
                    )
                }
            }

            if(newPassword.isNotEmpty()){
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.fogot_paswd_strength_prefix) + strengthText,
                    color = SwipeHomeTheme.colors.onSurfaceSecondary,
                    style = SwipeHomeTheme.typography.caption
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Поле "Підтвердити пароль"
            Text(
                text = stringResource(Res.string.fogot_paswd_confirm_password_label),
                style = SwipeHomeTheme.typography.label,
                fontWeight = FontWeight.SemiBold,
                color = SwipeHomeTheme.colors.neutral
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                placeholder = { Text(stringResource(Res.string.fogot_paswd_confirm_password_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = SwipeHomeTheme.shapes.smallShape,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painterResource(
                                if (confirmPasswordVisible) Res.drawable.ic_eye_visible else Res.drawable.ic_eye_hidden
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = SwipeHomeTheme.colors.onSurfaceSecondary
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Інформаційний блок
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SwipeHomeTheme.shapes.smallShape)
                    .background(SwipeHomeTheme.colors.surface)
                    .padding(16.dp),
            ){
                Row{
                    Icon(
                        painterResource(Res.drawable.ic_hint),
                        contentDescription = null,
                        tint = SwipeHomeTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(Res.string.fogot_paswd_hint),
                        style = SwipeHomeTheme.typography.label,
                        color = SwipeHomeTheme.colors.onSurfaceSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
