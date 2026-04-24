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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\b\u001a\u0004\u0018\u00010\u00072\u0006\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u000eH\'J\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\u00020\u00032\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/dumpdiary/app/data/local/LogDao;", "", "clear", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllLogs", "", "Lcom/dumpdiary/app/data/model/BowelLogEntity;", "getById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPendingSyncLogs", "observeActiveLogs", "Lkotlinx/coroutines/flow/Flow;", "upsert", "log", "(Lcom/dumpdiary/app/data/model/BowelLogEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertAll", "logs", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface LogDao {
    
    @androidx.room.Query(value = "SELECT * FROM bowel_logs WHERE isDeleted = 0 ORDER BY occurredAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dumpdiary.app.data.model.BowelLogEntity>> observeActiveLogs();
    
    @androidx.room.Query(value = "SELECT * FROM bowel_logs ORDER BY occurredAt DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllLogs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dumpdiary.app.data.model.BowelLogEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM bowel_logs WHERE pendingSyncAction IS NOT NULL ORDER BY updatedAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPendingSyncLogs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dumpdiary.app.data.model.BowelLogEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM bowel_logs WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dumpdiary.app.data.model.BowelLogEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.dumpdiary.app.data.model.BowelLogEntity log, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dumpdiary.app.data.model.BowelLogEntity> logs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM bowel_logs")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clear(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}