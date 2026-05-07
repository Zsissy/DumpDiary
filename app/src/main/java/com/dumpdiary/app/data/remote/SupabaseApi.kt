package com.dumpdiary.app.data.remote

import com.dumpdiary.app.data.model.SupabaseRoom
import com.dumpdiary.app.data.model.SupabaseUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApi {
    @GET("app_users")
    suspend fun getAllUsers(
        @Query("select") select: String = "*",
        @Query("order") order: String = "created_at.desc",
    ): List<SupabaseUser>

    @GET("app_users")
    suspend fun getUserByUsername(
        @Query("select") select: String = "*",
        @Query("username") username: String,
    ): List<SupabaseUser>

    @POST("app_users")
    suspend fun insertUser(@Body user: SupabaseUser)

    @PATCH("app_users")
    suspend fun updateUserById(
        @Query("id") id: String,
        @Body user: SupabaseUser,
    )

    @GET("app_sync_rooms")
    suspend fun getRoom(
        @Query("select") select: String = "*",
        @Query("room_code") roomCode: String,
    ): List<SupabaseRoom>

    @POST("app_sync_rooms")
    suspend fun upsertRoom(@Body room: SupabaseRoom)
}
