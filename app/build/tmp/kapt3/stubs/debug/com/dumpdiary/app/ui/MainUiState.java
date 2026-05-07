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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B/\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\tH\u00c6\u0003J3\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\t2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0007H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001c"}, d2 = {"Lcom/dumpdiary/app/ui/MainUiState;", "", "session", "Lcom/dumpdiary/app/data/repository/AppSession;", "profile", "Lcom/dumpdiary/app/data/repository/UserProfileUi;", "languageTag", "", "isReady", "", "(Lcom/dumpdiary/app/data/repository/AppSession;Lcom/dumpdiary/app/data/repository/UserProfileUi;Ljava/lang/String;Z)V", "()Z", "getLanguageTag", "()Ljava/lang/String;", "getProfile", "()Lcom/dumpdiary/app/data/repository/UserProfileUi;", "getSession", "()Lcom/dumpdiary/app/data/repository/AppSession;", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class MainUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.AppSession session = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dumpdiary.app.data.repository.UserProfileUi profile = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String languageTag = null;
    private final boolean isReady = false;
    
    public MainUiState(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.AppSession session, @org.jetbrains.annotations.Nullable()
    com.dumpdiary.app.data.repository.UserProfileUi profile, @org.jetbrains.annotations.NotNull()
    java.lang.String languageTag, boolean isReady) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.data.repository.AppSession getSession() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dumpdiary.app.data.repository.UserProfileUi getProfile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLanguageTag() {
        return null;
    }
    
    public final boolean isReady() {
        return false;
    }
    
    public MainUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.data.repository.AppSession component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dumpdiary.app.data.repository.UserProfileUi component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.ui.MainUiState copy(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.AppSession session, @org.jetbrains.annotations.Nullable()
    com.dumpdiary.app.data.repository.UserProfileUi profile, @org.jetbrains.annotations.NotNull()
    java.lang.String languageTag, boolean isReady) {
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