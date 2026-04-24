package com.dumpdiary.app.data.local;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import com.dumpdiary.app.data.model.BowelLogEntity;
import com.dumpdiary.app.data.model.UserProfileEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\bH\'J\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/dumpdiary/app/data/local/ProfileDao;", "", "clear", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProfile", "Lcom/dumpdiary/app/data/model/UserProfileEntity;", "observeProfile", "Lkotlinx/coroutines/flow/Flow;", "upsert", "profile", "(Lcom/dumpdiary/app/data/model/UserProfileEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface ProfileDao {
    
    @androidx.room.Query(value = "SELECT * FROM profile LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.dumpdiary.app.data.model.UserProfileEntity> observeProfile();
    
    @androidx.room.Query(value = "SELECT * FROM profile LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getProfile(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.UserProfileEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.UserProfileEntity profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM profile")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clear(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}