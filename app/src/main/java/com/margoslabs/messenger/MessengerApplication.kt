package com.margoslabs.messenger

import android.app.Application
import com.margoslabs.messenger.worker.SyncWorkManager

class MessengerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Запускаем периодическую синхронизацию
        SyncWorkManager.scheduleSync(this)
    }
}

