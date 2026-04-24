package com.dumpdiary.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumpdiary.app.data.repository.AuthRepository
import com.dumpdiary.app.data.repository.AppUpdateRepository
import com.dumpdiary.app.data.repository.AppUpdateUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val updateInfo: AppUpdateUi? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appUpdateRepository: AppUpdateRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkForUpdate()
    }

    fun login(email: String, password: String) = launchAction {
        authRepository.login(email, password)
        "Login success."
    }

    fun register(email: String, password: String, code: String) = launchAction {
        authRepository.register(email, password, code)
        "Register success."
    }

    fun sendRegisterCode(email: String) = launchAction {
        authRepository.sendRegisterCode(email).message
    }

    fun sendResetCode(email: String) = launchAction {
        authRepository.sendResetCode(email).message
    }

    fun resetPassword(email: String, code: String, newPassword: String) = launchAction {
        authRepository.resetPassword(email, code, newPassword).message
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
            if (updateInfo != null) {
                _uiState.update { it.copy(updateInfo = updateInfo) }
            }
        }
    }

    private fun launchAction(block: suspend () -> String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            val message = runCatching { block() }.getOrElse { it.message ?: "Something went wrong." }
            _uiState.update { it.copy(isLoading = false, message = message) }
        }
    }
}
