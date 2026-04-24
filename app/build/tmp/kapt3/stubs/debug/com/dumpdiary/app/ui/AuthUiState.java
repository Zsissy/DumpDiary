package com.dumpdiary.app.ui;

import androidx.lifecycle.ViewModel;
import com.dumpdiary.app.data.repository.AuthRepository;
import com.dumpdiary.app.data.repository.AppUpdateRepository;
import com.dumpdiary.app.data.repository.AppUpdateUi;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\'\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J+\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00032\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\tR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/dumpdiary/app/ui/AuthUiState;", "", "isLoading", "", "message", "", "updateInfo", "Lcom/dumpdiary/app/data/repository/AppUpdateUi;", "(ZLjava/lang/String;Lcom/dumpdiary/app/data/repository/AppUpdateUi;)V", "()Z", "getMessage", "()Ljava/lang/String;", "getUpdateInfo", "()Lcom/dumpdiary/app/data/repository/AppUpdateUi;", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class AuthUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String message = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dumpdiary.app.data.repository.AppUpdateUi updateInfo = null;
    
    public AuthUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    com.dumpdiary.app.data.repository.AppUpdateUi updateInfo) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dumpdiary.app.data.repository.AppUpdateUi getUpdateInfo() {
        return null;
    }
    
    public AuthUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dumpdiary.app.data.repository.AppUpdateUi component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.ui.AuthUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    com.dumpdiary.app.data.repository.AppUpdateUi updateInfo) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}