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

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u0016H\u0002J\u0016\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u0010 \u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u0010!\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020#H\u0086@\u00a2\u0006\u0002\u0010$J\u000e\u0010%\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010&J\u0016\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020*H\u0082@\u00a2\u0006\u0002\u0010+J\u000e\u0010,\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010&R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006-"}, d2 = {"Lcom/dumpdiary/app/data/repository/LogRepository;", "", "api", "Lcom/dumpdiary/app/data/remote/DumpDiaryApi;", "logDao", "Lcom/dumpdiary/app/data/local/LogDao;", "profileDao", "Lcom/dumpdiary/app/data/local/ProfileDao;", "preferencesRepository", "Lcom/dumpdiary/app/data/local/UserPreferencesRepository;", "contentResolver", "Landroid/content/ContentResolver;", "workManager", "Landroidx/work/WorkManager;", "(Lcom/dumpdiary/app/data/remote/DumpDiaryApi;Lcom/dumpdiary/app/data/local/LogDao;Lcom/dumpdiary/app/data/local/ProfileDao;Lcom/dumpdiary/app/data/local/UserPreferencesRepository;Landroid/content/ContentResolver;Landroidx/work/WorkManager;)V", "logsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/dumpdiary/app/data/model/BowelLogEntity;", "getLogsFlow", "()Lkotlinx/coroutines/flow/Flow;", "createOrUpdate", "", "input", "Lcom/dumpdiary/app/data/repository/BowelLogInput;", "(Lcom/dumpdiary/app/data/repository/BowelLogInput;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "enqueueSync", "exportOwnLogsToCsv", "", "uri", "Landroid/net/Uri;", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "importLogsFromCsv", "markDeleted", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshFromRemote", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resolveProfile", "Lcom/dumpdiary/app/data/model/UserProfileEntity;", "prefs", "Lcom/dumpdiary/app/data/local/UserPreferences;", "(Lcom/dumpdiary/app/data/local/UserPreferences;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncPendingChanges", "app_debug"})
public final class LogRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.remote.DumpDiaryApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.LogDao logDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.ProfileDao profileDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.ContentResolver contentResolver = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.work.WorkManager workManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.dumpdiary.app.data.model.BowelLogEntity>> logsFlow = null;
    
    @javax.inject.Inject()
    public LogRepository(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.remote.DumpDiaryApi api, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.LogDao logDao, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.ProfileDao profileDao, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository, @org.jetbrains.annotations.NotNull()
    android.content.ContentResolver contentResolver, @org.jetbrains.annotations.NotNull()
    androidx.work.WorkManager workManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.dumpdiary.app.data.model.BowelLogEntity>> getLogsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createOrUpdate(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.BowelLogInput input, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object markDeleted(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object syncPendingChanges(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refreshFromRemote(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object exportOwnLogsToCsv(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object importLogsFromCsv(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    private final void enqueueSync() {
    }
    
    private final java.lang.Object resolveProfile(com.dumpdiary.app.data.local.UserPreferences prefs, kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.UserProfileEntity> $completion) {
        return null;
    }
}