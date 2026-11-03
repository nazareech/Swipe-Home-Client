package com.nazar_protasov.swipehome.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.nazar_protasov.swipehome.ui.screens.RootScreen
import com.nazar_protasov.swipehome.ui.screens.auth.components.RegisterBottomButtons
import com.nazar_protasov.swipehome.ui.screens.auth.components.RegisterStepIndicator
import com.nazar_protasov.swipehome.ui.screens.auth.components.RegisterTopBar
import com.nazar_protasov.swipehome.ui.screens.auth.components.Step1Content
import com.nazar_protasov.swipehome.ui.screens.auth.components.Step2Content
import com.nazar_protasov.swipehome.ui.screens.auth.components.Step3Content
import com.nazar_protasov.swipehome.ui.screens.auth.components.Step4Content
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher

class RegisterScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val snackbarHostState = remember { SnackbarHostState() }

        val screenModel = koinScreenModel<RegisterScreenModel>()

        // Підписуємося на події Snackbar з ScreenModel
        LaunchedEffect(Unit) {
            screenModel.snackbarEvent.collect { message ->
                snackbarHostState.showSnackbar(message)
            }
        }

        // Створюємо лаунчер для галереї
        val singleImagePicker = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Single,
            scope = coroutineScope,
            onResult = { byteArrays ->
                // Отримуємо перше вибране фото
                screenModel.photoByteArray = byteArrays.firstOrNull()
            }
        )

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = SwipeHomeTheme.colors.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .background(SwipeHomeTheme.colors.background)
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp, bottom = 24.dp)
                ) {
                    // --------Верхня частина екрана--------
                    RegisterTopBar(
                        onBackClick = {
                            if (screenModel.currentStep > 1) screenModel.currentStep-- else navigator.pop()
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --------Індикатор прогресу--------
                    RegisterStepIndicator(
                        currentStep = screenModel.currentStep,
                        totalSteps = screenModel.totalSteps
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    when (screenModel.currentStep) {
                        1 -> Step1Content(
                            email = screenModel.email,
                            onEmailChange = { screenModel.email = it }
                        )
                        2 -> Step2Content(
                            login = screenModel.login,
                            onLoginChange = {  screenModel.login = it},
                            email = screenModel.email,
                            onEmailChange = { screenModel.email = it },
                            password = screenModel.password,
                            onPasswordChange = { screenModel.password = it }
                        )
                        3 -> Step3Content(
                            firstName = screenModel.firstName,
                            onFirstNameChange = { screenModel.firstName = it },
                            lastName = screenModel.lastName,
                            onLastNameChange = { screenModel.lastName = it },
                            photoData = screenModel.photoByteArray,
                            onPhotoClick = {
                                singleImagePicker.launch()
                            }
                        )
                        4 -> Step4Content(
                            phone = screenModel.phone,
                            onPhoneChange = { screenModel.phone = it },
                            countryCode = screenModel.countryCode,
                            onCountryCodeChange = { screenModel.countryCode = it },
                            termsAccepted = screenModel.termsAccepted,
                            onTermsAcceptedChange = { screenModel.termsAccepted = it }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Виштовхує кнопки вниз

                    // --------Нижні кнопки--------
                    RegisterBottomButtons(
                        currentStep = screenModel.currentStep,
                        isLoading = screenModel.isLoading,
                        onNextOrRegisterClick = {
                            if (screenModel.currentStep < screenModel.totalSteps) {
                                screenModel.nextStep()
                            } else {
                                screenModel.registerUser(
                                    onSuccess = {
                                        navigator.replaceAll(RootScreen())
                                    }
                                )
                            }
                        },
                        onBackClick = {
                            if (screenModel.currentStep > 1) screenModel.currentStep-- else navigator.pop()
                        }
                    )
                }

                if (screenModel.isLoading) {
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
            }
        }
    }
}
