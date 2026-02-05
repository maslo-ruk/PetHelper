package com.example.pethelper.data.firebaseRepositories

import androidx.compose.animation.core.snap
import com.example.pethelper.data.entities.Chat
import com.example.pethelper.data.entities.Message
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {
    private val db = FirebaseFirestore.getInstance()

    fun chatsFlow(myUid: String) = callbackFlow<List<Chat>> {
        val listener = db.collection("chats").whereArrayContains("participants", myUid)
            .whereEqualTo("status", "ACTIVE").orderBy("lastMessageAt",
                Query.Direction.DESCENDING).addSnapshotListener { snap, _ ->
                val chats = snap?.documents?.mapNotNull {d ->
                    d.toObject(Chat::class.java)!!.copy(id = d.id)
                } ?: emptyList()
            trySend(chats)}
        awaitClose { listener.remove() }
    }

    fun messagesFlow(chatid:String) = callbackFlow<List<Message>> {
        val listener = db.collection("messages").whereEqualTo("chatId", chatid)
            .orderBy("createdAt", Query.Direction.ASCENDING).addSnapshotListener { snap, _ ->
                val msgs = snap?.documents?.mapNotNull { d ->
                    d.toObject(Message::class.java)?.copy(id = d.id)
                } ?: emptyList()
                trySend(msgs)
            }
        awaitClose { listener.remove() }
    }
    suspend fun sendMessage(chatId: String, senderId: String, text: String) {
        val msgData = mapOf("chatId" to chatId,
            "senderId" to senderId,
            "text" to text,
            "createdAt" to FieldValue.serverTimestamp())
        db.collection("messages").add(msgData)
        db.collection("chats").document(chatId).update("lastMessage", text)
    }

    suspend fun createChat(orderId: String, petyaId: String, vasyaId: String): String {
        val chatRef = db.collection("chats").document()
        val data = mapOf("orderId" to orderId,
            "participants" to listOf(petyaId, vasyaId),
            "status" to "ACTIVE",
            "lastMessage" to "",
            "createdAt" to FieldValue.serverTimestamp(),
            "lastMessageAt" to FieldValue.serverTimestamp())
        chatRef.set(data).await()
        return chatRef.id
    }
    suspend fun finishOrder(orderId: String, petyaId: String) {
        val chats = db.collection("chats").whereEqualTo("orderId", orderId).get().await()
        val batch = db.batch()
        chats.documents.forEach {
            batch.update(it.reference, "status", "CLOSED")
        }
        batch.commit().await()
    }
}

