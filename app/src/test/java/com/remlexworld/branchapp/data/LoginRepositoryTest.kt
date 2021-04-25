package com.remlexworld.branchapp.data

import com.remlexworld.branchapp.data.MockWebServerResponses.loginResponse
import com.remlexworld.branchapp.data.MockWebServerResponses.messageWithThreadId40
import com.remlexworld.branchapp.data.local.AppDatabaseFake
import com.remlexworld.branchapp.data.local.MessageDaoFake
import com.remlexworld.branchapp.data.remote.MessageRemoteDataSource
import com.remlexworld.branchapp.model.*
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

class LoginRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val DUMMY_EMAIL = "remmyoyewo@gmail.com"
    private val DUMMY_PASSWORD = "moc.liamg@oweyoymmer"


    // system in test
    private lateinit var loginRepository: LoginRepository

    // Dependencies
    private lateinit var loginDataSource: LoginDataSource
    private lateinit var retrofit: Retrofit
    private lateinit var loginRequest: LoginRequest


    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/")

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        loginDataSource = LoginDataSource(retrofit)

        // instantiate the system in test
        loginRepository = LoginRepository(
                loginDataSource
        )

        loginRequest = LoginRequest(DUMMY_EMAIL, DUMMY_PASSWORD)
    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun fetchUserInfoFromNetwork_emitAuthToken(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
                MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(loginResponse)
        )

        val flowItems = loginRepository.login(loginRequest).toList()


        // first emission should be `loading`
        assert(flowItems[0] == Result.loading(null))

        // second emission should be the loginResponse details
        val loginResponse = flowItems[1]?.data

        //confirm loginResponse token is not empty or null
        loginResponse?.authToken?.isNotEmpty()?.let { assert(it) }

        // confirm response is a loginResponse object
        assert(loginResponse is LoginResponse)

        assert(flowItems[1] != Result.loading(null)) //loading should be false now

    }


}