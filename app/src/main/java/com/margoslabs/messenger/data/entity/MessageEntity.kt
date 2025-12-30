package com.margoslabs.messenger.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    val timestamp: Long = System.currentTimeMillis()
)

