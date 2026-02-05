package com.example.pethelper.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pethelper.data.session.AppSession

class ChatViewModelFactory(
    private val chatId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatScreenViewModel(repo = AppSession.chatRepository, chatId = chatId, userManager = AppSession.sessionManager) as T
    }
}