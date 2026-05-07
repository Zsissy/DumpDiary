package com.dumpdiary.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.repository.AppUpdateRepository
import com.dumpdiary.app.data.repository.AppUpdateUi
import com.dumpdiary.app.data.repository.AuthRepository
import com.dumpdiary.app.data.repository.ServerConfigRepository
import com.dumpdiary.app.data.repository.SupabaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isServerValidating: Boolean = false,
    val message: String? = null,
    val updateInfo: AppUpdateUi? = null,
    val serverBaseUrl: String = "",
    val serverType: String = "rest",
    val supabaseAnonKey: String = "",
    val serverStatusMessage: String? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val supabaseAuthRepository: SupabaseAuthRepository,
    private val appUpdateRepository: AppUpdateRepository,
    private val serverConfigRepository: ServerConfigRepository,
    private val preferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                _uiState.update {
                    it.copy(
                        serverBaseUrl = prefs.serverBaseUrl,
                        serverType = prefs.serverType,
                        supabaseAnonKey = prefs.supabaseAnonKey,
                        serverStatusMessage = if (prefs.serverBaseUrl.isNotBlank()) "Connected server: ${prefs.serverBaseUrl}" else null,
                    )
                }
            }
        }
        checkForUpdate()
    }

    // ---- REST API methods ----

    fun login(email: String, password: String) = launchAction {
        requireConfiguredServer()
        authRepository.login(email, password)
        "Login success."
    }

    fun register(email: String, password: String, code: String) = launchAction {
        requireConfiguredServer()
        authRepository.register(email, password, code)
        "Register success."
    }

    fun sendRegisterCode(email: String) = launchAction {
        requireConfiguredServer()
        authRepository.sendRegisterCode(email).message
    }

    fun sendResetCode(email: String) = launchAction {
        requireConfiguredServer()
        authRepository.sendResetCode(email).message
    }

    fun resetPassword(email: String, code: String, newPassword: String) = launchAction {
        requireConfiguredServer()
        authRepository.resetPassword(email, code, newPassword).message
    }

    // ---- Supabase methods ----

    fun supabaseLogin(username: String, password: String) = launchAction {
        requireConfiguredServer()
        supabaseAuthRepository.login(username, password)
        "Login success."
    }

    fun supabaseRegister(username: String, password: String, nickname: String = "", matchCode: String = "") = launchAction {
        requireConfiguredServer()
        supabaseAuthRepository.register(username, password, nickname, matchCode)
        "注册成功，等待管理员审核后可登录。"
    }

    // ---- Server config ----

    fun setServerType(type: String) {
        viewModelScope.launch {
            preferencesRepository.updateServerType(type)
            _uiState.update { it.copy(serverType = type) }
        }
    }

    fun validateAndSaveServer(rawInput: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isServerValidating = true, message = null, serverStatusMessage = null) }
            val message = runCatching {
                val normalized = serverConfigRepository.validateAndSave(rawInput)
                preferencesRepository.updateServerType("rest")
                _uiState.update { state -> state.copy(serverStatusMessage = "Connected server: $normalized", serverType = "rest") }
                checkForUpdate()
                "Server address saved."
            }.getOrElse { it.message ?: "Unable to save the server address." }
            _uiState.update { it.copy(isServerValidating = false, message = message) }
        }
    }

    fun validateAndSaveSupabaseServer(baseUrl: String, anonKey: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isServerValidating = true, message = null, serverStatusMessage = null) }
            val message = runCatching {
                val normalized = serverConfigRepository.validateAndSaveSupabase(baseUrl, anonKey)
                _uiState.update { state -> state.copy(serverStatusMessage = "Connected server: $normalized", serverType = "supabase", supabaseAnonKey = anonKey) }
                "Supabase server saved."
            }.getOrElse { it.message ?: "Unable to save Supabase server." }
            _uiState.update { it.copy(isServerValidating = false, message = message) }
        }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun dismissUpdate() {
        _uiState.update { it.copy(updateInfo = null) }
    }

    fun checkForUpdate() {
        viewModelScope.launch {
            val updateInfo = runCatching { appUpdateRepository.checkForUpdate() }.getOrNull()
            _uiState.update { it.copy(updateInfo = updateInfo) }
        }
    }

    private fun launchAction(block: suspend () -> String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            val message = runCatching { block() }.getOrElse { it.message ?: "Something went wrong." }
            _uiState.update { it.copy(isLoading = false, message = message) }
        }
    }

    private suspend fun requireConfiguredServer() {
        serverConfigRepository.requireConfiguredBaseUrl()
    }
}
