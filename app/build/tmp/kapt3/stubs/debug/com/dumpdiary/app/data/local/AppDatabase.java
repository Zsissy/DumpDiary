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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/dumpdiary/app/data/local/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "logDao", "Lcom/dumpdiary/app/data/local/LogDao;", "profileDao", "Lcom/dumpdiary/app/data/local/ProfileDao;", "app_debug"})
@androidx.room.Database(entities = {com.dumpdiary.app.data.model.UserProfileEntity.class, com.dumpdiary.app.data.model.BowelLogEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dumpdiary.app.data.local.ProfileDao profileDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.dumpdiary.app.data.local.LogDao logDao();
}