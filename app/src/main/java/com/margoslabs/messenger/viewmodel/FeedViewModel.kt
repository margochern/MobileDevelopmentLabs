package com.margoslabs.messenger.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.margoslabs.messenger.data.entity.MessageEntity
import com.margoslabs.messenger.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = MessageRepository(application)
    
    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        try {
            loadMessages()
            observeMessages()
        } catch (e: Exception) {
            android.util.Log.e("FeedViewModel", "Error in init", e)
            _error.value = "Ошибка инициализации: ${e.message}"
        }
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
            try {
                // Подписываемся на Flow из базы данных
                repository.getMessages()
                    .catch { e ->
                        android.util.Log.e("FeedViewModel", "Error observing messages", e)
                        _error.value = e.message ?: "Ошибка загрузки данных"
                    }
                    .collect { messageList ->
                        _messages.value = messageList
                    }
            } catch (e: Exception) {
                android.util.Log.e("FeedViewModel", "Error in observeMessages", e)
                _error.value = "Ошибка наблюдения за сообщениями: ${e.message}"
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
                }
        }
    }
}

