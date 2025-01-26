package com.example.test.model

data class User(
    val id: Int? = null,
    val username: String,
    val email: String,
    val password: String? = null,
    val full_name: String? = null,
    val bio: String? = null,
    val phone_number: String? = null,
    val image: UserImage? = null
)
object UserSession {
    var currentUser: User? = null
}
data class UserImage(
    val id: Int? = null,
    val user_id: Int,
    val image_path: String
)

//data class Message(
//    val id: Int? = null,
//    val sender_id: Int,
//    val receiver_id: Int,
//    val content: String,
//    val public_key: String,
//    val timestamp: String? = null
//)
data class Message(
    val sender_id: Int,
    val recipient_id: Int,
    val content: String,
    val public_key: String
)
//data class Message(
//    val sender_id: Int,
//    val receiver_id: Int,
//    val content: String,
//    val timestamp: String? = null // Optional since it might not be sent from the client
//)

data class ApiResponse(
    val message: String
)

//data class LoginResponse(
//    val token: String
//)

data class Credentials(
    val email: String,
    val password: String
)
data class LoginResponse(
    val success: Boolean,
    val userId: Int
)
data class PublicKeyResponse(
    val public_key: String
)
data class UserChat(
    val userId: Int,
    val username: String,
    val avatarImage: String?
)
data class ChatMessage(
    val senderName: String,
    val message: String,
    val avatarRes: String,
    val time: String,
    val senderPublicKey: String
)
