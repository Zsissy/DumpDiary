package com.dumpdiary.app.data.repository

import androidx.work.WorkManager
import com.dumpdiary.app.data.local.LogDao
import com.dumpdiary.app.data.local.ProfileDao
import com.dumpdiary.app.data.local.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject

const val SYNC_WORK_NAME = "dump-diary-sync"
private const val HEALTH_OK = "ok"

@Singleton
class ServerConfigRepository @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository,
    private val profileDao: ProfileDao,
    private val logDao: LogDao,
    private val workManager: WorkManager,
) {
    private val healthClient = OkHttpClient()

    val serverBaseUrlFlow: Flow<String> = preferencesRepository.preferences.map { it.serverBaseUrl }

    suspend fun getServerBaseUrl(): String = preferencesRepository.preferences.first().serverBaseUrl

    suspend fun requireConfiguredBaseUrl(): String {
        val configured = getServerBaseUrl()
        if (configured.isBlank()) error("Please configure the server address first.")
        return configured
    }

    suspend fun validateAndSaveSupabase(baseUrl: String, anonKey: String): String {
        val normalized = normalizeServerBaseUrl(baseUrl)
        validateSupabaseHealth(normalized, anonKey)
        preferencesRepository.updateServerBaseUrl(normalized)
        preferencesRepository.updateSupabaseAnonKey(anonKey)
        preferencesRepository.updateServerType("supabase")
        return normalized
    }

    suspend fun validateAndSwitchSupabase(baseUrl: String, anonKey: String): String {
        val normalized = normalizeServerBaseUrl(baseUrl)
        validateSupabaseHealth(normalized, anonKey)
        preferencesRepository.updateServerBaseUrl(normalized)
        preferencesRepository.updateSupabaseAnonKey(anonKey)
        preferencesRepository.updateServerType("supabase")
        workManager.cancelUniqueWork(SupabaseRoomRepository.SUPABASE_SYNC_WORK_NAME)
        workManager.cancelUniqueWork(SYNC_WORK_NAME)
        preferencesRepository.clearSession()
        profileDao.clear()
        logDao.clear()
        return normalized
    }

    suspend fun validateAndSave(rawInput: String): String {
        val normalized = normalizeServerBaseUrl(rawInput)
        validateHealth(normalized)
        preferencesRepository.updateServerBaseUrl(normalized)
        return normalized
    }

    suspend fun validateAndSwitch(rawInput: String): String {
        val normalized = normalizeServerBaseUrl(rawInput)
        validateHealth(normalized)
        preferencesRepository.updateServerBaseUrl(normalized)
        workManager.cancelUniqueWork(SYNC_WORK_NAME)
        preferencesRepository.clearSession()
        profileDao.clear()
        logDao.clear()
        return normalized
    }

    suspend fun clearRuntimeCaches() {
        workManager.cancelUniqueWork(SYNC_WORK_NAME)
        preferencesRepository.clearSession()
        profileDao.clear()
        logDao.clear()
    }

    suspend fun validateOnly(rawInput: String): String {
        val normalized = normalizeServerBaseUrl(rawInput)
        validateHealth(normalized)
        return normalized
    }

    private suspend fun validateHealth(baseUrl: String) {
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(baseUrl).get().build()
            val response = runCatching { healthClient.newCall(request).execute() }
                .getOrElse { throwable ->
                    error(throwable.toReadableServerError())
                }
            response.use { httpResponse ->
                if (!httpResponse.isSuccessful) {
                    error("Server is unreachable. HTTP ${httpResponse.code}.")
                }
                val body = httpResponse.body?.string().orEmpty()
                val json = runCatching { JSONObject(body) }
                    .getOrElse { error("This is not a DumpDiary server.") }
                val service = json.optString("service")
                val status = json.optString("status")
                if (service.isBlank() || status.lowercase() != HEALTH_OK) {
                    error("This is not a DumpDiary server.")
                }
            }
        }
    }

    private suspend fun validateSupabaseHealth(baseUrl: String, anonKey: String) {
        require(anonKey.isNotBlank()) { "Please enter the Supabase anon key." }
        withContext(Dispatchers.IO) {
            val healthUrl = "${baseUrl.trimEnd('/')}/rest/v1/app_users?select=id&limit=1"
            val request = Request.Builder()
                .url(healthUrl)
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .get()
                .build()
            runCatching { healthClient.newCall(request).execute() }
                .getOrElse { throwable ->
                    error(throwable.toReadableServerError())
                }
                .use { response ->
                    if (response.isSuccessful || response.code == 200) {
                        // Valid Supabase server
                    } else if (response.code == 401 || response.code == 403) {
                        error("Invalid anon key or unauthorized access.")
                    } else if (response.code == 404 && response.body?.string()?.contains("relation") == true) {
                        // Table doesn't exist — server is valid but needs SQL setup
                    } else {
                        error("Unable to verify Supabase connection. HTTP ${response.code}. Make sure the address is correct.")
                    }
                }
        }
    }
}

fun normalizeServerBaseUrl(rawInput: String): String {
    val trimmed = rawInput.trim()
    if (trimmed.isBlank()) error("Please enter a server address.")
    val withScheme = if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
        trimmed
    } else {
        "https://$trimmed"
    }
    val parsed = withScheme.toHttpUrlOrNull() ?: error("Please enter a valid server address.")
    if (parsed.scheme != "http" && parsed.scheme != "https") {
        error("Only http and https server addresses are supported.")
    }
    if (parsed.host.isBlank() || parsed.host.contains(" ")) {
        error("Please enter a valid server host.")
    }
    val cleanPath = parsed.encodedPath.trimEnd('/').takeIf { it.isNotEmpty() } ?: ""
    return buildString {
        append(parsed.scheme)
        append("://")
        append(parsed.host)
        if ((parsed.scheme == "http" && parsed.port != 80) || (parsed.scheme == "https" && parsed.port != 443)) {
            append(":")
            append(parsed.port)
        }
        if (cleanPath.isNotEmpty()) {
            append(cleanPath)
        }
        append("/")
    }
}

private fun Throwable.toReadableServerError(): String = when {
    message?.contains("CLEARTEXT", ignoreCase = true) == true ->
        "This server blocks the current protocol. Check whether the address should use http or https."
    message?.contains("timeout", ignoreCase = true) == true ->
        "Server validation timed out."
    else -> "Server is unreachable."
}
