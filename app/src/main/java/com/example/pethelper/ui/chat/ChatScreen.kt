package com.example.pethelper.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.Chat
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.data.fireBaseEntities.Message
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatId: String,
               onBack: () -> Unit = {},
               checkposition:(ordId:String)->Unit = {}) {
    val viewModel: ChatScreenViewModel = viewModel(
        factory = ChatViewModelFactory(chatId)
    )
//    fun onOrderAccepted(status:String) = viewModel::updateOrderStatus
//    fun onOrderStarted() : Unit = {}
//    fun checkPosition():Unit = {}
//    fun onOrderEnded():Unit = {}
    val chat by viewModel.chat.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isClosed = chat?.status == "CLOSED"
    val curUser:FUser? = viewModel.userManager.currentUser.collectAsState().value?.user
    val myUid = viewModel.userManager.currentUser.collectAsState().value?.uid ?: ""

    Scaffold(
        topBar = {
            TopAppBar(title = {Text(text = "Чат")})}
    ) {padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) { if (isClosed) {
            Text(text = "Заказ завершен. Чат закрыт", modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge)
        }
            LazyColumn(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(items = messages, key = {it.id}) {
                    msg ->
                    MessageBubble(message = msg, isMine = (msg.senderId == myUid))
                    Spacer(Modifier.height(8.dp))
                }
            }
            ChatInput(text = viewModel.input,
                onTextChange = viewModel::onInput,
                onSend = viewModel::send,
                enabled = !isClosed,
                checkPosition = checkposition,
                viewModel = viewModel
            )

        }
    }
}

@Composable
private fun MessageBubble(message: Message, isMine: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (isMine) Arrangement.End else
    Arrangement.Start) {
        Surface(tonalElevation = 2.dp, shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(message.text, style = MaterialTheme.typography.bodyLarge)
                val timetext = message.createdAt?.toDate()?.let { dt ->
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(dt)
                }.orEmpty()
                if (timetext.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(timetext, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun ChatInput(text: String, onTextChange: (String) -> Unit,
                      onSend: () -> Unit, enabled: Boolean,
                      checkPosition:(ordId:String)->Unit,
                      order: FOrder = FOrder(),
                      chat: Chat = Chat(),
                      uid:String = "",
                      viewModel: ChatScreenViewModel
) {
    val changeStatus = viewModel::updateOrderStatus
    val addOrderWorker = viewModel::addOrderWorker
    val startOrder = viewModel::startOrder
    val stopOrder = viewModel::stopOrder
    val context = LocalContext.current
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                enabled = enabled,
                value = text,
                onValueChange = onTextChange, modifier = Modifier.weight(1f), placeholder = { Text("Сообщение...")}
            )
        Spacer(Modifier.width(8.dp))
        Button(onClick = onSend, enabled = enabled) {
            Text("Отправить")
        }
    }
    if (uid == chat.participants[0]) {
        if (order.STATUS == "PUBLISHED") {
            Button(onClick = {
                changeStatus("ACCEPTED")
                addOrderWorker(chat.participants[1])
                             }, enabled = enabled) {
                Text("Принять заявку")
            }
        }
        if (order.STATUS == "ACCEPTED") {
            Row {
                Text("Заказ Принят")
            }
        }
        if (order.STATUS == "STARTED") {
            Button(onClick = { checkPosition(order.id) }, enabled = enabled) {
                Text("Посмотреть местоположение")
            }
        }
    }
    else if (uid == chat.participants[1]) {
        if (order.STATUS == "PUBLISHED") {
            Text("Ожидание подтверждения")
        }
        if (order.STATUS == "ACCEPTED") {
            Button(onClick = {
                changeStatus("STARTED")
                startOrder(context,order.id)
                             }, enabled = enabled) {
                Text("Начать Заказ")
            }
        }
        if (order.STATUS == "STARTED") {
            Button(onClick = {
                changeStatus("ENDED")
                stopOrder(context)
                             }, enabled = enabled) {
                Text("Закончить Заказ")
            }
        }
    }
}
