package com.example.pethelper.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.fireBaseEntities.Chat
import com.example.pethelper.data.firebaseRepositories.ChatRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ChatListViewModel(private val repo: ChatRepository,
    private val myUid: UserSessionManager) : ViewModel() {

    val _chatsFlow = MutableStateFlow(emptyList<Chat>())
    val chats: StateFlow<List<Chat>> = _chatsFlow

    private var listener: ListenerRegistration? = null

    fun startObservingChats() {
        if (listener != null) return

        listener = repo.observeChats(
            onChange = { orders ->
                _chatsFlow.value = orders
            },
            onError = { error ->
                Log.e("OrdersViewModel", "Firestore error", error)
            },
            myUid = myUid.currentUser.value!!.uid
        )
    }

    fun stopObservingChats() {
        listener?.remove()
        listener = null
    }
}

data class ChatListUiState(
    val error: String = "",
    val isLoading:Boolean = false,
    val success:Boolean = true,
    val myName:String = "",
    val otherName:String = ""
)