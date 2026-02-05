package com.example.pethelper.ui.chat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.data.fireBaseEntities.Message
import com.example.pethelper.network.NetworkConfig
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatScreenViewModel,
               onBack: () -> Unit, myUid: String) {
    val chat by viewModel.chat.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isClosed = chat?.status == "CLOSED"

    Scaffold(
        topBar = {
            TopAppBar(title = {Text(text = "Чат")})}
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) { if (isClosed) {
            Text(text = "Заказ завершен. Чат закрыт", modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge)
        }
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth(),
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
                enabled = !isClosed)

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
                      onSend: () -> Unit, enabled: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
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
}


@Composable // функция загрузки изображения я хз, куда ее вставить так, чтобы из бд взять fileID
fun GetPhoto(currentUser: FUser?,
             baseUrl: String = NetworkConfig.BASE_URL
) {
    val fileId: String? = currentUser?.photoId
    if (fileId.isNullOrBlank()) {
        Box(
            Modifier.size(160.dp).clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        return
    }
    AsyncImage(
        model = "$baseUrl/photo/$fileId",
        contentDescription = "photo",
        modifier = Modifier.size(160.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant,CircleShape),
        contentScale = ContentScale.Crop,
    )
}
