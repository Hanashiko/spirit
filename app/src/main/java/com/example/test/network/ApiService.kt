package com.example.test.network

import com.example.test.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/register")
    fun register(@Body user: User): Call<ApiResponse>

    @POST("/login")
    fun login(@Body credentials: Credentials): Call<LoginResponse>

    @POST("/send_message")
    fun sendMessage(@Body message: Message): Call<ApiResponse>

    @GET("/messages/{user_id}")
    fun getMessages(@Path("user_id") userId: Int): Call<List<Message>>

    @GET("/users/{id}/public_key")
    fun getPublicKey(@Path("id") userId: Int): Call<PublicKeyResponse>

    @POST("/users/{id}/update_public_key")
    fun updatePublicKey(@Path("id") userId: Int, @Body publicKey: Map<String, String>): Call<ApiResponse>

    @GET("/user_chats/{user_id}")
    fun getUserChats(@Path("user_id") userId: Int): Call<List<ChatMessage>>
}