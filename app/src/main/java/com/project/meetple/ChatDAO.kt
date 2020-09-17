package com.project.meetple

import androidx.room.*
import javax.sql.DataSource

@Dao
interface ChatDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatRoom(vararg chatRoom:chat_room_db)

    @Query("SELECT * FROM chat_room_db")
    fun selectChatRoom() : List<chat_room_db>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatMsg(vararg chatMsg:chat_msg_db)

    @Query("SELECT * FROM chat_msg_db WHERE roomNumber = :roomNumber")
    fun selectChatMsg(roomNumber:String) : List<chat_msg_db>
}

@Entity(tableName = "chat_room_db")
data class chat_room_db(
    @PrimaryKey (autoGenerate = true)
    var id:Int,
    var roomName:String,
    var title:String,
    var last_msg:String,
    var last_msg_time:String
)

@Entity(tableName = "chat_msg_db")
data class chat_msg_db(
    @PrimaryKey (autoGenerate = true)
    var num:Int? = null,
    var roomNumber:Int,
    var userName:String,
    var content:String,
    var time:String
)
