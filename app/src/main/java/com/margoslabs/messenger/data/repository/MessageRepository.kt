package com.margoslabs.messenger.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.margoslabs.messenger.data.api.RetrofitClient
import com.margoslabs.messenger.data.dao.MessageDao
import com.margoslabs.messenger.data.database.AppDatabase
import com.margoslabs.messenger.data.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

class MessageRepository(context: Context) {
    
    private val messageApi = RetrofitClient.messageApi
    private val messageDao: MessageDao = try {
        AppDatabase.getDatabase(context).messageDao()
    } catch (e: Exception) {
        Log.e("MessageRepository", "Error initializing database", e)
        throw e
    }
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    private val TAG = "MessageRepository"
    
    fun getMessages(): Flow<List<MessageEntity>> {
        return messageDao.getAllMessages()
    }
    
    suspend fun loadMessagesFromApi(): Result<Unit> {
        return try {
            if (isNetworkAvailable()) {
                Log.d(TAG, "Network available, loading from API")
                // Загружаем из API
                val messages = messageApi.getMessages().map { it.toEntity() }
                // Сохраняем в базу
                messageDao.deleteAllMessages()
                messageDao.insertMessages(messages)
                Log.d(TAG, "Messages loaded from API and saved to database: ${messages.size}")
                Result.success(Unit)
            } else {
                Log.d(TAG, "Network unavailable, loading from database")
                // Если нет сети, проверяем есть ли данные в базе
                val count = messageDao.getMessageCount()
                if (count > 0) {
                    Log.d(TAG, "Using cached messages from database: $count")
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Network unavailable and no cached data"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading messages from API", e)
            // При ошибке проверяем есть ли данные в базе
            val count = messageDao.getMessageCount()
            if (count > 0) {
                Log.d(TAG, "Error loading from API, using cached messages: $count")
                Result.success(Unit)
            } else {
                Result.failure(e)
            }
        }
    }
    
    suspend fun refreshMessages(): Result<Unit> {
        return try {
            if (isNetworkAvailable()) {
                Log.d(TAG, "Refreshing messages from API")
                val messages = messageApi.getMessages().map { it.toEntity() }
                messageDao.deleteAllMessages()
                messageDao.insertMessages(messages)
                Log.d(TAG, "Messages refreshed: ${messages.size}")
                Result.success(Unit)
            } else {
                Log.d(TAG, "Cannot refresh: network unavailable")
                Result.failure(Exception("Network unavailable"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing messages", e)
            Result.failure(e)
        }
    }
    
    private fun isNetworkAvailable(): Boolean {
        return try {
            val network = connectivityManager?.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (e: Exception) {
            Log.e(TAG, "Error checking network availability", e)
            false
        }
    }
}

