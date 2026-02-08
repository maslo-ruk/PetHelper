package com.example.pethelper.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.Chat
import androidx.compose.foundation.background
import com.example.pethelper.ui.AppViewModelProvider
import com.example.pethelper.Constants
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(viewModel: ChatListViewModel = viewModel(factory = AppViewModelProvider.Factory),
                   onOpenChat: (chatId: String) -> Unit, onBack:()-> Unit = {}) {
    val chats by viewModel.chats.collectAsState()

    LaunchedEffect (Unit) {
        viewModel.startObservingChats()
    }

    DisposableEffect (Unit) {
        onDispose {
            viewModel.stopObservingChats()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Constants.GRADIENT_BRUSH)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Назад",
                    tint = Color(0xFF690005)
                )
            }
            Text(
                "Чаты",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF690005),
                textAlign = TextAlign.Center
            )
        }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = chats, key = { it.id }) { chat ->
                    ChatRow(chat = chat, onClick = { onOpenChat(chat.id) })
                    Divider()
                }
            }
        }
    }

@Composable
private fun ChatRow(chat: Chat, onClick: () -> Unit) {
    val title = "Заказ ${chat.orderId}"
    val subtitle = chat.lastMessage.ifBlank { "Нет сообщений" }
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(subtitle,  style = MaterialTheme.typography.bodyMedium)
    }
}
