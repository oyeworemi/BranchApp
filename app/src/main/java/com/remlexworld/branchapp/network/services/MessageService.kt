package com.remlexworld.branchapp.network.services

import com.remlexworld.branchapp.model.ReplyMessageRequest
import com.remlexworld.branchapp.util.Config
import com.remlexworld.branchapp.model.Message
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API Service
 */
interface MessageService {


    @Headers("Content-Type: application/json")
    @GET(Config.MESSAGES_URL)
    suspend fun getMessages(
        @Header("X-Branch-Auth-Token") token: String
    ) : Response<List<Message>>

    @Headers("Content-Type: application/json")
    @POST(Config.REPLY_MESSAGE_URL)
    suspend fun replyMessage(
        @Header("X-Branch-Auth-Token") token: String,
        @Body request: ReplyMessageRequest
    ) : Response<Message>
}