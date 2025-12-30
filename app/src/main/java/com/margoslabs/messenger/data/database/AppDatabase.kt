package com.margoslabs.messenger.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.margoslabs.messenger.data.dao.MessageDao
import com.margoslabs.messenger.data.entity.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun messageDao(): MessageDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "messenger_database"
                )
                    .fallbackToDestructiveMigration() // Для разработки - пересоздает БД при изменении схемы
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

