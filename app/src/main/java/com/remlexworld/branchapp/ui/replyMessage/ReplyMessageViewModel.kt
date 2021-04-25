package com.remlexworld.branchapp.ui.replyMessage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.remlexworld.branchapp.data.MessageRepository
import com.remlexworld.branchapp.model.ReplyMessageRequest
import com.remlexworld.branchapp.model.Message
import kotlinx.coroutines.launch
import com.remlexworld.branchapp.model.Result
import kotlinx.coroutines.flow.collect



/**
 * ViewModel for Reply selected message screen
 */
class ReplyMessageViewModel @ViewModelInject constructor(private val messageRepository: MessageRepository) : ViewModel() {


    private val _messageList = MutableLiveData<Result<List<Message>>>()
    val messageList = _messageList

    private val _sentMessageResponse = MutableLiveData<Result<Message>>()
    val sentMessageResponse = _sentMessageResponse



    fun fetchSelectedMessages(selectedThreadId: Int) {
        viewModelScope.launch {

            messageRepository.fetchSelectedMessages(selectedThreadId).collect {
                _messageList.value = it
            }

        }
    }



    fun replyMessage(token : String, replyMessageRequest: ReplyMessageRequest) {

        viewModelScope.launch {

            messageRepository.replyMessage(token, replyMessageRequest).collect {
                _sentMessageResponse.value = it
            }


        }

    }

}