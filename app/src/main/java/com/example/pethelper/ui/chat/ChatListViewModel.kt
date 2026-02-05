package com.example.pethelper.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.entities.Chat
import com.example.pethelper.data.firebaseRepositories.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ChatListViewModel(private val repo: ChatRepository,
    private val myUid: String) : ViewModel() {
    val chats: StateFlow<List<Chat>> = repo.chatsFlow(myUid).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000),
        emptyList())
}