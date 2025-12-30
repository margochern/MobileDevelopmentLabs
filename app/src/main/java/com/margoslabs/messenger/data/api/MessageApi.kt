package com.margoslabs.messenger.data.api

import com.margoslabs.messenger.data.model.MessageResponse
import retrofit2.http.GET

interface MessageApi {
    
    @GET("posts")
    suspend fun getMessages(): List<MessageResponse>
}

