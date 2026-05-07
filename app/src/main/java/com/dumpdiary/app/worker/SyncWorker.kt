package com.dumpdiary.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.repository.FriendRepository
import com.dumpdiary.app.data.repository.LogRepository
import com.dumpdiary.app.data.repository.ProfileRepository
import com.dumpdiary.app.data.repository.SupabaseRoomRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val logRepository: LogRepository,
    private val profileRepository: ProfileRepository,
    private val friendRepository: FriendRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val supabaseRoomRepository: SupabaseRoomRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result =
        runCatching {
            val prefs = preferencesRepository.preferences.first()
            if (prefs.serverType == "supabase") {
                supabaseRoomRepository.refreshFromRemote()
                supabaseRoomRepository.pushToRemote()
            } else {
                profileRepository.refreshProfile()
                friendRepository.refreshFriends()
                logRepository.syncPendingChanges()
                logRepository.refreshFromRemote()
            }
            Result.success()
        }.getOrElse { Result.retry() }
}
