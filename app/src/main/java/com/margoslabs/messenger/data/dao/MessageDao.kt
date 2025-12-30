package com.margoslabs.messenger.data.dao

import androidx.room.*
import com.margoslabs.messenger.data.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    suspend fun getAllMessagesSync(): List<MessageEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)
    
    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
    
    @Query("SELECT COUNT(*) FROM messages")
    suspend fun getMessageCount(): Int
}

