package com.project.meetple


import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.recycler_chat_room.view.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment : Fragment() {

    lateinit var chatListAdapter : chatListRecyclerAdapter
    var chatRoomList:List<chat_room_db> = listOf()

    private val job by lazy { Job() }
    private val chatDAO by lazy { AppDB.getDatabase(requireContext()).ChatDAO()}

    inner class chatListRecyclerAdapter : RecyclerView.Adapter<chatListRecyclerAdapter.itemViewHolder>(){
        override fun getItemCount() = chatRoomList.size

        override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
            holder.bindChatRoom()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatListRecyclerAdapter.itemViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_chat_room, parent,false)
            return itemViewHolder(view)
        }

        inner class itemViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
            fun bindChatRoom(){
                itemView.chat_room_title.text = chatRoomList[position].roomName + chatRoomList[position].title
                itemView.chat_room_last_msg.text = chatRoomList[position].last_msg
                itemView.chat_room_time.text = chatRoomList[position].last_msg_time

                itemView.setOnClickListener{
                    setString(itemView.context, "roomName", chatRoomList[position].roomName)
                    var intent = Intent(itemView.context, TestActivity::class.java)
                    itemView.context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch(Dispatchers.Main + job){
            chatRoomList = withContext(Dispatchers.IO) { chatDAO.selectChatRoom() }
            chatListAdapter.notifyItemInserted(chatRoomList.size)
            Log.d("fuck","a")
        }

        chatListAdapter = chatListRecyclerAdapter()
        chat_room_list.adapter = chatListAdapter
        chat_room_list.layoutManager = LinearLayoutManager(requireContext())
        Log.d("fuck","b")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
