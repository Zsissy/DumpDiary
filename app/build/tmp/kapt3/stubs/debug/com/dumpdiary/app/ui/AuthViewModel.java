package com.dumpdiary.app.ui;

import androidx.lifecycle.ViewModel;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.repository.AppUpdateRepository;
import com.dumpdiary.app.data.repository.AppUpdateUi;
import com.dumpdiary.app.data.repository.AuthRepository;
import com.dumpdiary.app.data.repository.ServerConfigRepository;
import com.dumpdiary.app.data.repository.SupabaseAuthRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0019\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0015J\u0006\u0010\u0017\u001a\u00020\u0015J+\u0010\u0018\u001a\u00020\u00152\u001c\u0010\u0019\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u001b\u0012\u0006\u0012\u0004\u0018\u00010\u001d0\u001aH\u0002\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\u001cJ\u001e\u0010\"\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\u001c2\u0006\u0010#\u001a\u00020\u001cJ\u000e\u0010$\u001a\u00020\u0015H\u0082@\u00a2\u0006\u0002\u0010%J\u001e\u0010&\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u001c2\u0006\u0010#\u001a\u00020\u001c2\u0006\u0010\'\u001a\u00020\u001cJ\u000e\u0010(\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u001cJ\u000e\u0010)\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u001cJ\u000e\u0010*\u001a\u00020\u00152\u0006\u0010+\u001a\u00020\u001cJ\u0016\u0010,\u001a\u00020\u00152\u0006\u0010-\u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\u001cJ*\u0010.\u001a\u00020\u00152\u0006\u0010-\u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\u001c2\b\b\u0002\u0010/\u001a\u00020\u001c2\b\b\u0002\u00100\u001a\u00020\u001cJ\u000e\u00101\u001a\u00020\u00152\u0006\u00102\u001a\u00020\u001cJ\u0016\u00103\u001a\u00020\u00152\u0006\u00104\u001a\u00020\u001c2\u0006\u00105\u001a\u00020\u001cR\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u00066"}, d2 = {"Lcom/dumpdiary/app/ui/AuthViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/dumpdiary/app/data/repository/AuthRepository;", "supabaseAuthRepository", "Lcom/dumpdiary/app/data/repository/SupabaseAuthRepository;", "appUpdateRepository", "Lcom/dumpdiary/app/data/repository/AppUpdateRepository;", "serverConfigRepository", "Lcom/dumpdiary/app/data/repository/ServerConfigRepository;", "preferencesRepository", "Lcom/dumpdiary/app/data/local/UserPreferencesRepository;", "(Lcom/dumpdiary/app/data/repository/AuthRepository;Lcom/dumpdiary/app/data/repository/SupabaseAuthRepository;Lcom/dumpdiary/app/data/repository/AppUpdateRepository;Lcom/dumpdiary/app/data/repository/ServerConfigRepository;Lcom/dumpdiary/app/data/local/UserPreferencesRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dumpdiary/app/ui/AuthUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "checkForUpdate", "", "consumeMessage", "dismissUpdate", "launchAction", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "", "(Lkotlin/jvm/functions/Function1;)V", "login", "email", "password", "register", "code", "requireConfiguredServer", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetPassword", "newPassword", "sendRegisterCode", "sendResetCode", "setServerType", "type", "supabaseLogin", "username", "supabaseRegister", "nickname", "matchCode", "validateAndSaveServer", "rawInput", "validateAndSaveSupabaseServer", "baseUrl", "anonKey", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AuthViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.SupabaseAuthRepository supabaseAuthRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.AppUpdateRepository appUpdateRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.ServerConfigRepository serverConfigRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dumpdiary.app.ui.AuthUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.AuthUiState> uiState = null;
    
    @javax.inject.Inject()
    public AuthViewModel(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.SupabaseAuthRepository supabaseAuthRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.AppUpdateRepository appUpdateRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.ServerConfigRepository serverConfigRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.AuthUiState> getUiState() {
        return null;
    }
    
    public final void login(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password) {
    }
    
    public final void register(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String code) {
    }
    
    public final void sendRegisterCode(@org.jetbrains.annotations.NotNull()
    java.lang.String email) {
    }
    
    public final void sendResetCode(@org.jetbrains.annotations.NotNull()
    java.lang.String email) {
    }
    
    public final void resetPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    java.lang.String newPassword) {
    }
    
    public final void supabaseLogin(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String password) {
    }
    
    public final void supabaseRegister(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String nickname, @org.jetbrains.annotations.NotNull()
    java.lang.String matchCode) {
    }
    
    public final void setServerType(@org.jetbrains.annotations.NotNull()
    java.lang.String type) {
    }
    
    public final void validateAndSaveServer(@org.jetbrains.annotations.NotNull()
    java.lang.String rawInput) {
    }
    
    public final void validateAndSaveSupabaseServer(@org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String anonKey) {
    }
    
    public final void consumeMessage() {
    }
    
    public final void dismissUpdate() {
    }
    
    public final void checkForUpdate() {
    }
    
    private final void launchAction(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super java.lang.String>, ? extends java.lang.Object> block) {
    }
    
    private final java.lang.Object requireConfiguredServer(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}