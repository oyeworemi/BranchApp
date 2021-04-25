package com.remlexworld.branchapp.network.services

import com.remlexworld.branchapp.util.Config
import com.remlexworld.branchapp.model.LoginRequest
import com.remlexworld.branchapp.model.LoginResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API Service
 */
interface LoginService {


    @Headers("Content-Type: application/json")
    @POST(Config.LOGIN_URL)
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


}