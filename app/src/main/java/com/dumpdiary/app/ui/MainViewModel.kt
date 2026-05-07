package com.dumpdiary.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.repository.AppSession
import com.dumpdiary.app.data.repository.AuthRepository
import com.dumpdiary.app.data.repository.LogRepository
import com.dumpdiary.app.data.repository.ProfileRepository
import com.dumpdiary.app.data.repository.UserProfileUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainUiState(
    val session: AppSession = AppSession(false, "", "", "", "", "en", ""),
    val profile: UserProfileUi? = null,
    val languageTag: String = "en",
    val isReady: Boolean = false,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository,
    profileRepository: ProfileRepository,
    private val logRepository: LogRepository,
    private val preferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = combine(
        authRepository.sessionFlow,
        profileRepository.profileFlow,
    ) { session, profile ->
        MainUiState(
            session = session,
            profile = profile,
            languageTag = session.languageTag,
            isReady = true,
        )
    }.distinctUntilChanged().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainUiState(),
    )

    fun refreshAll() {
        viewModelScope.launch {
            runCatching { logRepository.refreshFromRemote() }
        }
    }

    fun updateLanguage(languageTag: String) {
        viewModelScope.launch {
            preferencesRepository.updateLanguage(languageTag)
        }
    }
}
