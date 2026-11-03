package com.nazar_protasov.swipehome.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.data.local.SessionManager
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.network.dto.RegisterRequestDTO
import com.nazar_protasov.swipehome.ui.utils.CountryPhoneCode
import com.nazar_protasov.swipehome.ui.utils.getLocalizedMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

class RegisterScreenModel(
    private val apiService: PropertyApiService,
    private val sessionManager: SessionManager
) : ScreenModel {

    // Головний стан майстра реєстрації
    var currentStep by mutableStateOf(1)
    val totalSteps = 4

    // Стани для збереження даних користувача (заповнюються на різних кроках)
    var login by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")
    var countryCode by mutableStateOf("+380") // Код за замовчуванням
    var photoByteArray by mutableStateOf<ByteArray?>(null) // Готовий масив байтів для POST-запиту
    var termsAccepted by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
        private set

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    // Перевірка полів для переходів між кроками
    fun nextStep() {
        when (currentStep) {
            1 -> {
                if (email.isBlank() || !email.contains("@")) {
                    _snackbarEvent.trySend("Будь ласка, введіть коректну Email адресу")
                    return
                }
            }
            2 -> {
                if (login.isBlank()) {
                    _snackbarEvent.trySend("Будь ласка, заповніть поле логіну")
                    return
                }
                if (email.isBlank() || !email.contains("@")) {
                    _snackbarEvent.trySend("Будь ласка, введіть коректну Email адресу")
                    return
                }
                if (password.isBlank()) {
                    _snackbarEvent.trySend("Будь ласка, введіть пароль")
                    return
                }
                if (password.length < 8) {
                    _snackbarEvent.trySend("Пароль має містити щонайменше 8 символів")
                    return
                }
            }
            3 -> {
                if (firstName.isBlank() || lastName.isBlank()) {
                    _snackbarEvent.trySend("Будь ласка, введіть ім'я та прізвище")
                    return
                }
            }
        }
        if (currentStep < totalSteps) {
            currentStep++
        }
    }

    // Повна валідація всіх обов'язкових полів перед відправкою на сервер
    private fun validateAllFields(): Boolean {
        if (email.isBlank() || !email.contains("@")) {
            _snackbarEvent.trySend("Будь ласка, введіть коректну Email адресу")
            return false
        }
        if (login.isBlank()) {
            _snackbarEvent.trySend("Будь ласка, заповніть поле логіну")
            return false
        }
        if (password.isBlank() || password.length < 8) {
            _snackbarEvent.trySend("Пароль має містити щонайменше 8 символів")
            return false
        }
        if (firstName.isBlank() || lastName.isBlank()) {
            _snackbarEvent.trySend("Будь ласка, заповніть ім'я та прізвище")
            return false
        }
        val expectedDigits = CountryPhoneCode.getMaskByCode(countryCode).count { it == '0' }
        if (phone.isBlank() || phone.length < expectedDigits) {
            _snackbarEvent.trySend("Будь ласка, введіть повний номер телефону")
            return false
        }
        if (!termsAccepted) {
            _snackbarEvent.trySend("Будь ласка, погодьтеся з правилами користування")
            return false
        }
        return true
    }

    fun registerUser(onSuccess: () -> Unit) {
        if (isLoading) return

        // Якщо не заповнені всі необхідні поля — не відправляємо запит
        if (!validateAllFields()) return

        screenModelScope.launch {
            isLoading = true
            try {
                val trimmedEmail = email.trim()
                val fullPhone = "$countryCode ${phone.trim()}".trim()

                val request = RegisterRequestDTO(
                    login = login,
                    email = trimmedEmail,
                    password = password,
                    firstName = firstName.trim(),
                    lastName = lastName.trim(),
                    phone = fullPhone,
                    isVerifiedOwner = false,
                    isAdmin = false
                )

                val response = withTimeoutOrNull(15.seconds) {
                    apiService.registerUser(request)
                }

                if (response != null && response.token.isNotBlank()) {
                    val token = response.token
                    // Зберігаємо токен у SessionManager
                    sessionManager.saveToken(token)

                    // Якщо користувач обрав фото профілю на 3-му кроці — відправляємо його
                    photoByteArray?.let { bytes ->
                        withTimeoutOrNull(15.seconds) {
                            apiService.updatedAvatar(token = token, photoBytes = bytes)
                        }
                    }

                    // Підвантажуємо інформацію профілю у SessionManager
                    withTimeoutOrNull(10.seconds) {
                        apiService.fetchProfileInfo(token = token)
                    }?.let { profile ->
                        sessionManager.saveProfile(profile)
                    }

                    onSuccess()
                } else {
                    _snackbarEvent.send("Помилка реєстрації. Перевірте введені дані та спробуйте ще раз.")
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі при реєстрації: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                isLoading = false
            }
        }
    }
}
