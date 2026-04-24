package com.dumpdiary.app.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.dumpdiary.app.data.model.BowelLogEntity
import com.dumpdiary.app.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile LIMIT 1")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM profile LIMIT 1")
    suspend fun getProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)

    @Query("DELETE FROM profile")
    suspend fun clear()
}

@Dao
interface LogDao {
    @Query("SELECT * FROM bowel_logs WHERE isDeleted = 0 ORDER BY occurredAt DESC")
    fun observeActiveLogs(): Flow<List<BowelLogEntity>>

    @Query("SELECT * FROM bowel_logs ORDER BY occurredAt DESC")
    suspend fun getAllLogs(): List<BowelLogEntity>

    @Query("SELECT * FROM bowel_logs WHERE pendingSyncAction IS NOT NULL ORDER BY updatedAt ASC")
    suspend fun getPendingSyncLogs(): List<BowelLogEntity>

    @Query("SELECT * FROM bowel_logs WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): BowelLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(log: BowelLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(logs: List<BowelLogEntity>)

    @Query("DELETE FROM bowel_logs")
    suspend fun clear()
}

@Database(
    entities = [UserProfileEntity::class, BowelLogEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun logDao(): LogDao
}
