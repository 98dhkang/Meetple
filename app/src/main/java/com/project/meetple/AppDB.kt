package com.project.meetple

import android.content.Context
import androidx.room.*

@Database (entities = arrayOf(chat_room_db::class, chat_msg_db::class), version = 4)
abstract class AppDB : RoomDatabase() {
    abstract fun ChatDAO(): ChatDAO

    companion object{
        private var database: AppDB? = null
        private const val chatDB = "chat_db"

        fun getDatabase(context: Context) : AppDB{
            if(database==null){
                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java, chatDB
                ).build()
//.fallbackToDestructiveMigration()
            }
            return database!!
        }
    }
}