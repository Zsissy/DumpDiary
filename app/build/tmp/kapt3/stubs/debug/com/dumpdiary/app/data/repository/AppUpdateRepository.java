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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0086@\u00a2\u0006\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/dumpdiary/app/data/repository/AppUpdateRepository;", "", "api", "Lcom/dumpdiary/app/data/remote/DumpDiaryApi;", "(Lcom/dumpdiary/app/data/remote/DumpDiaryApi;)V", "checkForUpdate", "Lcom/dumpdiary/app/data/repository/AppUpdateUi;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class AppUpdateRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.remote.DumpDiaryApi api = null;
    
    @javax.inject.Inject()
    public AppUpdateRepository(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.remote.DumpDiaryApi api) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object checkForUpdate(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.repository.AppUpdateUi> $completion) {
        return null;
    }
}