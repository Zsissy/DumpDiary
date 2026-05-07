package com.dumpdiary.app.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.repository.AuthRepository
import com.dumpdiary.app.data.repository.FriendRepository
import com.dumpdiary.app.data.repository.LogRepository
import com.dumpdiary.app.data.repository.ProfileRepository
import com.dumpdiary.app.data.repository.ServerConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val serverBaseUrl: String = "",
    val serverType: String = "rest",
    val supabaseAnonKey: String = "",
    val isServerValidating: Boolean = false,
    val serverStatusMessage: String? = null,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val logRepository: LogRepository,
    private val authRepository: AuthRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val serverConfigRepository: ServerConfigRepository,
    private val friendRepository: FriendRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                _uiState.update {
                    it.copy(
                        serverBaseUrl = prefs.serverBaseUrl,
                        serverType = prefs.serverType,
                        supabaseAnonKey = prefs.supabaseAnonKey,
                        serverStatusMessage = if (prefs.serverBaseUrl.isBlank()) null else "Connected server: ${prefs.serverBaseUrl}",
                    )
                }
            }
        }
    }

    fun updateDisplayName(displayName: String) = runAction {
        profileRepository.updateDisplayName(displayName)
        "Profile updated."
    }

    fun uploadAvatar(uri: Uri) = runAction {
        profileRepository.uploadAvatar(uri)
        "Avatar updated."
    }

    fun updateLanguage(languageTag: String) = runAction {
        preferencesRepository.updateLanguage(languageTag)
        "Language updated."
    }

    fun validateAndSwitchServer(rawInput: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isServerValidating = true, message = null, serverStatusMessage = null) }
            val message = runCatching {
                val normalized = serverConfigRepository.validateAndSwitch(rawInput)
                preferencesRepository.updateServerType("rest")
                friendRepository.clearFriends()
                _uiState.update { state -> state.copy(serverStatusMessage = "Connected server: $normalized", serverType = "rest") }
                "Server switched. Please log in again."
            }.getOrElse { it.message ?: "Unable to switch the server address." }
            _uiState.update { it.copy(isLoading = false, isServerValidating = false, message = message) }
        }
    }

    fun validateAndSwitchSupabaseServer(baseUrl: String, anonKey: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isServerValidating = true, message = null, serverStatusMessage = null) }
            val message = runCatching {
                val normalized = serverConfigRepository.validateAndSwitchSupabase(baseUrl, anonKey)
                friendRepository.clearFriends()
                _uiState.update { state -> state.copy(serverStatusMessage = "Connected server: $normalized", serverType = "supabase", supabaseAnonKey = anonKey) }
                "Supabase server switched. Please log in again."
            }.getOrElse { it.message ?: "Unable to switch Supabase server." }
            _uiState.update { it.copy(isLoading = false, isServerValidating = false, message = message) }
        }
    }

    fun exportLogs(uri: Uri) = runAction {
        val count = logRepository.exportOwnLogsToCsv(uri)
        "Exported $count logs to CSV."
    }

    fun importLogs(uri: Uri) = runAction {
        val count = logRepository.importLogsFromCsv(uri)
        "Imported $count logs from CSV."
    }

    fun logout() = runAction {
        authRepository.logout()
        "Logged out."
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
    }

    private fun runAction(block: suspend () -> String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            val message = runCatching { block() }.getOrElse { it.message ?: "Action failed." }
            _uiState.update { it.copy(isLoading = false, message = message) }
        }
    }
}
