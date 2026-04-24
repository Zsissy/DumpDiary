package com.dumpdiary.app.ui;

import androidx.lifecycle.ViewModel;
import com.dumpdiary.app.data.repository.AuthRepository;
import com.dumpdiary.app.data.repository.AppUpdateRepository;
import com.dumpdiary.app.data.repository.AppUpdateUi;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fJ\u0006\u0010\u0011\u001a\u00020\u000fJ+\u0010\u0012\u001a\u00020\u000f2\u001c\u0010\u0013\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u0015\u0012\u0006\u0012\u0004\u0018\u00010\u00170\u0014H\u0002\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u0016J\u001e\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u0016J\u001e\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\u0016J\u000e\u0010 \u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u0016J\u000e\u0010!\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u0016R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\""}, d2 = {"Lcom/dumpdiary/app/ui/AuthViewModel;", "Landroidx/lifecycle/ViewModel;", "authRepository", "Lcom/dumpdiary/app/data/repository/AuthRepository;", "appUpdateRepository", "Lcom/dumpdiary/app/data/repository/AppUpdateRepository;", "(Lcom/dumpdiary/app/data/repository/AuthRepository;Lcom/dumpdiary/app/data/repository/AppUpdateRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dumpdiary/app/ui/AuthUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "checkForUpdate", "", "consumeMessage", "dismissUpdate", "launchAction", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "", "(Lkotlin/jvm/functions/Function1;)V", "login", "email", "password", "register", "code", "resetPassword", "newPassword", "sendRegisterCode", "sendResetCode", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AuthViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.AppUpdateRepository appUpdateRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dumpdiary.app.ui.AuthUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.AuthUiState> uiState = null;
    
    @javax.inject.Inject()
    public AuthViewModel(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.AppUpdateRepository appUpdateRepository) {
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
    
    public final void consumeMessage() {
    }
    
    public final void dismissUpdate() {
    }
    
    public final void checkForUpdate() {
    }
    
    private final void launchAction(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super java.lang.String>, ? extends java.lang.Object> block) {
    }
}