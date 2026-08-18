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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import org.jetbrains.compose.resources.painterResource
import kotlin.repeat

class RegisterScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Головний стан майстра реєстрації
        var currentStep by remember { mutableStateOf(1) }

        // Стани для збереження даних користувача (будуть заповнюватися на різних кроках)
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 24.dp)
        ){
            //--------Верхня частина екран--------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        // Якщо ми далі першого кроку - повертаємось на крок назад, інакше виходимо з екрана
                        if (currentStep > 1) currentStep-- else navigator.pop()
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "Swipe Home",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Порожній блок для балансу заголовка по центру
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            //--------Індикатор прогресу--------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4){index ->
                    val stepNumber = index + 1
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(4.dp)
                            .width(32.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (stepNumber <= currentStep) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                            )
                    )
                }
            }
            Text(
                text = "Крок $currentStep з 4",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --------Вміст поточного кроку--------
            when (currentStep){
                1 -> Step1Content(email = email, onEmailChange = { email = it })
                2 -> {/* TODO: Крок 2 (Безпека)*/}
                3 -> {/* TODO: Крок 3 (Про себе)*/}
                4 -> {/* TODO: Крок 4 (Контакти)*/}
            }

            Spacer(modifier = Modifier.weight(1f)) // Виштовхує кнопки вниз

            // --------Нижні кнопки--------
            if (currentStep == 1) {
                Button(
                    onClick = { currentStep = 2 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Далі", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painterResource(Res.drawable.ic_arrow_next),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Посилання на політику використання
                Column (
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Натискання продовжити, ви погоджуєтесь з",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    TextButton(onClick = { /*TODO: Посилання на політику використання*/ })
                    {
                        Text(
                            text = "Умовами використання",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            } else {
                // Кнопки для кроків 2, 3, 4 (Далі + Назад)
                Button(
                    onClick = {
                        if (currentStep < 4) currentStep++ else {/*TODO: Фінальна реєстація*/}
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ){
                    Text(if (currentStep == 4) "Зареєструватися" else "Далі", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    if (currentStep != 4){
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(painterResource(Res.drawable.ic_arrow_next), contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { currentStep-- },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ){
                    Text("Назад", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    //  Окрема функція для верстки вмісту Першого кроку
    @Composable
    fun Step1Content(email: String, onEmailChange: (String) -> Unit) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Створити акаунт",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Швидкий старт для пошуку ідеального дому",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопки соц. мереж
            OutlinedButton(
                onClick = {/*TODO*/},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ){
                Text("Google", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Продовжити з Google", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {/*TODO*/},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ){
                Text("Apple", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Продовжити з Apple ID", fontSize = 16.sp, color = MaterialTheme.colorScheme.background)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Розділювач "АБО"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)))
                Text(
                    text = "АБО",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Email адреса",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}