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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u001e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u000e\u0010\u0016\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0017J.\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0013H\u0082@\u00a2\u0006\u0002\u0010\u001cJ\u000e\u0010\u001d\u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010\u0017J&\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010!J&\u0010\"\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u00132\u0006\u0010$\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010%\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010&J\u0016\u0010\'\u001a\u00020#2\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010&R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006("}, d2 = {"Lcom/dumpdiary/app/data/repository/AuthRepository;", "", "api", "Lcom/dumpdiary/app/data/remote/DumpDiaryApi;", "preferencesRepository", "Lcom/dumpdiary/app/data/local/UserPreferencesRepository;", "profileDao", "Lcom/dumpdiary/app/data/local/ProfileDao;", "logDao", "Lcom/dumpdiary/app/data/local/LogDao;", "(Lcom/dumpdiary/app/data/remote/DumpDiaryApi;Lcom/dumpdiary/app/data/local/UserPreferencesRepository;Lcom/dumpdiary/app/data/local/ProfileDao;Lcom/dumpdiary/app/data/local/LogDao;)V", "sessionFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/dumpdiary/app/data/repository/AppSession;", "getSessionFlow", "()Lkotlinx/coroutines/flow/Flow;", "login", "", "email", "", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "persistSession", "accessToken", "refreshToken", "userId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshSession", "", "register", "code", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetPassword", "Lcom/dumpdiary/app/data/model/MessageDto;", "newPassword", "sendRegisterCode", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendResetCode", "app_debug"})
public final class AuthRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.remote.DumpDiaryApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.ProfileDao profileDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.LogDao logDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.dumpdiary.app.data.repository.AppSession> sessionFlow = null;
    
    @javax.inject.Inject()
    public AuthRepository(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.remote.DumpDiaryApi api, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.ProfileDao profileDao, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.LogDao logDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.dumpdiary.app.data.repository.AppSession> getSessionFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendRegisterCode(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendResetCode(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object register(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object login(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object resetPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String code, @org.jetbrains.annotations.NotNull()
    java.lang.String newPassword, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refreshSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logout(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object persistSession(java.lang.String accessToken, java.lang.String refreshToken, java.lang.String userId, java.lang.String email, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}