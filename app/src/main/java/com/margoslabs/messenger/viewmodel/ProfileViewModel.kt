package com.margoslabs.messenger.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.margoslabs.messenger.data.preferences.AvatarPreferences
import com.margoslabs.messenger.data.preferences.AvatarType

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val TAG = "ProfileViewModel"
    private val avatarPreferences = AvatarPreferences(application)
    
    // LiveData для имени пользователя
    private val _userName = MutableLiveData<String>("Пользователь")
    val userName: LiveData<String> = _userName
    
    // LiveData для статуса пользователя
    private val _userStatus = MutableLiveData<String>("В сети")
    val userStatus: LiveData<String> = _userStatus
    
    // LiveData для аватара
    private val _selectedAvatar = MutableLiveData<AvatarType>()
    val selectedAvatar: LiveData<AvatarType> = _selectedAvatar
    
    init {
        Log.d(TAG, "init: ViewModel создан")
        // Загружаем сохраненный аватар
        _selectedAvatar.value = avatarPreferences.getCurrentUserAvatar()
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
    
    /**
     * Обновить аватар пользователя
     */
    fun updateAvatar(avatarType: AvatarType) {
        Log.d(TAG, "updateAvatar: Обновление аватара на '${avatarType.displayName}'")
        avatarPreferences.saveCurrentUserAvatar(avatarType)
        _selectedAvatar.value = avatarType
    }
    
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ViewModel очищается")
    }
}

