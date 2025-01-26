package com.example.test

import android.util.Log
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.test.model.ApiResponse
import com.example.test.model.Message
import com.example.test.model.PublicKeyResponse
import com.example.test.network.RetrofitInstance
import com.example.test.CryptoUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.InvalidKeyException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageScreen(onBack: () -> Unit) {
    val senderId = remember { mutableStateOf("") }
    val recipientId = remember { mutableStateOf("") }
    val messageText = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Надіслати повідомлення",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = senderId.value,
                onValueChange = { senderId.value = it },
                label = { Text("ID відправника") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = recipientId.value,
                onValueChange = { recipientId.value = it },
                label = { Text("ID отримувача") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = messageText.value,
                onValueChange = { messageText.value = it },
                label = { Text("Текст повідомлення") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val keyPair = CryptoUtils.generateECKeyPair()
                    val publicKeyString = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)

                    // Отримання публічного ключа отримувача з сервера
                    RetrofitInstance.api.getPublicKey(recipientId.value.toInt()).enqueue(object : Callback<PublicKeyResponse> {
                        override fun onResponse(call: Call<PublicKeyResponse>, response: Response<PublicKeyResponse>) {
                            if (response.isSuccessful) {
                                val recipientPublicKeyString = response.body()?.public_key
                                if (recipientPublicKeyString != null) {
                                    try {
                                        Log.d("SendMessageScreen", "Public key received: $recipientPublicKeyString")
                                        val recipientPublicKey = CryptoUtils.deserializePublicKey(recipientPublicKeyString)
                                        val sharedSecret = CryptoUtils.generateSharedSecret(keyPair.private, recipientPublicKey)
                                        val encryptedMessage = CryptoUtils.encryptMessage(sharedSecret, messageText.value)

                                        Log.d("SendMessageScreen", "Sending message: $encryptedMessage")
                                        sendMessageToServer(
                                            senderId = senderId.value.toInt(),
                                            recipientId = recipientId.value.toInt(),
                                            messageText = encryptedMessage,
                                            publicKey = publicKeyString
                                        )
                                    } catch (e: IllegalArgumentException) {
                                        // Handle invalid Base64 encoding
                                        Log.e("SendMessageScreen", "Invalid Base64 encoding: ${e.message}")
                                    } catch (e: InvalidKeyException) {
                                        // Handle invalid key agreement
                                        Log.e("SendMessageScreen", "Invalid Key Agreement: ${e.message}")
                                    }
                                } else {
                                    Log.e("SendMessageScreen", "Public key is null")
                                }
                            } else {
                                // Handle error response
                                Log.e("SendMessageScreen", "Failed to get public key: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<PublicKeyResponse>, t: Throwable) {
                            // Handle failure
                            Log.e("SendMessageScreen", "Failed to get public key: ${t.message}")
                        }
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCB45))
            ) {
                Text(text = "Надіслати", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

fun sendMessageToServer(senderId: Int, recipientId: Int, messageText: String, publicKey: String) {
    val message = Message(sender_id = senderId, recipient_id = recipientId, content = messageText, public_key = publicKey)
    RetrofitInstance.api.sendMessage(message).enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            if (response.isSuccessful) {
                // Message sent successfully
                Log.d("SendMessageScreen", "Message sent successfully")
            } else {
                // Handle unsuccessful response
                Log.e("SendMessageScreen", "Failed to send message: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            // Handle failure
            Log.e("SendMessageScreen", "Failed to send message: ${t.message}")
        }
    })
}