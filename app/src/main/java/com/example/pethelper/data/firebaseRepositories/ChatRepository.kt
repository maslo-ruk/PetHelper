package com.example.pethelper.data.firebaseRepositories

import android.util.Log
import com.example.pethelper.data.enums.OrderStatus
import com.example.pethelper.data.fireBaseEntities.Chat
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.data.fireBaseEntities.Message
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {
    private val db = FirebaseFirestore.getInstance()

    fun chatsFlowListen(myUid: String) = callbackFlow<List<Chat>> {
        val listener = db.collection("chats").whereArrayContains("participants", myUid)
            .whereEqualTo("status", "ACTIVE").orderBy("lastMessageAt",
                Query.Direction.DESCENDING).addSnapshotListener { snap, e ->
                    if (e != null) {trySend(emptyList())
                        return@addSnapshotListener}
                val chats = snap?.documents?.mapNotNull { d ->
                    d.toObject(Chat::class.java)
                        ?.copy(id = d.id)
                } ?: emptyList()

                trySend(chats)}
        awaitClose { listener.remove() }
    }

    fun observeChats(myUid: String, onError: (Throwable) -> Unit,
                     onChange: (List<Chat>) -> Unit): ListenerRegistration {
        val chatsRef = db.collection("chats").whereArrayContains("participants", myUid)
            .whereEqualTo("status", "ACTIVE")

        return chatsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener
            val chats = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Chat::class.java)?.copy(id = doc.id)
            }
            onChange(chats)
        }
    }

    fun chatFlow(chatId: String) = callbackFlow<Chat?> {
        val listener = db.collection("chats").document(chatId)
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                val chat = snap?.toObject(Chat::class.java)?.copy(id = snap.id)
                trySend(chat)
            }
        awaitClose { listener.remove() }
    }

    fun messagesFlow(chatid:String) = callbackFlow<List<Message>> {
        val listener = db.collection("messages").whereEqualTo("chatId", chatid)
            .orderBy("createdAt", Query.Direction.ASCENDING).addSnapshotListener { snap, e ->
                if (e != null) {trySend(emptyList())
                return@addSnapshotListener}
                val msgs = snap?.documents?.mapNotNull { d ->
                    d.toObject(Message::class.java)?.copy(id = d.id)
                } ?: emptyList()
                trySend(msgs)
            }
        awaitClose { listener.remove() }
    }

    fun observeMessages(chatid: String, onError: (Throwable) -> Unit,
                     onChange: (List<Message>) -> Unit): ListenerRegistration {
        val messagesRef = db.collection("messages").whereEqualTo("chatId", chatid)
            .orderBy("createdAt", Query.Direction.ASCENDING)

        return messagesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener
            val messages = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Message::class.java)?.copy(id = doc.id)
            }
            Log.d("ChatRepository", "Received ${messages.size} messages")
            onChange(messages)
        }
    }
    suspend fun sendMessage(chatId: String, senderId: String, text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        val chatRef = db.collection("chats").document(chatId)
        val chatSnap = chatRef.get().await()
        val chatStatus = chatSnap.getString("status") ?: "ACTIVE"
        if (chatStatus != "ACTIVE") return
        val msgData = mapOf("chatId" to chatId,
            "senderId" to senderId,
            "text" to trimmed,
            "createdAt" to FieldValue.serverTimestamp(),)
        val chatUpdates = mapOf("lastMessage" to trimmed,
            "lastMessageAt" to FieldValue.serverTimestamp())
        db.collection("messages").add(msgData).await()
        chatRef.update(chatUpdates).await()
    }

    suspend fun createChat(orderId: String, clientId: String, workerId: String): String {
        val chatRef = db.collection("chats").document()
        val data = mapOf("orderId" to orderId,
            "participants" to listOf(clientId, workerId),
            "status" to "ACTIVE",
            "lastMessage" to "",
            "createdAt" to FieldValue.serverTimestamp(),
            "lastMessageAt" to FieldValue.serverTimestamp())
        chatRef.set(data).await()
        return chatRef.id
    }
    suspend fun finishOrder(orderId: String, petyaId: String) {
        val chatSnap = db.collection("chats").whereEqualTo("orderId", orderId)
            .whereEqualTo("status", "ACTIVE").get().await()
        val batch = db.batch()
        chatSnap.documents.forEach { doc ->
            batch.update(doc.reference, mapOf("status" to "CLOSED",
                "lastMessage" to "Заказ Завершен",
                "lastMessageAt" to FieldValue.serverTimestamp()))
        }
        val orderRef = db.collection("orders").document(orderId)
        batch.update(orderRef, mapOf("STATUS" to OrderStatus.CLOSED.toString(),
        "closedAT" to FieldValue.serverTimestamp()))
        batch.commit().await()
    }

    suspend fun getOrderIdByChatId(chatId: String): FOrder? {
        val chatSnap = db.collection("chats").document(chatId).get().await()
        return chatSnap.toObject(FOrder::class.java)
    }

}

