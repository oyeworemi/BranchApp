package com.remlexworld.branchapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.remlexworld.branchapp.data.MessageConverters
import com.remlexworld.branchapp.model.Message

@Database(entities = [Message::class], version = 1)
@TypeConverters(MessageConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}