package com.dumpdiary.app.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.dumpdiary.app.data.local.AppDatabase
import com.dumpdiary.app.data.local.LogDao
import com.dumpdiary.app.data.local.ProfileDao
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.remote.DumpDiaryApi
import com.dumpdiary.app.data.remote.SupabaseApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit

private val placeholderBaseUrl: HttpUrl = "https://placeholder.invalid/".toHttpUrl()

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
    @Named("rest")
    @Singleton
    fun provideOkHttp(preferencesRepository: UserPreferencesRepository): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val prefs = runBlocking { preferencesRepository.preferences.first() }
                val originalRequest = chain.request()
                val requestUrl = originalRequest.url
                val rewrittenUrl = if (requestUrl.host == placeholderBaseUrl.host) {
                    val baseUrl = prefs.serverBaseUrl.ifBlank { throw java.io.IOException("Please configure the server address first.") }
                    resolveRuntimeUrl(baseUrl, requestUrl)
                } else {
                    requestUrl
                }
                val request = originalRequest.newBuilder().url(rewrittenUrl).apply {
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
                    if (prefs.refreshToken.isBlank() || prefs.serverBaseUrl.isBlank()) return null
                    val client = OkHttpClient()
                    val payload = JSONObject().put("refreshToken", prefs.refreshToken).toString()
                    val body = payload.toRequestBody("application/json".toMediaType())
                    val refreshRequest = Request.Builder()
                        .url("${prefs.serverBaseUrl}auth/refresh")
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
    fun provideApi(@Named("rest") okHttpClient: OkHttpClient): DumpDiaryApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(placeholderBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(DumpDiaryApi::class.java)
    }

    @Provides
    @Named("supabase")
    @Singleton
    fun provideSupabaseOkHttp(preferencesRepository: UserPreferencesRepository): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val prefs = runBlocking { preferencesRepository.preferences.first() }
                val anonKey = prefs.supabaseAnonKey.ifBlank {
                    throw java.io.IOException("Please configure the Supabase anon key first.")
                }
                val serverUrl = prefs.serverBaseUrl.ifBlank {
                    throw java.io.IOException("Please configure the server address first.")
                }
                val originalRequest = chain.request()
                val requestUrl = originalRequest.url
                val rewrittenUrl = if (requestUrl.host == placeholderBaseUrl.host) {
                    resolveSupabaseUrl(serverUrl, requestUrl)
                } else {
                    requestUrl
                }
                val request = originalRequest.newBuilder()
                    .url(rewrittenUrl)
                    .header("apikey", anonKey)
                    .header("Authorization", "Bearer $anonKey")
                    .header("Prefer", "return=representation")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideSupabaseApi(@Named("supabase") supabaseOkHttpClient: OkHttpClient): SupabaseApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(placeholderBaseUrl)
            .client(supabaseOkHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(SupabaseApi::class.java)
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

    private fun resolveRuntimeUrl(baseUrl: String, originalUrl: HttpUrl): HttpUrl {
        val runtimeBase = baseUrl.toHttpUrl()
        return runtimeBase.newBuilder().apply {
            val relativePath = originalUrl.encodedPath.removePrefix("/")
            if (relativePath.isNotBlank()) {
                addEncodedPathSegments(relativePath)
            }
            originalUrl.queryParameterNames.forEach { name ->
                originalUrl.queryParameterValues(name).forEach { value ->
                    addQueryParameter(name, value)
                }
            }
        }.build()
    }

    private fun resolveSupabaseUrl(baseUrl: String, originalUrl: HttpUrl): HttpUrl {
        val normalized = baseUrl.trimEnd('/')
        val runtimeBase = "$normalized/rest/v1/".toHttpUrl()
        return runtimeBase.newBuilder().apply {
            val relativePath = originalUrl.encodedPath.removePrefix("/")
            if (relativePath.isNotBlank()) {
                addEncodedPathSegments(relativePath)
            }
            originalUrl.queryParameterNames.forEach { name ->
                originalUrl.queryParameterValues(name).forEach { value ->
                    addEncodedPathParameter(name, value)
                }
            }
        }.build()
    }

    private fun HttpUrl.Builder.addEncodedPathParameter(name: String, value: String?) {
        if (value != null) {
            addQueryParameter(name, value)
        }
    }
}
