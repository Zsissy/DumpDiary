package com.dumpdiary.app.ui;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.repository.AuthRepository;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016J\u0006\u0010\u0018\u001a\u00020\u0013J+\u0010\u0019\u001a\u00020\u00132\u001c\u0010\u001a\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u001c\u0012\u0006\u0012\u0004\u0018\u00010\u001e0\u001bH\u0002\u00a2\u0006\u0002\u0010\u001fJ\u000e\u0010 \u001a\u00020\u00132\u0006\u0010!\u001a\u00020\u001dJ\u000e\u0010\"\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\u001dJ\u000e\u0010$\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006%"}, d2 = {"Lcom/dumpdiary/app/ui/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "profileRepository", "Lcom/dumpdiary/app/data/repository/ProfileRepository;", "logRepository", "Lcom/dumpdiary/app/data/repository/LogRepository;", "authRepository", "Lcom/dumpdiary/app/data/repository/AuthRepository;", "preferencesRepository", "Lcom/dumpdiary/app/data/local/UserPreferencesRepository;", "(Lcom/dumpdiary/app/data/repository/ProfileRepository;Lcom/dumpdiary/app/data/repository/LogRepository;Lcom/dumpdiary/app/data/repository/AuthRepository;Lcom/dumpdiary/app/data/local/UserPreferencesRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dumpdiary/app/ui/SettingsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "consumeMessage", "", "exportLogs", "uri", "Landroid/net/Uri;", "importLogs", "logout", "runAction", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "", "(Lkotlin/jvm/functions/Function1;)V", "updateDisplayName", "displayName", "updateLanguage", "languageTag", "uploadAvatar", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.ProfileRepository profileRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.LogRepository logRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dumpdiary.app.ui.SettingsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.SettingsUiState> uiState = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.ProfileRepository profileRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.LogRepository logRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.SettingsUiState> getUiState() {
        return null;
    }
    
    public final void updateDisplayName(@org.jetbrains.annotations.NotNull()
    java.lang.String displayName) {
    }
    
    public final void uploadAvatar(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void updateLanguage(@org.jetbrains.annotations.NotNull()
    java.lang.String languageTag) {
    }
    
    public final void exportLogs(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void importLogs(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void logout() {
    }
    
    public final void consumeMessage() {
    }
    
    private final void runAction(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super java.lang.String>, ? extends java.lang.Object> block) {
    }
}