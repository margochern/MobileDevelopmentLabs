package com.margoslabs.messenger.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.margoslabs.messenger.data.entity.MessageEntity
import com.margoslabs.messenger.data.repository.MessageRepository
import com.margoslabs.messenger.network.NetworkMonitor
import com.margoslabs.messenger.notification.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = MessageRepository(application)
    private val networkMonitor = NetworkMonitor(application)
    private val notificationHelper = NotificationHelper(application)
    
    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _isOnline = MutableStateFlow(networkMonitor.isNetworkAvailable())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()
    
    init {
        loadMessages()
        observeMessages()
        observeNetworkState()
    }
    
    private fun observeNetworkState() {
        networkMonitor.isOnline
            .onEach { isOnline ->
                _isOnline.value = isOnline
                if (isOnline) {
                    // При восстановлении сети автоматически обновляем сообщения
                    loadMessages()
                }
            }
            .launchIn(viewModelScope)
    }
    
    private fun loadMessages() {
        viewModelScope.launch {
            _isLoading.value = true
            // Пытаемся загрузить из API
            repository.loadMessagesFromApi()
                .onFailure { e ->
                    _error.value = e.message
                    _isLoading.value = false
                }
                .onSuccess {
                    _isLoading.value = false
                    _error.value = null
                }
        }
    }
    
    private fun observeMessages() {
        viewModelScope.launch {
            // Подписываемся на Flow из базы данных
            repository.getMessages()
                .catch { e ->
                    _error.value = e.message
                }
                .collect { messageList ->
                    _messages.value = messageList
                }
        }
    }
    
    fun refreshMessages() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.refreshMessages()
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка обновления"
                    _isLoading.value = false
                }
                .onSuccess {
                    _isLoading.value = false
                    // Показываем уведомление об успешном обновлении
                    notificationHelper.showSyncSuccessNotification()
                }
        }
    }
    
    fun toggleLike(messageId: Int) {
        viewModelScope.launch {
            repository.toggleLike(messageId)
                .onFailure { e ->
                    _error.value = e.message ?: "Ошибка при изменении лайка"
                }
        }
    }
}

