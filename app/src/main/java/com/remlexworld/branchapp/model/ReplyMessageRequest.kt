package com.remlexworld.branchapp.model

import com.google.gson.annotations.SerializedName

data class ReplyMessageRequest (
    @SerializedName("thread_id")
    var threadId: Int,

    @SerializedName("body")
    var body: String
)