package com.remlexworld.branchapp.data

import com.remlexworld.branchapp.data.MockWebServerResponses.messageListResponse
import com.remlexworld.branchapp.data.MockWebServerResponses.messageWithThreadId40
import com.remlexworld.branchapp.data.local.AppDatabaseFake
import com.remlexworld.branchapp.data.local.MessageDaoFake
import com.remlexworld.branchapp.data.remote.MessageRemoteDataSource
import com.remlexworld.branchapp.model.Message
import com.remlexworld.branchapp.model.ReplyMessageRequest
import com.remlexworld.branchapp.model.Result
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class MessageRepositoryTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val DUMMY_TOKEN = "123456" // can be anything
    private val DUMMY_THREADID = 40
    private val DUMMY_BODY = "This a random message"


    // system in test
    private lateinit var messageRepository: MessageRepository

    // Dependencies
    private lateinit var messageDao: MessageDaoFake
    private lateinit var messageRemoteDataSource: MessageRemoteDataSource
    private lateinit var retrofit: Retrofit
    private lateinit var replyMessageRequest: ReplyMessageRequest


    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/")

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        messageDao = MessageDaoFake(appDatabaseFake = appDatabase)

        messageRemoteDataSource = MessageRemoteDataSource(retrofit)

        // instantiate the system in test
        messageRepository = MessageRepository(
                messageRemoteDataSource,
                  messageDao
        )

        replyMessageRequest = ReplyMessageRequest(DUMMY_THREADID, DUMMY_BODY)
    }


    /**
     * 1. Are the messages retrieved from the network?
     * 2. Are the messages inserted into the cache?
     * 3. Are the messages then emitted as a flow?
     */
    @Test
    fun fetchMessagesFromNetwork_emitMessages(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
                MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(messageListResponse)
        )

        // confirm the cache is empty to start
        messageDao.getAll()?.isEmpty()?.let { assert(it) }

        val flowItems = messageRepository.fetchMessages(DUMMY_TOKEN).toList()

        // confirm the cache is no longer empty
        messageDao.getAll()?.isNotEmpty()?.let { assert(it) }

        // first emission should be `loading`
        assert(flowItems[0] == Result.loading(null))

        // second emission should be the list of messages
        val messages = flowItems[1]?.data
        assert(messages?.size ?: 0 > 0)

        // confirm they are actually Message objects
        assert(messages?.get(index = 0) is Message)

        assert(flowItems[1] != Result.loading(null)) //loading should be false now

    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun replyMessageThread_emitMessage(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
                MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(messageWithThreadId40)
        )


        val flowItems = messageRepository.replyMessage(DUMMY_TOKEN, replyMessageRequest).toList()


        // first emission should be `loading`
        assert(flowItems[0] == Result.loading(null))

        // second emission should be the message details
        val message = flowItems[1]?.data

        //confirm message thread Id
        assert(message?.thread_id == DUMMY_THREADID)

        // confirm response is a Message object
        assert(message is Message)

        assert(flowItems[1] != Result.loading(null)) //loading should be false now

    }




}