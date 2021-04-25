package com.remlexworld.branchapp.ui.messages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remlexworld.branchapp.data.MessageRepository
import com.remlexworld.branchapp.model.Message
import com.remlexworld.branchapp.model.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

/**
 * ViewModel for MessagesActivity
 */
class MessagesViewModel @ViewModelInject constructor(private val messageRepository: MessageRepository) :
        ViewModel() {

    private val _messageList = MutableLiveData<Result<List<Message>>>()
    val messageList = _messageList


     fun fetchMessages(token: String) {
        viewModelScope.launch {

            messageRepository.fetchMessages(token).collect {
                _messageList.value = it
            }

        }
    }


}