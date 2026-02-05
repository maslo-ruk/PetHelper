package com.example.pethelper.ui.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.Chat
import com.example.pethelper.data.fireBaseEntities.Message
import com.example.pethelper.data.firebaseRepositories.ChatRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatScreenViewModel(
    private val repo: ChatRepository,
    private val chatId: String,
    val userManager: UserSessionManager
): ViewModel() {
    val chat: StateFlow<Chat?> = repo.chatFlow(chatId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val messages: StateFlow<List<Message>> = repo.messagesFlow(chatId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    var input by mutableStateOf("")
        private set

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
}