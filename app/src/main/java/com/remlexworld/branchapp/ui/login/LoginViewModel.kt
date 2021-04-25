package com.remlexworld.branchapp.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.remlexworld.branchapp.data.LoginRepository
import com.remlexworld.branchapp.model.Result
import com.remlexworld.branchapp.model.LoginRequest
import com.remlexworld.branchapp.model.LoginResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


/**
 * ViewModel for Login screen
 */
class LoginViewModel @ViewModelInject constructor(private val loginRepository: LoginRepository) : ViewModel() {


    private val _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse = _loginResponse



    fun login(loginRequest: LoginRequest) {

        viewModelScope.launch {


            loginRepository.login(loginRequest).collect {
                _loginResponse.value = it
            }


        }

    }



}