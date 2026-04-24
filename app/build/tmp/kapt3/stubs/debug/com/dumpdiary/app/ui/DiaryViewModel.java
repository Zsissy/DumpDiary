package com.dumpdiary.app.ui;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.dumpdiary.app.data.model.BowelLogEntity;
import com.dumpdiary.app.data.repository.BowelLogInput;
import com.dumpdiary.app.data.repository.FriendRepository;
import com.dumpdiary.app.data.repository.FriendUi;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
import com.dumpdiary.app.data.repository.UserProfileUi;
import java.time.Duration;
import dagger.hilt.android.lifecycle.HiltViewModel;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u0007\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0011J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0013J\u0010\u0010\u0017\u001a\u00020\u00112\b\u0010\u0016\u001a\u0004\u0018\u00010\u0013J\u000e\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u0011J\u0006\u0010\u001c\u001a\u00020\u0011J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000bH\u0002J\u0006\u0010 \u001a\u00020\u0011J\u0010\u0010!\u001a\u00020\u00112\b\u0010\"\u001a\u0004\u0018\u00010\u0013J\u0006\u0010#\u001a\u00020\u0011J\u0006\u0010$\u001a\u00020\u0011J\u000e\u0010%\u001a\u00020\u00112\u0006\u0010&\u001a\u00020\u0013J\u000e\u0010\'\u001a\u00020\u00112\u0006\u0010&\u001a\u00020\u0013J\u000e\u0010(\u001a\u00020\u00112\u0006\u0010&\u001a\u00020\u0013J\u000e\u0010)\u001a\u00020\u00112\u0006\u0010&\u001a\u00020\u0013J\u000e\u0010*\u001a\u00020\u00112\u0006\u0010&\u001a\u00020+R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006,"}, d2 = {"Lcom/dumpdiary/app/ui/DiaryViewModel;", "Landroidx/lifecycle/ViewModel;", "logRepository", "Lcom/dumpdiary/app/data/repository/LogRepository;", "profileRepository", "Lcom/dumpdiary/app/data/repository/ProfileRepository;", "friendRepository", "Lcom/dumpdiary/app/data/repository/FriendRepository;", "(Lcom/dumpdiary/app/data/repository/LogRepository;Lcom/dumpdiary/app/data/repository/ProfileRepository;Lcom/dumpdiary/app/data/repository/FriendRepository;)V", "formState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/dumpdiary/app/ui/DiaryUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addFriend", "", "email", "", "consumeMessage", "deleteLog", "id", "loadForEdit", "moveMonth", "offset", "", "refresh", "resetForm", "resolveDurationSeconds", "", "state", "saveLog", "selectCalendarUser", "userId", "startDurationTimer", "stopDurationTimer", "toggleSymptomTag", "value", "updateDetailNote", "updateFeeling", "updateOccurredAt", "updateStoolForm", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DiaryViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.LogRepository logRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.ProfileRepository profileRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.FriendRepository friendRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.dumpdiary.app.ui.DiaryUiState> formState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.DiaryUiState> uiState = null;
    
    @javax.inject.Inject()
    public DiaryViewModel(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.LogRepository logRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.ProfileRepository profileRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.FriendRepository friendRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.dumpdiary.app.ui.DiaryUiState> getUiState() {
        return null;
    }
    
    public final void refresh() {
    }
    
    public final void moveMonth(long offset) {
    }
    
    public final void selectCalendarUser(@org.jetbrains.annotations.Nullable()
    java.lang.String userId) {
    }
    
    public final void loadForEdit(@org.jetbrains.annotations.Nullable()
    java.lang.String id) {
    }
    
    public final void resetForm() {
    }
    
    public final void updateOccurredAt(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void startDurationTimer() {
    }
    
    public final void stopDurationTimer() {
    }
    
    public final void updateFeeling(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void updateStoolForm(float value) {
    }
    
    public final void toggleSymptomTag(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void updateDetailNote(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void saveLog() {
    }
    
    public final void deleteLog(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void addFriend(@org.jetbrains.annotations.NotNull()
    java.lang.String email) {
    }
    
    public final void consumeMessage() {
    }
    
    private final int resolveDurationSeconds(com.dumpdiary.app.ui.DiaryUiState state) {
        return 0;
    }
}