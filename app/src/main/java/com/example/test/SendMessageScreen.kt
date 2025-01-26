package com.example.test

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageScreen(onBack: () -> Unit) {
    val senderId = remember { mutableStateOf("") }
    val receiverId = remember { mutableStateOf("") }
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
                value = receiverId.value,
                onValueChange = { receiverId.value = it },
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
                    RetrofitInstance.api.getPublicKey(receiverId.value.toInt()).enqueue(object : Callback<PublicKeyResponse> {
                        override fun onResponse(call: Call<PublicKeyResponse>, response: Response<PublicKeyResponse>) {
                            if (response.isSuccessful) {
                                val recipientPublicKeyString = response.body()?.public_key
                                if (recipientPublicKeyString != null) {
                                    val recipientPublicKey = CryptoUtils.deserializePublicKey(recipientPublicKeyString)
                                    val sharedSecret = CryptoUtils.generateSharedSecret(keyPair.private, recipientPublicKey)
                                    val encryptedMessage = CryptoUtils.encryptMessage(sharedSecret, messageText.value)

                                    sendMessageToServer(
                                        senderId = senderId.value.toInt(),
                                        receiverId = receiverId.value.toInt(),
                                        messageText = encryptedMessage,
                                        publicKey = publicKeyString
                                    )
                                }
                            } else {
                                // Handle error response
                                println("Failed to get public key: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<PublicKeyResponse>, t: Throwable) {
                            // Handle failure
                            println("Failed to get public key: ${t.message}")
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

fun sendMessageToServer(senderId: Int, receiverId: Int, messageText: String, publicKey: String) {
    val message = Message(sender_id = senderId, receiver_id = receiverId, content = messageText, public_key = publicKey)
    RetrofitInstance.api.sendMessage(message).enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            if (response.isSuccessful) {
                // Message sent successfully
                println("Message sent successfully")
            } else {
                // Handle unsuccessful response
                println("Failed to send message: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            // Handle failure
            println("Failed to send message: ${t.message}")
        }
    })
}

//fun SendMessage(senderId: Int, receiverId: Int, messageText: String, publicKey: String) {
//    val message = Message(sender_id = senderId, receiver_id = receiverId, content = messageText, public_key = publicKey)
//    RetrofitInstance.api.sendMessage(message).enqueue(object : Callback<ApiResponse> {
//        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//            if (response.isSuccessful) {
//                // Handle successful response
//                val apiResponse = response.body()
//                // Show success message
//            } else {
//                // Handle error response
//            }
//        }
//
//        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//            // Handle failure
//        }
//    })
//}

//fun sendMessage(senderId: Int, receiverId: Int, content: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
//    val message = Message(sender_id = senderId, receiver_id = receiverId, content = content)
//    RetrofitInstance.api.sendMessage(message).enqueue(object : Callback<ApiResponse> {
//        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//            if (response.isSuccessful) {
//                onSuccess()
//            } else {
//                onFailure(Exception("Failed to send message"))
//            }
//        }
//
//        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//            onFailure(t)
//        }
//    })
//}
//
//fun getMessages(userId: Int, onSuccess: (List<Message>) -> Unit, onFailure: (Throwable) -> Unit) {
//    RetrofitInstance.api.getMessages(userId).enqueue(object : Callback<List<Message>> {
//        override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
//            if (response.isSuccessful) {
//                onSuccess(response.body() ?: emptyList())
//            } else {
//                onFailure(Exception("Failed to retrieve messages"))
//            }
//        }
//
//        override fun onFailure(call: Call<List<Message>>, t: Throwable) {
//            onFailure(t)
//        }
//    })
//}