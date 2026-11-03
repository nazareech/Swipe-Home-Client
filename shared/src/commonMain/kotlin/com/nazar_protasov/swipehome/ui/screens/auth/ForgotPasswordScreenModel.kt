package com.nazar_protasov.swipehome.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.utils.getLocalizedMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

class ForgotPasswordScreenModel(
    private val apiService: PropertyApiService
) : ScreenModel {

    var currentStep by mutableStateOf(1)
    var loginOrEmail by mutableStateOf("")
    var recoveryMethod by mutableStateOf("sms")
    var otpCode by mutableStateOf("")
    var newPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    fun onMainButtonClick(onSuccess: () -> Unit) {
        when (currentStep) {
            1 -> {
                if (loginOrEmail.isBlank()) {
                    _snackbarEvent.trySend("Будь ласка, введіть Email або логін")
                    return
                }
                currentStep = 2
            }
            2 -> sendForgotPasswordRequest(moveToNextStep = true)
            3 -> verifyOtpCode()
            4 -> submitNewPassword(onSuccess)
        }
    }

    fun resendCode() {
        sendForgotPasswordRequest(moveToNextStep = false)
    }

    private fun sendForgotPasswordRequest(moveToNextStep: Boolean) {
        if (isLoading) return
        if (loginOrEmail.isBlank()) {
            _snackbarEvent.trySend("Будь ласка, введіть Email або логін")
            return
        }

        screenModelScope.launch {
            isLoading = true
            try {
                val success = withTimeoutOrNull(15.seconds) {
                    apiService.forgotPassword(loginOrEmail.trim())
                } ?: false

                if (success) {
                    if (moveToNextStep) {
                        currentStep = 3
                    } else {
                        _snackbarEvent.send("Код відправлено повторно")
                    }
                } else {
                    _snackbarEvent.send("Не вдалося відправити код. Перевірте введені дані.")
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі при відновленні пароля: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                isLoading = false
            }
        }
    }

    private fun verifyOtpCode() {
        if (isLoading) return
        if (otpCode.length != 6) {
            _snackbarEvent.trySend("Будь ласка, введіть 6-значний код підтвердження")
            return
        }

        screenModelScope.launch {
            isLoading = true
            try {
                val result = withTimeoutOrNull(15.seconds) {
                    apiService.verifyCode(
                        emailOrLogin = loginOrEmail.trim(),
                        code = otpCode.trim()
                    )
                }

                if (result != null && result.isSuccess) {
                    currentStep = 4
                } else {
                    val errorMsg = result?.exceptionOrNull()?.message ?: "Invalid or expired code"
                    _snackbarEvent.send(errorMsg)
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі при перевірці коду: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                isLoading = false
            }
        }
    }

    private fun submitNewPassword(onSuccess: () -> Unit) {
        if (isLoading) return

        if (newPassword.isBlank() || newPassword.length < 8) {
            _snackbarEvent.trySend("Новий пароль має містити щонайменше 8 символів")
            return
        }
        if (newPassword != confirmPassword) {
            _snackbarEvent.trySend("Паролі не співпадають")
            return
        }
        val codeInt = otpCode.trim().toIntOrNull()
        if (codeInt == null) {
            _snackbarEvent.trySend("Невірний формат коду підтвердження")
            return
        }

        screenModelScope.launch {
            isLoading = true
            try {
                val success = withTimeoutOrNull(15.seconds) {
                    apiService.resetPassword(
                        emailOrLogin = loginOrEmail.trim(),
                        code = codeInt,
                        newPassword = newPassword
                    )
                } ?: false

                if (success) {
                    onSuccess()
                } else {
                    _snackbarEvent.send("Не вдалося оновити пароль. Спробуйте ще раз.")
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі при скиданні пароля: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                isLoading = false
            }
        }
    }
}
