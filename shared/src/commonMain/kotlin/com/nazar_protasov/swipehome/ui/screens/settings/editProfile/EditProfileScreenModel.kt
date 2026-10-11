package com.nazar_protasov.swipehome.ui.screens.settings.editProfile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.nazar_protasov.swipehome.data.local.SessionManager
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.network.UpdateProfileRequest
import com.nazar_protasov.swipehome.ui.utils.getLocalizedMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

class EditProfileScreenModel(
    private val sessionManager: SessionManager,
    private val apiService: PropertyApiService
) : ScreenModel {

    // Стан завантаження (щоб показувати крутилку)
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    // Канал для Snackbar на екрані списку
    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    // UI Дані для редагування
    val userProfile = sessionManager.userProfileFlow
        .stateIn(screenModelScope, SharingStarted.Lazily, null)

    // TODO: Отримати реальний токен з сервера
    private val fakeUserToken = "c88b4054-e1d9-4c3a-9f05-3ea64b223ee7"

    fun loadProfile(userId: String){

    }

    init{
        fetchProfileInfo()
    }

    fun fetchProfileInfo(){
        screenModelScope.launch {
            _isLoading.value = true
            try {
                // Отримуємо реальний токен із SessionManager
                val token = sessionManager.tokenFlow.firstOrNull() ?: "c88b4054-e1d9-4c3a-9f05-3ea64b223ee7" // return@launch

                // При створенні екрана одразу йдемо на сервер
                println("Робимо запит на сервер для отримання профілю! та передаємо токен: $token")

                // Передаємо токен і поточні фільтри в API
                val result = withTimeoutOrNull(10.seconds) {
                    apiService.fetchProfileInfo(
                        token = token
                    )
                }

                if (result != null) {
                    sessionManager.saveProfile(result)
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveProfileInfo(
        login: String,
        firstName: String,
        lastName: String,
        aboutInfo: String,
        fullPhone: String,
        email: String,
        onSuccess: () -> Unit
    ){
        if (_isLoading.value) return

        screenModelScope.launch {
            _isLoading.value = true

            try {
                // Отримуємо поточний профіль та змінюємо дані
                val currentProfile = userProfile.value ?: return@launch

                val updatedProfile = currentProfile.copy(
                    login = login,
                    firstName = firstName,
                    lastName = lastName,
                    aboutInfo = aboutInfo,
                    phone = fullPhone,
                    email = email
                )

                // Зберігаємо локально у файл JSON
                sessionManager.saveProfile(updatedProfile)

                // Створюємо об'єкт для відправки
                val requestData = UpdateProfileRequest(
                    login = login,
                    firstName = firstName,
                    lastName = lastName,
                    aboutInfo = aboutInfo,
                    email = email,
                    phone = fullPhone,
                    isVerifiedOwner = currentProfile.isVerifiedOwner,
                    isAdmin = currentProfile.isAdmin,
                    createdAt = currentProfile.createdAt,
                    lastSeen = currentProfile.lastSeen,
                    avatarUrl = currentProfile.avatarUrl
                )

                // Отримуємо реальний токен із SessionManager
                val token = sessionManager.tokenFlow.firstOrNull() ?: return@launch
                println("Робимо запит на сервер для збереження профілю")

                // Передаємо токен і поточні фільтри в API
                val result = withTimeoutOrNull(10.seconds) {
                    // TODO: У apiService створити метод updatedProfile
                    apiService.updatedProfile(
                        token = token,
                        profileRequest = requestData
                    )
                }

                if (result != null) {
                    onSuccess()
                } else {
                    _snackbarEvent.send("Помилка збереження профілю. Спробуйте ще раз")
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAvatar(photoByteArray: ByteArray?){
        if (_isLoading.value) return

        if (photoByteArray == null) {
            _snackbarEvent.trySend("Помилка завантаження фото. Спробуйте ще раз")
            return
        }

        screenModelScope.launch {
            _isLoading.value = true

            try {
                // Отримуємо поточний профіль та змінюємо дані
                val token = sessionManager.tokenFlow.firstOrNull() ?: return@launch

                // Передаємо токен і нове фото
                val result = withTimeoutOrNull(10.seconds) {
                    // TODO: У apiService створити метод updatedProfile
                    apiService.updatedAvatar(
                        token = token,
                        photoBytes = photoByteArray
                    )
                }

                if (result != null) {

                    val currentProfile = userProfile.value ?: return@launch

                    val updatedProfile = currentProfile.copy(
                        avatarUrl = result.avatarUrl
                    )
                    // Зберігаємо локально у файл JSON
                    sessionManager.saveProfile(updatedProfile)

                    _snackbarEvent.send("Аватар успішно оновлено")
                } else {
                    _snackbarEvent.send("Помилка завантаження фото. Спробуйте ще раз")
                }

            } catch (e: Exception) {
                if (e is CancellationException) throw e
                println("Помилка мережі: ${e.message}")
                _snackbarEvent.send("Помилка з'єднання: ${e.getLocalizedMessage()}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}