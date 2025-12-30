package com.margoslabs.messenger.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.margoslabs.messenger.data.repository.MessageRepository
import com.margoslabs.messenger.notification.NotificationHelper

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val repository = MessageRepository(context)
    private val notificationHelper = NotificationHelper(context)
    private val TAG = "SyncWorker"
    
    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Starting background sync")
            val result = repository.refreshMessages()
            
            if (result.isSuccess) {
                Log.d(TAG, "Background sync successful")
                // Показываем уведомление об успешной синхронизации
                notificationHelper.showSyncSuccessNotification()
                Result.success()
            } else {
                Log.e(TAG, "Background sync failed: ${result.exceptionOrNull()?.message}")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in background sync", e)
            Result.failure()
        }
    }
}

