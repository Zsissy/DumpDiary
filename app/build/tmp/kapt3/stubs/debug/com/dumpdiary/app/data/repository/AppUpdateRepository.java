package com.dumpdiary.app.data.repository;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.OpenableColumns;
import androidx.work.ExistingWorkPolicy;
import androidx.work.WorkManager;
import com.dumpdiary.app.data.local.LogDao;
import com.dumpdiary.app.data.local.ProfileDao;
import com.dumpdiary.app.data.local.UserPreferences;
import com.dumpdiary.app.data.local.UserPreferencesRepository;
import com.dumpdiary.app.data.model.AuthRequestDto;
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
import com.dumpdiary.app.data.remote.SupabaseApi;
import com.dumpdiary.app.worker.SyncWorker;
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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0082@\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u000e\u001a\u0004\u0018\u00010\fH\u0082@\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u000f\u001a\u0004\u0018\u00010\fH\u0086@\u00a2\u0006\u0002\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/dumpdiary/app/data/repository/AppUpdateRepository;", "", "api", "Lcom/dumpdiary/app/data/remote/DumpDiaryApi;", "supabaseApi", "Lcom/dumpdiary/app/data/remote/SupabaseApi;", "serverConfigRepository", "Lcom/dumpdiary/app/data/repository/ServerConfigRepository;", "preferencesRepository", "Lcom/dumpdiary/app/data/local/UserPreferencesRepository;", "(Lcom/dumpdiary/app/data/remote/DumpDiaryApi;Lcom/dumpdiary/app/data/remote/SupabaseApi;Lcom/dumpdiary/app/data/repository/ServerConfigRepository;Lcom/dumpdiary/app/data/local/UserPreferencesRepository;)V", "checkForRestUpdate", "Lcom/dumpdiary/app/data/repository/AppUpdateUi;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkForSupabaseUpdate", "checkForUpdate", "app_debug"})
public final class AppUpdateRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.remote.DumpDiaryApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.remote.SupabaseApi supabaseApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.ServerConfigRepository serverConfigRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository = null;
    
    @javax.inject.Inject()
    public AppUpdateRepository(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.remote.DumpDiaryApi api, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.remote.SupabaseApi supabaseApi, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.ServerConfigRepository serverConfigRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object checkForUpdate(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.repository.AppUpdateUi> $completion) {
        return null;
    }
    
    private final java.lang.Object checkForRestUpdate(kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.repository.AppUpdateUi> $completion) {
        return null;
    }
    
    private final java.lang.Object checkForSupabaseUpdate(kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.repository.AppUpdateUi> $completion) {
        return null;
    }
}