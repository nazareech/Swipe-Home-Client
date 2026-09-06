package com.nazar_protasov.swipehome.ui.screens.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.nazar_protasov.swipehome.ui.screens.chat.components.ChatInputBar
import com.nazar_protasov.swipehome.ui.screens.chat.components.ChatMessageList
import com.nazar_protasov.swipehome.ui.screens.chat.components.ChatTopBar
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme

data class ChatMessage(
    val id: String,
    val text: String,
    val time: String,
    val isOut: Boolean,
    val hasImage: Boolean = false,
    val replyTo: String? = null
)

class ChatScreen : Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = { ChatTopBar() },
            bottomBar = { ChatInputBar() },
            containerColor = SwipeHomeTheme.colors.background
        ) { paddingValues ->
            ChatMessageList(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
