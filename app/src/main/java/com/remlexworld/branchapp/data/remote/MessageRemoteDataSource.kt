package com.remlexworld.branchapp.data.remote

import com.remlexworld.branchapp.model.Message
import com.remlexworld.branchapp.model.ReplyMessageRequest
import com.remlexworld.branchapp.model.Result
import com.remlexworld.branchapp.network.services.MessageService
import com.remlexworld.branchapp.util.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * fetches data from remote source
 */
class MessageRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {


    suspend fun fetchMessages(token: String): Result<List<Message>> {
        val messageService = retrofit.create(MessageService::class.java)
        return getResponse(
            request = { messageService.getMessages(token) },
            defaultErrorMessage = "Error fetching Message list")

    }

    suspend fun replyMessage(token: String, replyMessageRequest: ReplyMessageRequest): Result<Message> {
        val messageService = retrofit.create(MessageService::class.java)
        return getResponse(
                request = { messageService.replyMessage(token, replyMessageRequest) },
                defaultErrorMessage = "Error Replying Message")
    }


    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)

        }
    }
}