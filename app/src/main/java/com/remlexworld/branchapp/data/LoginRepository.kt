package com.remlexworld.branchapp.data

import com.remlexworld.branchapp.model.Result
import com.remlexworld.branchapp.model.LoginRequest
import com.remlexworld.branchapp.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


/**
 * Repository which fetches data from Remote data sources
 */
class LoginRepository @Inject constructor(
    private val loginDataSource: LoginDataSource

) {

    suspend fun login(loginRequest: LoginRequest): Flow<Result<LoginResponse>?> {
        return flow {
            emit(Result.loading())
            val result = loginDataSource.login(loginRequest)
            emit(result)

        }.flowOn(Dispatchers.IO)
    }


}
