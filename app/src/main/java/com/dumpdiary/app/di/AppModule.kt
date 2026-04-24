package com.dumpdiary.app.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.dumpdiary.app.BuildConfig
import com.dumpdiary.app.data.local.AppDatabase
import com.dumpdiary.app.data.local.LogDao
import com.dumpdiary.app.data.local.ProfileDao
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.remote.DumpDiaryApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "dump-diary.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideProfileDao(database: AppDatabase): ProfileDao = database.profileDao()

    @Provides
    fun provideLogDao(database: AppDatabase): LogDao = database.logDao()

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun provideOkHttp(preferencesRepository: UserPreferencesRepository): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val prefs = runBlocking { preferencesRepository.preferences.first() }
                val request = chain.request().newBuilder().apply {
                    if (prefs.accessToken.isNotBlank()) {
                        header("Authorization", "Bearer ${prefs.accessToken}")
                    }
                    if (prefs.refreshToken.isNotBlank()) {
                        header("X-Refresh-Token", prefs.refreshToken)
                    }
                }.build()
                chain.proceed(request)
            }
            .authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    if (responseCount(response) >= 2) return null
                    val prefs = runBlocking { preferencesRepository.preferences.first() }
                    if (prefs.refreshToken.isBlank()) return null
                    val client = OkHttpClient()
                    val payload = JSONObject().put("refreshToken", prefs.refreshToken).toString()
                    val body = payload.toRequestBody("application/json".toMediaType())
                    val refreshRequest = Request.Builder()
                        .url("${BuildConfig.API_BASE_URL}auth/refresh")
                        .post(body)
                        .build()
                    val refreshResponse = client.newCall(refreshRequest).execute()
                    if (!refreshResponse.isSuccessful) return null
                    val raw = refreshResponse.body?.string() ?: return null
                    val json = JSONObject(raw)
                    val newAccessToken = json.optString("accessToken")
                    val newRefreshToken = json.optString("refreshToken", prefs.refreshToken)
                    val newUserId = json.optString("userId", prefs.userId)
                    val newEmail = json.optString("email", prefs.email)
                    if (newAccessToken.isBlank()) return null
                    runBlocking {
                        preferencesRepository.updateSession(
                            accessToken = newAccessToken,
                            refreshToken = newRefreshToken,
                            userId = newUserId,
                            email = newEmail,
                        )
                    }
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                }
            })
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): DumpDiaryApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(DumpDiaryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)

    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }
}
