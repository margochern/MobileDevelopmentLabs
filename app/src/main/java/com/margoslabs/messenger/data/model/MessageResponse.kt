package com.margoslabs.messenger.data.model

import com.google.gson.annotations.SerializedName
import com.margoslabs.messenger.data.entity.MessageEntity

data class MessageResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("userId")
    val userId: Int
) {
    fun toEntity(): MessageEntity {
        return MessageEntity(
            id = id,
            title = title,
            body = body,
            userId = userId,
            userName = "Пользователь #$userId"
        )
    }
}

