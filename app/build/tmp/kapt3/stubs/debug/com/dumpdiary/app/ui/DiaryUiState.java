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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\"\n\u0002\b\'\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u00ad\u0001\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000e\u001a\u00020\n\u0012\b\b\u0002\u0010\u000f\u001a\u00020\n\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u0011\u001a\u00020\n\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\n0\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\n\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u0018J\u000f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\nH\u00c6\u0003J\t\u0010/\u001a\u00020\u0013H\u00c6\u0003J\u000f\u00100\u001a\b\u0012\u0004\u0012\u00020\n0\u0015H\u00c6\u0003J\t\u00101\u001a\u00020\nH\u00c6\u0003J\u000b\u00102\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000f\u00104\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0003J\u000b\u00105\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u00106\u001a\u00020\fH\u00c6\u0003J\u000b\u00107\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u00108\u001a\u00020\nH\u00c6\u0003J\t\u00109\u001a\u00020\nH\u00c6\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u00b1\u0001\u0010;\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000e\u001a\u00020\n2\b\b\u0002\u0010\u000f\u001a\u00020\n2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u0011\u001a\u00020\n2\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\n0\u00152\b\b\u0002\u0010\u0016\u001a\u00020\n2\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\nH\u00c6\u0001J\u0013\u0010<\u001a\u00020=2\b\u0010>\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010?\u001a\u00020@H\u00d6\u0001J\t\u0010A\u001a\u00020\nH\u00d6\u0001R\u0011\u0010\u0016\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001aR\u0011\u0010\u0011\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001aR\u0013\u0010\r\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001aR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001fR\u0013\u0010\u0017\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001aR\u0011\u0010\u000e\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001aR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001aR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\n0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001a\u00a8\u0006B"}, d2 = {"Lcom/dumpdiary/app/ui/DiaryUiState;", "", "logs", "", "Lcom/dumpdiary/app/data/model/BowelLogEntity;", "profile", "Lcom/dumpdiary/app/data/repository/UserProfileUi;", "friends", "Lcom/dumpdiary/app/data/repository/FriendUi;", "selectedCalendarUserId", "", "selectedMonth", "Ljava/time/YearMonth;", "formId", "occurredAt", "durationSeconds", "timerStartedAt", "feeling", "stoolForm", "", "symptomTags", "", "detailNote", "message", "(Ljava/util/List;Lcom/dumpdiary/app/data/repository/UserProfileUi;Ljava/util/List;Ljava/lang/String;Ljava/time/YearMonth;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;FLjava/util/Set;Ljava/lang/String;Ljava/lang/String;)V", "getDetailNote", "()Ljava/lang/String;", "getDurationSeconds", "getFeeling", "getFormId", "getFriends", "()Ljava/util/List;", "getLogs", "getMessage", "getOccurredAt", "getProfile", "()Lcom/dumpdiary/app/data/repository/UserProfileUi;", "getSelectedCalendarUserId", "getSelectedMonth", "()Ljava/time/YearMonth;", "getStoolForm", "()F", "getSymptomTags", "()Ljava/util/Set;", "getTimerStartedAt", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class DiaryUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs = null;
    @org.jetbrains.annotations.Nullable()
    private final com.dumpdiary.app.data.repository.UserProfileUi profile = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.dumpdiary.app.data.repository.FriendUi> friends = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String selectedCalendarUserId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.time.YearMonth selectedMonth = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String formId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String occurredAt = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String durationSeconds = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String timerStartedAt = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String feeling = null;
    private final float stoolForm = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> symptomTags = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String detailNote = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String message = null;
    
    public DiaryUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, @org.jetbrains.annotations.Nullable()
    com.dumpdiary.app.data.repository.UserProfileUi profile, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dumpdiary.app.data.repository.FriendUi> friends, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedCalendarUserId, @org.jetbrains.annotations.NotNull()
    java.time.YearMonth selectedMonth, @org.jetbrains.annotations.Nullable()
    java.lang.String formId, @org.jetbrains.annotations.NotNull()
    java.lang.String occurredAt, @org.jetbrains.annotations.NotNull()
    java.lang.String durationSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.String timerStartedAt, @org.jetbrains.annotations.NotNull()
    java.lang.String feeling, float stoolForm, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> symptomTags, @org.jetbrains.annotations.NotNull()
    java.lang.String detailNote, @org.jetbrains.annotations.Nullable()
    java.lang.String message) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> getLogs() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dumpdiary.app.data.repository.UserProfileUi getProfile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dumpdiary.app.data.repository.FriendUi> getFriends() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSelectedCalendarUserId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.YearMonth getSelectedMonth() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getFormId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOccurredAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDurationSeconds() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTimerStartedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFeeling() {
        return null;
    }
    
    public final float getStoolForm() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getSymptomTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDetailNote() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMessage() {
        return null;
    }
    
    public DiaryUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    public final float component11() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dumpdiary.app.data.repository.UserProfileUi component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dumpdiary.app.data.repository.FriendUi> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.YearMonth component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.ui.DiaryUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, @org.jetbrains.annotations.Nullable()
    com.dumpdiary.app.data.repository.UserProfileUi profile, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dumpdiary.app.data.repository.FriendUi> friends, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedCalendarUserId, @org.jetbrains.annotations.NotNull()
    java.time.YearMonth selectedMonth, @org.jetbrains.annotations.Nullable()
    java.lang.String formId, @org.jetbrains.annotations.NotNull()
    java.lang.String occurredAt, @org.jetbrains.annotations.NotNull()
    java.lang.String durationSeconds, @org.jetbrains.annotations.Nullable()
    java.lang.String timerStartedAt, @org.jetbrains.annotations.NotNull()
    java.lang.String feeling, float stoolForm, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> symptomTags, @org.jetbrains.annotations.NotNull()
    java.lang.String detailNote, @org.jetbrains.annotations.Nullable()
    java.lang.String message) {
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