package com.nazar_protasov.swipehome.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nazar_protasov.swipehome.ui.screens.chat.ChatMessage
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_arrow_next
import mymultiplatformproject.shared.generated.resources.ic_arrow_single_down
import mymultiplatformproject.shared.generated.resources.ic_close
import mymultiplatformproject.shared.generated.resources.ic_list
import mymultiplatformproject.shared.generated.resources.ic_support
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(){
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = { /*TODO*/ },
        actions = {
            IconButton(onClick = {/*TODO:*/}){
                Icon(painterResource(Res.drawable.ic_support), contentDescription = "Call")
            }
            IconButton(onClick = {/*TODO:*/}){
                Icon(painterResource(Res.drawable.ic_list), contentDescription = "More")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SwipeHomeTheme.colors.background,
            titleContentColor = SwipeHomeTheme.colors.onBackground,
            navigationIconContentColor = SwipeHomeTheme.colors.onBackground,
            actionIconContentColor = SwipeHomeTheme.colors.onBackground
        )
    )
}

@Composable
fun ChatMessageList(modifier: Modifier){

    // Мокові Текстові повідомлення
    val messages = listOf(
        ChatMessage("1", "Добрі дєнь! Підкажіть, чи ще актіальна квартира біля гавнокачки?", "10:46", isOut = false),
        ChatMessage("2", "Добрі дєнь! Так, актіальна. Ьщжемо дараваріться про обзор на сьогодні", "10:50", isOut = true, replyTo = "Добрий! Підкажіть, чи ще актіальна...."),
        ChatMessage("3", "Атлічно, а там кіт тоже буде? Приїду чекнуть раді кота.", "10:55", isOut = false, hasImage = true)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ){
        item {
            // Дата по центру
            Box(
                modifier = modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Surface(
                    shape = SwipeHomeTheme.shapes.mediumShape,
                    color = SwipeHomeTheme.colors.background
                ){
                    Text(
                        text = "Сьогодні, 10:45",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = SwipeHomeTheme.typography.caption.fontSize,
                    )
                }
            }
        }

        items(messages){ message ->
            ChatBubble(message = message)
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage){
    val bubbleShape = if (message.isOut) {
        SwipeHomeTheme.shapes.bubbleOut
    } else {
        SwipeHomeTheme.shapes.bubbleIn
    }

    val bubbleColor = if (message.isOut) {
        SwipeHomeTheme.colors.primary
    } else {
        SwipeHomeTheme.colors.surface
    }

    val textColor = if (message.isOut) {
        SwipeHomeTheme.colors.onPrimary
    } else {
        SwipeHomeTheme.colors.onSurface
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isOut) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ){
        if(!message.isOut){
            // Аватарка зліва для вхідних повідомлень
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(SwipeHomeTheme.colors.background),
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = bubbleShape,
            color = bubbleColor,
            modifier = Modifier.widthIn(max = 280.dp)
        ){
            Column(modifier = Modifier.padding(12.dp)) {

                // Блок цитування
                if(message.replyTo != null){
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(SwipeHomeTheme.colors.background)
                            .padding(start = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ){
                            Text("Саньок Коваленко", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral)
                            Text(message.replyTo, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.neutral, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }

                // Зображення в повідомленні
                if(message.hasImage){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(bottom = 8.dp)
                            .clip(SwipeHomeTheme.shapes.mediumShape)
                            .background(SwipeHomeTheme.colors.background)
                    )
                }
                Text(
                    text = message.text,
                    style = SwipeHomeTheme.typography.body,
                    color = textColor
                )

                Row(
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.time,
                        style = SwipeHomeTheme.typography.caption,
                        color = textColor.copy(alpha = 0.7f)
                    )
                    if(message.isOut){
                       Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_single_down),// Тут можна використати кастомну іконку подвійної галочки
                            contentDescription = "Read",
                            tint = SwipeHomeTheme.colors.primary.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatInputBar() {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SwipeHomeTheme.colors.background)
    ){
        // Панель "Відповідь над полем вводу
        Surface(
            color = SwipeHomeTheme.colors.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Reply",
                    tint = SwipeHomeTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Відповідь Саньок", color = SwipeHomeTheme.colors.primary, style = SwipeHomeTheme.typography.label)
                    Text("Отлічно, а цей кіт...", color = SwipeHomeTheme.colors.surface, style = SwipeHomeTheme.typography.label, maxLines = 1)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painterResource(Res.drawable.ic_close), contentDescription = "Close reply", tint = SwipeHomeTheme.colors.surface)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {/*TODO:*/},
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SwipeHomeTheme.colors.background)
            ){
                Icon(painterResource(Res.drawable.ic_list), contentDescription = "Attach", tint = SwipeHomeTheme.colors.primary)
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Напишіть повідомлення...", style = SwipeHomeTheme.typography.label) },
                modifier = Modifier.weight(1f).clip(SwipeHomeTheme.shapes.largeShape),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = SwipeHomeTheme.colors.background,
                    unfocusedIndicatorColor = SwipeHomeTheme.colors.background,
                    disabledIndicatorColor = SwipeHomeTheme.colors.background,
                    unfocusedContainerColor = SwipeHomeTheme.colors.background,
                    focusedContainerColor = SwipeHomeTheme.colors.background,
                ),
                trailingIcon ={
                    Icon(painterResource(Res.drawable.ic_arrow_single_down), contentDescription = "Emoji", tint = SwipeHomeTheme.colors.primary)
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {/*TODO:*/},
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SwipeHomeTheme.colors.primary)
            ){
                Icon(painterResource(Res.drawable.ic_arrow_next), contentDescription = "Send", tint = SwipeHomeTheme.colors.primary)
            }
        }
    }
}