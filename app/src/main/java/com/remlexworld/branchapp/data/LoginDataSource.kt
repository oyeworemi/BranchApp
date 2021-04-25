package com.remlexworld.branchapp.data

import android.util.Log
import com.remlexworld.branchapp.model.LoginRequest
import com.remlexworld.branchapp.model.LoginResponse
import com.remlexworld.branchapp.model.Result
import com.remlexworld.branchapp.network.services.LoginService
import com.remlexworld.branchapp.util.ErrorUtils
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves auth token.
 */
class LoginDataSource @Inject constructor(private val retrofit: Retrofit) {


    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        val loginService = retrofit.create(LoginService::class.java)
        return getResponse(
            request = { loginService.login(loginRequest) },
            defaultErrorMessage = "Error login")

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