package com.remlexworld.branchapp.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Message(
    @NonNull
    @PrimaryKey
    val id: Int ,
    val thread_id: Int ,
    val user_id: String = "",
    val agent_id: String? = "",
    val body: String = "",
    val timestamp: String = ""
) : Serializable
