package com.example.pethelper.ui.chat

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.Chat
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.data.fireBaseEntities.Message
import com.example.pethelper.data.firebaseRepositories.ChatRepository
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.example.pethelper.service.WalkServiceController
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatScreenViewModel(
    private val repo: ChatRepository,
    private val chatId: String,
    val userManager: UserSessionManager,
    val fbRepository: FireStoreRepository
): ViewModel() {

    val messages: StateFlow<List<Message>> = repo.messagesFlow(chatId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    var input by mutableStateOf("")
        private set


    val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    private var listener: ListenerRegistration? = null


    fun onInput(text: String) {
        input = text
    }

    fun send() {
        val text = input.trim()
        if (text.isEmpty()) return
        viewModelScope.launch { repo.sendMessage(chatId, userManager.currentUser.value!!.uid, text) }
        input = ""
    }

    init {
        Log.d("CHAT", "load start")
        _uiState.update { it.copy(isLoading = true, state = "Loading") }
        viewModelScope.launch {
            val ord = repo.getOrderByChatId(chatId)
            Log.d("CHAT", "ORDER VIEWMODEL ${ord!!.status}")
            val ordId = repo.getOrderIdByChatId(chatId)
            val ch = repo.getChatById(chatId)
            var partId = ch!!.participants[0]
            if (partId == userManager.currentUser.value?.uid) {
                partId = ch.participants[1]
            }
            val part = fbRepository.getUser(partId)
            _uiState.update { it.copy(chat = ch, orderId = ordId!!, participant = part) }
            _uiState.update { it.copy(isLoading = false, state="Loaded") }
            Log.d("CHAT", "viewmodel ${ch!!.orderId}")
        }
    }

    fun updateOrderStatus(status: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            var ord = _uiState.value.order
            Log.d("CHAT", "ORDER STATE before ${ord.status}")
            ord = ord.copy(status = status)
            Log.d("CHAT", "ORDER STATE ${ord.status}")
            fbRepository.updateOrder(_uiState.value.orderId, ord)
            Log.d("CHAT", "ORDER STATE after ${ord.status}")
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun addOrderWorker(workerId: String, status: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            var ord = _uiState.value.order
            ord = ord.copy(workerId = workerId, status = status)
            fbRepository.updateOrder(_uiState.value.orderId, ord)
            repo.finishChatsById(_uiState.value.orderId, userManager.currentUser.value!!.uid)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun observeOrder(orderId: String) {

        viewModelScope.launch {

            fbRepository.observeOrder(orderId)
                .collect { order ->

                    _uiState.update {
                        it.copy(order = order)
                    }
                }
        }
    }

    fun changeChatStatus() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repo.finishOrder(_uiState.value.orderId, userManager.currentUser.value!!.uid)
            _uiState.update { it.copy(isLoading = false)
            }
        }
    }

    fun startOrder(
        context: Context,
        orderId: String
    ) {
        WalkServiceController.startWalkService(context, orderId)
    }

    fun stopOrder(
        context: Context
    ) {
        WalkServiceController.stopWalkService(context)
    }
}
data class ChatUiState(
    val chat:Chat = Chat(),
    val order:FOrder = FOrder(),
    val orderId:String = "",
    val participant:FUser? = FUser(),
    val error:String = "",
    val success: Boolean = false,
    val isLoading: Boolean = true,
    val state:String = "Loading"
)