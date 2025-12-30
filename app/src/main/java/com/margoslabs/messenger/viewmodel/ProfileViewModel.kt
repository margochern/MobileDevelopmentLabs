package com.margoslabs.messenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    
    private val TAG = "ProfileViewModel"
    
    // LiveData для имени пользователя
    private val _userName = MutableLiveData<String>("Пользователь")
    val userName: LiveData<String> = _userName
    
    // LiveData для статуса пользователя
    private val _userStatus = MutableLiveData<String>("В сети")
    val userStatus: LiveData<String> = _userStatus
    
    init {
        Log.d(TAG, "init: ViewModel создан")
    }
    
    /**
     * Обновить имя пользователя
     */
    fun updateUserName(name: String) {
        Log.d(TAG, "updateUserName: Обновление имени на '$name'")
        _userName.value = name
    }
    
    /**
     * Обновить статус пользователя
     */
    fun updateUserStatus(status: String) {
        Log.d(TAG, "updateUserStatus: Обновление статуса на '$status'")
        _userStatus.value = status
    }
    
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ViewModel очищается")
    }
}

