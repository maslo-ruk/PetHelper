package com.example.pethelper.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.Constants
import com.example.pethelper.data.fireBaseEntities.Chat
import com.example.pethelper.ui.AppViewModelProvider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(viewModel: ChatListViewModel = viewModel(factory = AppViewModelProvider.Factory),
                   onOpenChat: (chatId: String) -> Unit, onBack:()-> Unit = {}) {
    val chats by viewModel.chats.collectAsState()
    val curUserId = viewModel.myUid.currentUser.collectAsState().value!!.uid

    LaunchedEffect (Unit) {
        viewModel.startObservingChats()
    }

    DisposableEffect (Unit) {
        onDispose {
            viewModel.stopObservingChats()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Чаты",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF690005),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(brush = Constants.GRADIENT_BRUSH),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(items = chats, key = { it.id }) { chat ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFF690005),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    ChatRow(chat = chat, onClick = { onOpenChat(chat.id) }, curUserId = curUserId)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

@Composable
private fun ChatRow(chat: Chat, onClick: () -> Unit, curUserId:String) {
    lateinit var x:String
    if (chat.participants[0] == curUserId) {
        x = "${chat.worker.name} ${chat.worker.surname}"
    } else {
        x = "${chat.client.name} ${chat.client.surname}"
    }
    val title = "Чат с ${x}"
    val subtitle = chat.lastMessage.ifBlank { "Нет сообщений" }
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = Color(0xFF690005))
        Spacer(modifier = Modifier.height(4.dp))
        Text(subtitle,  style = MaterialTheme.typography.bodyMedium, color = Color(0xFF690005))
    }
}
