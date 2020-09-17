package com.project.meetple

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.recycler_chat_msg.view.*

class TestActivity : AppCompatActivity(), View.OnClickListener {

    val chatList: ArrayList<TestActivity.Message> = arrayListOf()

    data class Message(
        var roomName:String,
        var content:String,
        var userName:String,
        var phoneNumber:String,
        var time:String
    )
    data class joinInfo(
        var roomName:String,
        var phoneNumber:String
    )

    lateinit var chatMsgAdapter : chatMsgRecyclerAdapter
    lateinit var mSocket: Socket

    val gson: Gson =Gson()
    inner class chatMsgRecyclerAdapter: RecyclerView.Adapter<chatMsgRecyclerAdapter.itemViewHolder>(){
        override fun getItemCount(): Int {
            return chatList!!.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_chat_msg, parent,false)
            return itemViewHolder(view)
        }

        override fun onBindViewHolder(holder:itemViewHolder, position:Int) {
            holder.bindMsg()
        }

        inner class itemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bindMsg(){
                itemView.chat_msg_name.text=chatList[position].userName
                itemView.chat_msg_contetnt.text=chatList[position].content
                itemView.chat_msg_time.text=chatList[position].time
                if(chatList[position].userName.equals(gettString(itemView.context, "chatName"))){
                    itemView.chat_msg_contetnt.setBackgroundResource(R.drawable.chatbox_man_mine)
                    itemView.chat_msg.gravity=Gravity.RIGHT
                    itemView.left_View.visibility=View.VISIBLE
                    itemView.right_View.visibility=View.GONE
                    itemView.chat_msg_name.visibility=View.GONE
                    itemView.chat_msg_img.visibility=View.GONE
                }else{
                    itemView.chat_msg_contetnt.setBackgroundResource(R.drawable.chatbox_woman_other)
                    itemView.chat_msg.gravity=Gravity.LEFT
                    itemView.left_View.visibility=View.GONE
                    itemView.right_View.visibility=View.VISIBLE
                    itemView.chat_msg_name.visibility=View.VISIBLE
                    itemView.chat_msg_img.visibility=View.VISIBLE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        sendBtn.setOnClickListener(this)

        gettString(this,"roomName")

        chatMsgAdapter = chatMsgRecyclerAdapter()
        recyclerView.adapter=chatMsgAdapter
        recyclerView.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this)

        try{
            mSocket= IO.socket("http://${serverIp}:${serverPort}")
        }catch(e:Exception){
            e.printStackTrace()
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("receiveMsg", onRecevieMsg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
    }

    var onConnect = Emitter.Listener{
        var data = joinInfo(gettString(this@TestActivity, "roomName"), gettString(this@TestActivity,"phoneNumber"))
        var jsonData = gson.toJson(data)
        mSocket.emit("joinRoom", jsonData)
    }

    var onRecevieMsg = Emitter.Listener{
        Log.d("fuck","receive")
        val chat:Message = gson.fromJson(it[0].toString(), Message::class.java)
        addItemToRecyclerView(chat)
    }

    private fun sendMsg(){
        val roomName = gettString(this@TestActivity, "roomName")
        val content= chatEdit.text.toString()
        val phoneNumber = gettString(this@TestActivity, "chatName")
        val sendData = Message(roomName,content, phoneNumber, phoneNumber, "")
        val jsonData = gson.toJson(sendData)

        mSocket.emit("sendMsg", jsonData)
        chatEdit.setText("")
        addItemToRecyclerView(sendData)
    }

    private fun addItemToRecyclerView(message: Message) {
        runOnUiThread {
            chatList.add(message)
            chatMsgAdapter.notifyItemInserted(chatList.size)
            recyclerView.scrollToPosition(chatList.size - 1)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.sendBtn -> sendMsg()
        }
    }
}