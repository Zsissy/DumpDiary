package com.dumpdiary.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dumpdiary.app.data.repository.FriendRepository
import com.dumpdiary.app.data.repository.LogRepository
import com.dumpdiary.app.data.repository.ProfileRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val logRepository: LogRepository,
    private val profileRepository: ProfileRepository,
    private val friendRepository: FriendRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result =
        runCatching {
            profileRepository.refreshProfile()
            friendRepository.refreshFriends()
            logRepository.syncPendingChanges()
            logRepository.refreshFromRemote()
            Result.success()
        }.getOrElse { Result.retry() }
}
