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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u000e\u0010\u0014\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\u0016R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/dumpdiary/app/data/repository/FriendRepository;", "", "api", "Lcom/dumpdiary/app/data/remote/DumpDiaryApi;", "preferencesRepository", "Lcom/dumpdiary/app/data/local/UserPreferencesRepository;", "(Lcom/dumpdiary/app/data/remote/DumpDiaryApi;Lcom/dumpdiary/app/data/local/UserPreferencesRepository;)V", "_friendsFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/dumpdiary/app/data/repository/FriendUi;", "friendsFlow", "Lkotlinx/coroutines/flow/Flow;", "getFriendsFlow", "()Lkotlinx/coroutines/flow/Flow;", "addFriend", "Lcom/dumpdiary/app/data/model/MessageDto;", "email", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshFriends", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class FriendRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.remote.DumpDiaryApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.dumpdiary.app.data.repository.FriendUi>> _friendsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.dumpdiary.app.data.repository.FriendUi>> friendsFlow = null;
    
    @javax.inject.Inject()
    public FriendRepository(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.remote.DumpDiaryApi api, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.local.UserPreferencesRepository preferencesRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.dumpdiary.app.data.repository.FriendUi>> getFriendsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refreshFriends(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addFriend(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.MessageDto> $completion) {
        return null;
    }
}