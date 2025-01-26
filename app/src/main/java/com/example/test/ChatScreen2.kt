package com.example.test

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.model.ChatMessage
import com.example.test.network.RetrofitInstance
import com.example.test.CryptoUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.PrivateKey
import java.util.Base64

@Composable
fun ChatScreen2(context: Context, userId: Int, onChatClick: (ChatMessage) -> Unit) {
    var isLoading by remember { mutableStateOf(true) }
    var chatMessages by remember { mutableStateOf(listOf<ChatMessage>()) }

    LaunchedEffect(Unit) {
        val privateKey = CryptoUtils.getPrivateKey(context)
        if (privateKey != null) {
            RetrofitInstance.api.getUserChats(userId).enqueue(object : Callback<List<ChatMessage>> {
                override fun onResponse(call: Call<List<ChatMessage>>, response: Response<List<ChatMessage>>) {
                    if (response.isSuccessful) {
                        chatMessages = response.body()?.map { chatMessage ->
                            val senderPublicKey = CryptoUtils.deserializePublicKey(chatMessage.senderPublicKey)
                            val sharedSecret = CryptoUtils.generateSharedSecret(privateKey, senderPublicKey)
                            Log.d("ChatScreen2", "Shared secret: ${Base64.getEncoder().encodeToString(sharedSecret)}")
                            try {
                                val decryptedMessage = CryptoUtils.decryptMessage(sharedSecret, chatMessage.message)
                                chatMessage.copy(
                                    message = decryptedMessage,
                                    avatarRes = mapAvatarRes(chatMessage.avatarRes).toString()
                                )
                            } catch (e: Exception) {
                                Log.e("ChatScreen2", "Decryption failed", e)
                                chatMessage
                            }
                        } ?: emptyList()
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<List<ChatMessage>>, t: Throwable) {
                    isLoading = false
                }
            })
        } else {
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Всі чати",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                items(chatMessages) { message ->
                    ChatRow(chatMessage = message, onClick = { onChatClick(message) })
                }
            }
        }
    }
}

@Composable
fun ChatRow(chatMessage: ChatMessage, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = mapAvatarRes(chatMessage.avatarRes)),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chatMessage.senderName,
                style = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = chatMessage.message,
                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chatMessage.time,
                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
            )
            Icon(
                painter = painterResource(id = R.drawable.check),
                contentDescription = "Message status",
                tint = Color.Gray,
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 4.dp)
            )
        }
    }
}

// Helper function to map avatarRes strings to drawable resource IDs
fun mapAvatarRes(avatarRes: String): Int {
    return when (avatarRes) {
        "default_avatar_image_path" -> R.drawable.avatar5
        else -> R.drawable.avatar5 // Default fallback
    }
}