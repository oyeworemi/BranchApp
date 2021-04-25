package com.remlexworld.branchapp.data.local

import com.remlexworld.branchapp.model.Message

class MessageDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): MessageDao {


    override fun getAll(): List<Message>? {
        return appDatabaseFake.messages // return the entire list for simplicity

    }

    override fun insertAll(movies: List<Message>) {
        appDatabaseFake.messages.addAll(movies)

    }

    override fun delete(message: Message) {

    }

    override fun deleteAll(message: List<Message>) {
        appDatabaseFake.messages.clear()
    }

    override fun getSelectedMessages(search: Int?): List<Message> {
        return appDatabaseFake.messages
    }
}

















