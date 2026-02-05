package com.example.pethelper.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.entities.Message
import com.example.pethelper.data.firebaseRepositories.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatScreenViewModel(
    private val repo: ChatRepository,
    private val chatId: String,
    private val myUid: String
): ViewModel() {
    val messages: StateFlow<List<Message>> = repo.messagesFlow(chatId).stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5_000), emptyList())
    var input by mutableStateOf("")
        private set

    fun onInput(text: String) {
        input = text
    }

    fun send() {
        val text = input.trim()
        if (text.isEmpty()) return
        viewModelScope.launch { repo.sendMessage(chatId, myUid, text) }
        input = ""
    }
}