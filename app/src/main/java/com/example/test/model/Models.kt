package com.example.test.model

data class User(
    val id: Int? = null,
    val username: String,
    val email: String,
    val hashed_password: String? = null,
    val full_name: String? = null,
    val bio: String? = null,
    val phone_number: String? = null,
    val image: UserImage? = null
)

data class UserImage(
    val id: Int? = null,
    val user_id: Int,
    val image_path: String
)

data class Message(
    val id: Int? = null,
    val sender_id: Int,
    val receiver_id: Int,
    val content: String,
    val timestamp: String? = null
)

data class ApiResponse(
    val message: String
)

data class LoginResponse(
    val token: String
)

data class Credentials(
    val username: String,
    val password: String
)