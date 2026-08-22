package com.nazar_protasov.swipehome

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.nazar_protasov.swipehome.di.appModule
import com.nazar_protasov.swipehome.ui.screens.SplashScreen
import com.nazar_protasov.swipehome.ui.screens.home.HomeScreen
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
            modules(appModule)
    }) {
        SwipeHomeTheme {
            // Navigator починає роботу з SplashScreen
            Navigator(SplashScreen()) { navigator ->
                // SingleTransition додає красиву анімацію ковзання при зміні екранів
                SlideTransition(navigator)
            }
        }
    }
}


