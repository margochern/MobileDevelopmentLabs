package com.margoslabs.messenger.data.preferences

import android.content.Context
import android.content.SharedPreferences

class AvatarPreferences(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "avatar_preferences",
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_AVATAR_PREFIX = "avatar_user_"
        private const val KEY_DEFAULT_AVATAR = "default_avatar"
    }
    
    /**
     * Сохранить выбранный аватар для пользователя
     */
    fun saveAvatar(userId: Int, avatarType: AvatarType) {
        prefs.edit()
            .putString("$KEY_AVATAR_PREFIX$userId", avatarType.name)
            .apply()
    }
    
    /**
     * Получить аватар для пользователя
     */
    fun getAvatar(userId: Int): AvatarType {
        val avatarName = prefs.getString("$KEY_AVATAR_PREFIX$userId", null)
        return if (avatarName != null) {
            try {
                AvatarType.valueOf(avatarName)
            } catch (e: IllegalArgumentException) {
                AvatarType.getDefaultForUser(userId)
            }
        } else {
            AvatarType.getDefaultForUser(userId)
        }
    }
    
    /**
     * Сохранить аватар текущего пользователя (для профиля)
     */
    fun saveCurrentUserAvatar(avatarType: AvatarType) {
        prefs.edit()
            .putString(KEY_DEFAULT_AVATAR, avatarType.name)
            .apply()
    }
    
    /**
     * Получить аватар текущего пользователя
     */
    fun getCurrentUserAvatar(): AvatarType {
        val avatarName = prefs.getString(KEY_DEFAULT_AVATAR, null)
        return if (avatarName != null) {
            try {
                AvatarType.valueOf(avatarName)
            } catch (e: IllegalArgumentException) {
                AvatarType.DEFAULT_1
            }
        } else {
            AvatarType.DEFAULT_1
        }
    }
}

enum class AvatarType(val colorHex: String, val displayName: String) {
    DEFAULT_1("#FF6B6B", "Красный"),
    DEFAULT_2("#4ECDC4", "Бирюзовый"),
    DEFAULT_3("#45B7D1", "Голубой"),
    DEFAULT_4("#FFA07A", "Персиковый"),
    DEFAULT_5("#98D8C8", "Мятный"),
    DEFAULT_6("#F7DC6F", "Желтый"),
    DEFAULT_7("#BB8FCE", "Фиолетовый"),
    DEFAULT_8("#85C1E2", "Небесный");
    
    companion object {
        fun getDefaultForUser(userId: Int): AvatarType {
            val types = values()
            return types[userId % types.size]
        }
        
        fun getAllTypes(): List<AvatarType> = values().toList()
    }
}

