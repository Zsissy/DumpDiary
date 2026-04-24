package com.dumpdiary.app.ui;

import androidx.lifecycle.ViewModel;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.repository.AppSession;
import com.dumpdiary.app.data.repository.AuthRepository;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
import com.dumpdiary.app.data.repository.UserProfileUi;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0015"}, d2 = {"Lcom/dumpdiary/app/ui/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/dumpdiary/app/data/repository/AuthRepository;", "profileRepository", "Lcom/dumpdiary/app/data/repository/ProfileRepository;", "logRepository", "Lcom/dumpdiary/app/data/repository/LogRepository;", "preferencesRepository", "Lcom/dumpdiary/app/data/local/UserPreferencesRepository;", "(Lcom/dumpdiary/app/data/repository/AuthRepository;Lcom/dumpdiary/app/data/repository/ProfileRepository;Lcom/dumpdiary/app/data/repository/LogRepository;Lcom/dumpdiary/app/data/local/UserPreferencesRepository;)V", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/dumpdiary/app/ui/MainUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "refreshAll", "", "updateLanguage", "languageTag", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MainViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.LogRepository logRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.MainUiState> uiState = null;
    
    @javax.inject.Inject()
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.ProfileRepository profileRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.LogRepository logRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.MainUiState> getUiState() {
        return null;
    }
    
    public final void refreshAll() {
    }
    
    public final void updateLanguage(@org.jetbrains.annotations.NotNull()
    java.lang.String languageTag) {
    }
}