package com.nazar_protasov.swipehome.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.app_name
import mymultiplatformproject.shared.generated.resources.app_title
import org.jetbrains.compose.resources.stringResource

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        //LaunchedEffect запускає корутину при відкритті екрана
        LaunchedEffect(Unit){
            delay(2000) // Затримка 2 секунди (в мілісекундах 2000)

            // Використовуємо replace замість push
            // Це видаляє SplashScreen з історії, щоб користувач не міг повернутися на нього кнопкою назад на телефоні
            navigator.replaceAll(OnboardingScreen())
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // Тимчасова іконка-лого
                Text(
                    text = "🏠",
                    fontSize = 80.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.app_title),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 32.sp
                )
            }
        }
    }
}
