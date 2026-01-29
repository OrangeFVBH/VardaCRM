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

    @DELETE("groups/{group_id}") // ИСПРАВЛЕНИЕ: group_id вместо id
    suspend fun deleteGroup(@Path("group_id") groupId: Int)

    @POST("students/{group_id}")
    suspend fun addStudent(@Path("group_id") groupId: Int, @Body student: Student): Student

    @PUT("students/{group_id}/{student_id}") // НОВОЕ: для сохранения даты
    suspend fun updateStudent(
        @Path("group_id") groupId: Int,
        @Path("student_id") studentId: Int,
        @Body student: Student
    ): Student

    @DELETE("students/{group_id}/{student_id}")
    suspend fun deleteStudent(@Path("group_id") groupId: Int, @Path("student_id") studentId: Int)
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