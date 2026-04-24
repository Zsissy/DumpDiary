package com.dumpdiary.app.data.remote

import com.dumpdiary.app.data.model.AuthRequestDto
import com.dumpdiary.app.data.model.AppVersionDto
import com.dumpdiary.app.data.model.AvatarResponseDto
import com.dumpdiary.app.data.model.BowelLogDto
import com.dumpdiary.app.data.model.AddFriendRequestDto
import com.dumpdiary.app.data.model.FriendProfileDto
import com.dumpdiary.app.data.model.MessageDto
import com.dumpdiary.app.data.model.MonthlySummaryDto
import com.dumpdiary.app.data.model.RefreshRequestDto
import com.dumpdiary.app.data.model.RegisterRequestDto
import com.dumpdiary.app.data.model.ResetPasswordRequestDto
import com.dumpdiary.app.data.model.SendEmailCodeRequestDto
import com.dumpdiary.app.data.model.SessionDto
import com.dumpdiary.app.data.model.StreakSummaryDto
import com.dumpdiary.app.data.model.UpdateProfileRequestDto
import com.dumpdiary.app.data.model.UserProfileDto
import com.dumpdiary.app.data.model.VerifyCodeDto
import com.dumpdiary.app.data.model.VerifyEmailCodeRequestDto
import com.dumpdiary.app.data.model.YearlyTrendPointDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface DumpDiaryApi {
    @GET("app/version")
    suspend fun getLatestAppVersion(): AppVersionDto

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequestDto): SessionDto

    @POST("auth/login")
    suspend fun login(@Body body: AuthRequestDto): SessionDto

    @POST("auth/send-email-code")
    suspend fun sendEmailCode(@Body body: SendEmailCodeRequestDto): MessageDto

    @POST("auth/verify-email-code")
    suspend fun verifyEmailCode(@Body body: VerifyEmailCodeRequestDto): VerifyCodeDto

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequestDto): MessageDto

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequestDto): SessionDto

    @POST("auth/logout")
    suspend fun logout(): MessageDto

    @GET("me/profile")
    suspend fun getProfile(): UserProfileDto

    @PUT("me/profile")
    suspend fun updateProfile(@Body body: UpdateProfileRequestDto): UserProfileDto

    @Multipart
    @POST("me/avatar")
    suspend fun uploadAvatar(@Part avatar: MultipartBody.Part): AvatarResponseDto

    @GET("friends")
    suspend fun getFriends(): List<FriendProfileDto>

    @POST("friends")
    suspend fun addFriend(@Body body: AddFriendRequestDto): MessageDto

    @GET("logs")
    suspend fun getLogs(): List<BowelLogDto>

    @POST("logs")
    suspend fun createLog(@Body body: BowelLogDto): BowelLogDto

    @PUT("logs/{id}")
    suspend fun updateLog(@Path("id") id: String, @Body body: BowelLogDto): BowelLogDto

    @DELETE("logs/{id}")
    suspend fun deleteLog(@Path("id") id: String): MessageDto

    @GET("stats/monthly")
    suspend fun getMonthlySummary(@Query("month") month: String): MonthlySummaryDto

    @GET("stats/streak")
    suspend fun getStreakSummary(): StreakSummaryDto

    @GET("stats/yearly")
    suspend fun getYearlyTrend(@Query("year") year: Int): List<YearlyTrendPointDto>
}
