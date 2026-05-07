package com.dumpdiary.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val accessToken: String = "",
    val refreshToken: String = "",
    val userId: String = "",
    val email: String = "",
    val languageTag: String = "en",
    val serverBaseUrl: String = "",
    val serverType: String = "rest",
    val supabaseAnonKey: String = "",
    val username: String = "",
    val matchCode: String = "",
)

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val accessToken = stringPreferencesKey("access_token")
        val refreshToken = stringPreferencesKey("refresh_token")
        val userId = stringPreferencesKey("user_id")
        val email = stringPreferencesKey("email")
        val languageTag = stringPreferencesKey("language_tag")
        val serverBaseUrl = stringPreferencesKey("server_base_url")
        val serverType = stringPreferencesKey("server_type")
        val supabaseAnonKey = stringPreferencesKey("supabase_anon_key")
        val username = stringPreferencesKey("username")
        val matchCode = stringPreferencesKey("match_code")
    }

    val preferences: Flow<UserPreferences> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
            UserPreferences(
                accessToken = prefs[Keys.accessToken].orEmpty(),
                refreshToken = prefs[Keys.refreshToken].orEmpty(),
                userId = prefs[Keys.userId].orEmpty(),
                email = prefs[Keys.email].orEmpty(),
                languageTag = prefs[Keys.languageTag] ?: "en",
                serverBaseUrl = prefs[Keys.serverBaseUrl].orEmpty(),
                serverType = prefs[Keys.serverType] ?: "rest",
                supabaseAnonKey = prefs[Keys.supabaseAnonKey].orEmpty(),
                username = prefs[Keys.username].orEmpty(),
                matchCode = prefs[Keys.matchCode].orEmpty(),
            )
        }

    suspend fun updateSession(accessToken: String, refreshToken: String, userId: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.accessToken] = accessToken
            prefs[Keys.refreshToken] = refreshToken
            prefs[Keys.userId] = userId
            prefs[Keys.email] = email
        }
    }

    suspend fun updateLanguage(languageTag: String) {
        context.dataStore.edit { prefs -> prefs[Keys.languageTag] = languageTag }
    }

    suspend fun updateServerBaseUrl(serverBaseUrl: String) {
        context.dataStore.edit { prefs -> prefs[Keys.serverBaseUrl] = serverBaseUrl }
    }

    suspend fun updateServerType(type: String) {
        context.dataStore.edit { prefs -> prefs[Keys.serverType] = type }
    }

    suspend fun updateSupabaseAnonKey(key: String) {
        context.dataStore.edit { prefs -> prefs[Keys.supabaseAnonKey] = key }
    }

    suspend fun updateUsername(username: String) {
        context.dataStore.edit { prefs -> prefs[Keys.username] = username }
    }

    suspend fun updateMatchCode(matchCode: String) {
        context.dataStore.edit { prefs -> prefs[Keys.matchCode] = matchCode }
    }

    suspend fun saveSupabaseSession(userId: String, username: String, matchCode: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.userId] = userId
            prefs[Keys.username] = username
            prefs[Keys.matchCode] = matchCode
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.accessToken)
            prefs.remove(Keys.refreshToken)
            prefs.remove(Keys.userId)
            prefs.remove(Keys.email)
            prefs.remove(Keys.username)
            prefs.remove(Keys.matchCode)
        }
    }
}
