package com.dumpdiary.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dumpdiary.app.data.repository.SupabaseRoomRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SupabaseSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val roomRepository: SupabaseRoomRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result =
        runCatching {
            roomRepository.refreshFromRemote()
            roomRepository.pushToRemote()
            Result.success()
        }.getOrElse { Result.retry() }
}
