package com.margoslabs.messenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    
    private val TAG = "SettingsViewModel"
    
    // LiveData для темы (true = темная, false = светлая)
    private val _isDarkTheme = MutableLiveData<Boolean>(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme
    
    init {
        Log.d(TAG, "init: ViewModel создан")
    }
    
    /**
     * Обновить тему
     */
    fun setDarkTheme(isDark: Boolean) {
        Log.d(TAG, "setDarkTheme: Установка темы - темная: $isDark")
        _isDarkTheme.value = isDark
    }
    
    /**
     * Переключить тему
     */
    fun toggleTheme() {
        val currentValue = _isDarkTheme.value ?: false
        setDarkTheme(!currentValue)
    }
    
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ViewModel очищается")
    }
}

