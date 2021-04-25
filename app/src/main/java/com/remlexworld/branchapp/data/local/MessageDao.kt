package com.remlexworld.branchapp.data.local

import androidx.room.*
import com.remlexworld.branchapp.model.Message

@Dao
interface MessageDao {

    @Query("SELECT * FROM message order by timestamp DESC")
    fun getAll(): List<Message>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<Message>)

    @Delete
    fun delete(message: Message)

    @Delete
    fun deleteAll(message: List<Message>)

    @Query("SELECT * FROM message WHERE thread_id LIKE '%' || :search || '%'")
    fun getSelectedMessages(search: Int?): List<Message>
}