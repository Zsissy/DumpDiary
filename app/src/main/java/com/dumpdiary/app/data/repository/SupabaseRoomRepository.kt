package com.dumpdiary.app.data.repository

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dumpdiary.app.data.local.LogDao
import com.dumpdiary.app.data.local.ProfileDao
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.model.BowelLogEntity
import com.dumpdiary.app.data.model.SupabaseBowelLog
import com.dumpdiary.app.data.model.SupabaseRoom
import com.dumpdiary.app.data.model.toBowelLogEntity
import com.dumpdiary.app.data.model.toSupabaseBowelLog
import com.dumpdiary.app.data.remote.SupabaseApi
import com.dumpdiary.app.worker.SupabaseSyncWorker
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

@Singleton
class SupabaseRoomRepository @Inject constructor(
    private val api: SupabaseApi,
    private val logDao: LogDao,
    private val profileDao: ProfileDao,
    private val preferencesRepository: UserPreferencesRepository,
    private val workManager: WorkManager,
) {
    val logsFlow: Flow<List<BowelLogEntity>> = logDao.observeActiveLogs()

    fun resolveRoomCode(userId: String, matchCode: String): String {
        return if (matchCode.isNotBlank()) "pair:$matchCode" else "user:$userId"
    }

    suspend fun refreshFromRemote() {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.userId.isBlank()) return

        val roomCode = resolveRoomCode(prefs.userId, prefs.matchCode)
        val rooms = runCatching {
            api.getRoom(roomCode = "eq.$roomCode")
        }.getOrNull() ?: return

        val room = rooms.firstOrNull() ?: return

        val entities = room.bowelLogs.map { it.toBowelLogEntity(prefs.userId) }
        if (entities.isNotEmpty()) {
            logDao.upsertAll(entities)
        }
    }

    suspend fun pushToRemote() {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.userId.isBlank()) return

        // First refresh to get latest remote state
        val roomCode = resolveRoomCode(prefs.userId, prefs.matchCode)
        val remoteLogs = runCatching {
            api.getRoom(roomCode = "eq.$roomCode")
        }.getOrNull()?.firstOrNull()?.bowelLogs ?: emptyList()

        val localLogs = logDao.getAllLogs()
            .filter { !it.isDeleted }
            .map { it.toSupabaseBowelLog() }

        // Merge: use local over remote by id, keep remote logs from other users
        val remoteById = remoteLogs.associateBy { it.id }
        val localById = localLogs.associateBy { it.id }
        val merged = remoteById.toMutableMap()
        merged.putAll(localById)

        val now = Clock.System.now().toString()
        val room = SupabaseRoom(
            roomCode = roomCode,
            bowelLogs = merged.values.toList(),
            updatedAt = now,
        )
        api.upsertRoom(room)
    }

    suspend fun createOrUpdate(input: BowelLogInput, userId: String) {
        val prefs = preferencesRepository.preferences.first()
        val profile = resolveProfile(prefs)
        val now = Clock.System.now().toString()
        val occurredAt = input.occurredAt
        val dateKey = occurredAt.take(10)
        val existing = input.id?.let { logDao.getById(it) }
        val entity = BowelLogEntity(
            id = input.id ?: UUID.randomUUID().toString(),
            userId = userId,
            occurredAt = occurredAt,
            dateKey = dateKey,
            durationSeconds = input.durationSeconds,
            feeling = input.feeling,
            stoolForm = input.stoolForm,
            symptomTagsRaw = input.symptomTags.distinct().joinToString("|"),
            detailNote = input.detailNote,
            snapshotDisplayName = profile.displayName,
            snapshotAvatarUrl = profile.avatarUrl,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now,
            isDeleted = false,
            pendingSyncAction = null,
        )
        logDao.upsert(entity)
        enqueueSync()
    }

    suspend fun markDeleted(id: String) {
        val current = logDao.getById(id) ?: return
        logDao.upsert(current.copy(isDeleted = true, updatedAt = Clock.System.now().toString()))
        enqueueSync()
    }

    private suspend fun resolveProfile(prefs: com.dumpdiary.app.data.local.UserPreferences): com.dumpdiary.app.data.model.UserProfileEntity {
        profileDao.getProfile()?.takeIf { it.userId == prefs.userId }?.let { return it }
        val fallbackName = prefs.username.ifBlank { prefs.userId }
        val entity = com.dumpdiary.app.data.model.UserProfileEntity(
            userId = prefs.userId,
            email = prefs.username,
            displayName = fallbackName,
            avatarUrl = null,
            updatedAt = Clock.System.now().toString(),
        )
        profileDao.upsert(entity)
        return entity
    }

    private fun enqueueSync() {
        workManager.enqueueUniqueWork(
            SUPABASE_SYNC_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<SupabaseSyncWorker>().build(),
        )
    }

    companion object {
        const val SUPABASE_SYNC_WORK_NAME = "dump-diary-supabase-sync"
    }
}
