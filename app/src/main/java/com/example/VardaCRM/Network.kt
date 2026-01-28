package com.example.VardaCRM

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("groups")
    suspend fun getGroups(): List<Group>

    @POST("groups")
    suspend fun addGroup(@Body group: Group): Group

    @DELETE("groups/{id}")
    suspend fun deleteGroup(@Path("id") groupId: Int) // Для удаления групп

    @POST("students/{groupId}")
    suspend fun addStudent(@Path("groupId") groupId: Int, @Body student: Student): Student

    @DELETE("students/{groupId}/{studentId}")
    suspend fun deleteStudent(@Path("groupId") groupId: Int, @Path("studentId") studentId: Int) // Для удаления учеников
}

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.190:8000/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}