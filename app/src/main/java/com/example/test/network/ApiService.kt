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

    @POST("/messages")
    fun sendMessage(@Body message: Message): Call<ApiResponse>

    @GET("/messages/{username}")
    fun getMessages(@Path("username") username: String): Call<List<Message>>
}