package com.dumpdiary.app.data.repository;

import android.content.ContentResolver;
import android.net.Uri;
import androidx.work.ExistingWorkPolicy;
import androidx.work.WorkManager;
import com.dumpdiary.app.data.local.LogDao;
import com.dumpdiary.app.data.local.ProfileDao;
import com.dumpdiary.app.data.local.UserPreferences;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.model.AuthRequestDto;
import com.dumpdiary.app.data.model.AppVersionDto;
import com.dumpdiary.app.data.model.BowelLogDto;
import com.dumpdiary.app.data.model.BowelLogEntity;
import com.dumpdiary.app.data.model.AddFriendRequestDto;
import com.dumpdiary.app.data.model.FriendProfileDto;
import com.dumpdiary.app.data.model.MessageDto;
import com.dumpdiary.app.data.model.RefreshRequestDto;
import com.dumpdiary.app.data.model.RegisterRequestDto;
import com.dumpdiary.app.data.model.ResetPasswordRequestDto;
import com.dumpdiary.app.data.model.SendEmailCodeRequestDto;
import com.dumpdiary.app.data.model.UpdateProfileRequestDto;
import com.dumpdiary.app.data.model.UserProfileEntity;
import com.dumpdiary.app.data.model.VerificationPurposeDto;
import com.dumpdiary.app.data.remote.DumpDiaryApi;
import com.dumpdiary.app.worker.SyncWorker;
import dagger.hilt.android.qualifiers.ApplicationContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.Clock;
import okhttp3.MultipartBody;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001BG\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\fJ\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0006H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\nH\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003JW\u0010\u001e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00062\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n2\b\b\u0002\u0010\u000b\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020\u0006H\u00d6\u0001J\t\u0010#\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u0011\u0010\b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006$"}, d2 = {"Lcom/dumpdiary/app/data/repository/BowelLogInput;", "", "id", "", "occurredAt", "durationSeconds", "", "feeling", "stoolForm", "symptomTags", "", "detailNote", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;ILjava/util/List;Ljava/lang/String;)V", "getDetailNote", "()Ljava/lang/String;", "getDurationSeconds", "()I", "getFeeling", "getId", "getOccurredAt", "getStoolForm", "getSymptomTags", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class BowelLogInput {
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String occurredAt = null;
    private final int durationSeconds = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String feeling = null;
    private final int stoolForm = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> symptomTags = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String detailNote = null;
    
    public BowelLogInput(@org.jetbrains.annotations.Nullable()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String occurredAt, int durationSeconds, @org.jetbrains.annotations.NotNull()
    java.lang.String feeling, int stoolForm, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> symptomTags, @org.jetbrains.annotations.NotNull()
    java.lang.String detailNote) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOccurredAt() {
        return null;
    }
    
    public final int getDurationSeconds() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFeeling() {
        return null;
    }
    
    public final int getStoolForm() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getSymptomTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDetailNote() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dumpdiary.app.data.repository.BowelLogInput copy(@org.jetbrains.annotations.Nullable()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String occurredAt, int durationSeconds, @org.jetbrains.annotations.NotNull()
    java.lang.String feeling, int stoolForm, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> symptomTags, @org.jetbrains.annotations.NotNull()
    java.lang.String detailNote) {
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