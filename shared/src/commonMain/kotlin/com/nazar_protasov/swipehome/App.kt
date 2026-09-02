package com.nazar_protasov.swipehome

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.nazar_protasov.swipehome.di.appModule
import com.nazar_protasov.swipehome.ui.screens.OnboardingScreen
import com.nazar_protasov.swipehome.ui.screens.SplashScreen
import com.nazar_protasov.swipehome.ui.screens.details.PropertyDetailsScreen
import com.nazar_protasov.swipehome.ui.screens.home.FilterScreen
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    KoinApplication(application = {
            modules(appModule)
    }) {
        SwipeHomeTheme {
            // Navigator починає роботу з SplashScreen
            Navigator(SplashScreen()) { navigator ->
                AnimatedContent(
                    targetState = navigator.lastItem,
                    transitionSpec = {
                        // Визначаємо, чи рухаємося ми вперед (Push або Replace)
                        val ifForward = navigator.lastEvent == StackEvent.Push || navigator.lastEvent == StackEvent.Replace

                        // Спільний tween для всіх
                        val animationSpec = tween<IntOffset>(durationMillis = 250, easing = FastOutSlowInEasing)
                        val alphaSpec = tween<Float>(durationMillis = 250, easing = FastOutSlowInEasing)
                        if (ifForward){
                            // Перехід вперед (Відкриття екрана)

                            // Спеціальний випадок: Якщо завершуємо Onboarding, він має поїхати ВГОРУ
                            if (targetState is OnboardingScreen){
                                fadeIn(alphaSpec) togetherWith  slideOutVertically(
                                    animationSpec = animationSpec,
                                    targetOffsetY = { fullHeight -> -fullHeight }
                                )
                            }
                            // Правила для інших екранів при відкритті
                            else when(targetState){
                                is FilterScreen -> {
                                    // Фільтри виїжджають ЗВЕРХУ вниз
                                    slideInVertically(
                                    animationSpec = animationSpec,
                                        initialOffsetY = { fullHeight -> -fullHeight }
                                    ) togetherWith fadeOut(animationSpec = tween(400))
                                }

                                is PropertyDetailsScreen -> {
                                    // Для Details та інших екранів -> виїжджає ЗНИЗУ вгору (ti)
                                    slideInVertically (
                                        animationSpec = animationSpec,
                                        initialOffsetY = { fullHeight -> fullHeight }
                                    ) togetherWith fadeOut(animationSpec = tween(400))
                                }

                                else -> {
                                    // Всі інші екрани виїжджають СПРАВА наліво
                                    slideInHorizontally(
                                        animationSpec = animationSpec,
                                        initialOffsetX = { fullWidth -> fullWidth }
                                    ) togetherWith fadeOut(animationSpec = tween(400))
                                }
                            }

                        } else {
                            // ПЕРЕХІД НАЗАД (ЗАКРИТТЯ) (StackEvent.Pop)
                            when (initialState) {
                                is FilterScreen -> {
                                    // Фільтри ховаються назад ВГОРУ
                                    fadeIn(animationSpec = alphaSpec) togetherWith slideOutVertically(
                                        animationSpec = animationSpec,
                                        targetOffsetY = { fullHeight -> -fullHeight }
                                    )
                                }

                                is PropertyDetailsScreen -> {
                                    // Деталі ховаються назад ВНИЗ
                                    fadeIn(animationSpec = alphaSpec) togetherWith slideOutVertically(
                                        animationSpec = animationSpec,
                                        targetOffsetY = { fullHeight -> fullHeight }
                                    )
                                }

                                else -> {
                                    // Всі інші екрани ховаються ЗЛІВА направо
                                    fadeIn(animationSpec = alphaSpec) togetherWith slideOutHorizontally(
                                        animationSpec = animationSpec,
                                        targetOffsetX = { fullWidth -> fullWidth }
                                    )
                                }
                            }
                        }
                    },
                    label = "RootNavigatorAnimation"
                ){ currentScreen ->
                    navigator.saveableState("transition", currentScreen){
                        currentScreen.Content()
                    }
                }
            }
        }
    }
}


