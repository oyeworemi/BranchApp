package com.remlexworld.branchapp.data

import com.remlexworld.branchapp.model.ReplyMessageRequest
import com.remlexworld.branchapp.model.Result
import com.remlexworld.branchapp.data.local.MessageDao
import com.remlexworld.branchapp.model.Message
import com.remlexworld.branchapp.data.remote.MessageRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Repository which fetches data from Remote or Local data sources
 */
class MessageRepository @Inject constructor(
        private val messageRemoteDataSource: MessageRemoteDataSource,
        private val messageDao: MessageDao
) {


    suspend fun fetchMessages(token: String): Flow<Result<List<Message>>?> {
        return flow {
            emit(Result.loading())
            val result = messageRemoteDataSource.fetchMessages(token)


            //Cache to database if response is successful
            if (result.status == Result.Status.SUCCESS) {
                result.data?.let { it ->
                    messageDao.deleteAll(it)
                    messageDao.insertAll(it)
                }
            }
            emit(fetchMessagesCached())


        }.flowOn(Dispatchers.IO)
    }


    private fun fetchMessagesCached(): Result<List<Message>>? =
            messageDao.getAll()?.let {
                Result.success((it))
            }


    private fun fetchSelectedMessagesCached(selectedThreadId: Int): Result<List<Message>>? =
            messageDao.getSelectedMessages(selectedThreadId)?.let {
                Result.success((it))
            }



    suspend fun replyMessage(token: String, replyMessageRequest: ReplyMessageRequest): Flow<Result<Message>> {
        return flow {
            emit(Result.loading())
            emit(messageRemoteDataSource.replyMessage(token, replyMessageRequest))
        }.flowOn(Dispatchers.IO)
    }


    suspend fun fetchSelectedMessages(selectedThreadId: Int): Flow<Result<List<Message>>?> {
        return flow {
            emit(Result.loading())

            emit(fetchSelectedMessagesCached(selectedThreadId))

        }.flowOn(Dispatchers.IO)
    }

}