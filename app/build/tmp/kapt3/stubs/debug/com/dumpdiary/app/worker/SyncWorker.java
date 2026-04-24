package com.dumpdiary.app.worker;

import android.content.Context;
import androidx.hilt.work.HiltWorker;
import androidx.work.CoroutineWorker;
import androidx.work.WorkerParameters;
import com.dumpdiary.app.data.repository.FriendRepository;
import com.dumpdiary.app.data.repository.LogRepository;
import com.dumpdiary.app.data.repository.ProfileRepository;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B3\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u000fR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/dumpdiary/app/worker/SyncWorker;", "Landroidx/work/CoroutineWorker;", "appContext", "Landroid/content/Context;", "params", "Landroidx/work/WorkerParameters;", "logRepository", "Lcom/dumpdiary/app/data/repository/LogRepository;", "profileRepository", "Lcom/dumpdiary/app/data/repository/ProfileRepository;", "friendRepository", "Lcom/dumpdiary/app/data/repository/FriendRepository;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;Lcom/dumpdiary/app/data/repository/LogRepository;Lcom/dumpdiary/app/data/repository/ProfileRepository;Lcom/dumpdiary/app/data/repository/FriendRepository;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.hilt.work.HiltWorker()
public final class SyncWorker extends androidx.work.CoroutineWorker {
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.LogRepository logRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.ProfileRepository profileRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.dumpdiary.app.data.repository.FriendRepository friendRepository = null;
    
    @dagger.assisted.AssistedInject()
    public SyncWorker(@dagger.assisted.Assisted()
    @org.jetbrains.annotations.NotNull()
    android.content.Context appContext, @dagger.assisted.Assisted()
    @org.jetbrains.annotations.NotNull()
    androidx.work.WorkerParameters params, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.LogRepository logRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.ProfileRepository profileRepository, @org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.repository.FriendRepository friendRepository) {
        super(null, null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object doWork(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
        return null;
    }
}