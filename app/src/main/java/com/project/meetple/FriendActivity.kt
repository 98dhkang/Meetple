package com.project.meetple

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_friend.*
import kotlinx.android.synthetic.main.recycler_friend.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendActivity : AppCompatActivity() {
    var friendReqList : ArrayList<friend> = arrayListOf()
    var friendList : ArrayList<friend> = arrayListOf()

    lateinit var friendReqListAdapter : friendRequestListRecyclerAdapter
    lateinit var friendListAdapter : friendListRecyclerAdapter

    inner class friendRequestListRecyclerAdapter : RecyclerView.Adapter<friendRequestListRecyclerAdapter.itemViewHolder>(){
        override fun getItemCount() = friendReqList.size

        override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
            holder.bindfriendReqList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): friendRequestListRecyclerAdapter.itemViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_friend, parent,false)
            return itemViewHolder(view)
        }

        inner class itemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bindfriendReqList(){
                //이미지 수정
                itemView.friend_talent_img.setImageResource(R.drawable.chatbox_woman_mine)
                itemView.friend_nickname.text = friendReqList[position].nickname
                itemView.friend_phone.text = friendReqList[position].phone
                itemView.friend_btn.text = "수락"
                itemView.friend_btn2.text = "거절"

                itemView.friend_btn.setOnClickListener {
                    resFriendReq.postRequest(3, gettString(itemView.context, "phone"), friendReqList[position].phone, true).enqueue(object : Callback<String>{
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.d("fuck", t.toString())
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if(response.body() == "Success"){
                                friendList.add(0, friendReqList[position])
                                friend_list_text.text = "친구 목록(" + friendList.size + ")"
                                friendListAdapter.notifyDataSetChanged()

                                friendReqList.removeAt(position)
                                friend_request_text.text = "친구 요청(" + friendReqList.size + ")"
                                friendReqListAdapter.notifyDataSetChanged()
                            }
                        }
                    })
                }

                itemView.friend_btn2.setOnClickListener {
                    resFriendReq.postRequest(3, gettString(itemView.context, "phone"), friendReqList[position].phone, false).enqueue(object : Callback<String>{
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.d("fuck", t.toString())
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if(response.body() == "Success"){
                                friendReqList.removeAt(position)
                                friend_request_text.text = "친구 요청(" + friendReqList.size + ")"
                                friendReqListAdapter.notifyDataSetChanged()
                            }
                        }
                    })
                }
            }
        }
    }

    inner class friendListRecyclerAdapter : RecyclerView.Adapter<friendListRecyclerAdapter.itemViewHolder>(){
        override fun getItemCount() = friendList.size

        override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
            holder.bindfriendList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): friendListRecyclerAdapter.itemViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_friend, parent,false)
            return itemViewHolder(view)
        }

        inner class itemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bindfriendList(){
                //이미지 수정
                itemView.friend_talent_img.setImageResource(R.drawable.chatbox_woman_mine)
                itemView.friend_nickname.text = friendList[position].nickname
                itemView.friend_phone.text = friendList[position].phone
                itemView.friend_btn.text = "삭제"
                itemView.friend_btn2.visibility = View.GONE

                itemView.friend_btn.setOnClickListener {
                    var dialog_listner = object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which){
                                DialogInterface.BUTTON_POSITIVE -> {
                                    delFriend.postRequest(4, gettString(itemView.context, "phone"), friendList[position].phone).enqueue(object : Callback<String>{
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Log.d("fuck", t.toString())
                                        }

                                        override fun onResponse(call: Call<String>, response: Response<String>) {
                                            if(response.body() == "Success"){
                                                friendList.removeAt(position)
                                                friend_list_text.text = "친구 목록(" + friendList.size + ")"
                                                friendListAdapter.notifyDataSetChanged()
                                            }
                                        }
                                    })
                                }
                                DialogInterface.BUTTON_NEGATIVE -> {
                                }
                            }
                        }
                    }
                    var dialog = AlertDialog.Builder(itemView.context)
                    dialog.setTitle("친구 삭제")
                    dialog.setMessage("상대방과 친구를 끊으시겠습니까?")
                    dialog.setPositiveButton("확인", dialog_listner)
                    dialog.setNegativeButton("취소", dialog_listner)
                    dialog.show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val phone = gettString(this, "phone")

        friendReqList = arrayListOf()
        friendReqListAdapter = friendRequestListRecyclerAdapter()
        friend_request_recycler.adapter = friendReqListAdapter
        friend_request_recycler.layoutManager = LinearLayoutManager(this@FriendActivity)

        friendList = arrayListOf()
        friendListAdapter = friendListRecyclerAdapter()
        friend_list_recycler.adapter = friendListAdapter
        friend_list_recycler.layoutManager = LinearLayoutManager(this@FriendActivity)

        reqFriendList.postRequest(2, phone).enqueue(object : Callback<friendListData>{
            override fun onFailure(call: Call<friendListData>, t: Throwable) {
                Log.d("fuck", t.toString())
            }

            override fun onResponse(call: Call<friendListData>, response: Response<friendListData>) {
                val result = response.body()!!
                if(result.friendReqList.size > 0) {
                    friendReqList = result.friendReqList
                    friend_request_text.text = "친구 요청(" + friendReqList.size + ")"

                    friendReqListAdapter.notifyDataSetChanged()
                }
                if(result.friendList.size > 0){
                    friendList = result.friendList
                    friend_list_text.text = "친구 목록(" + friendList.size + ")"

                    friendListAdapter.notifyDataSetChanged()
                }
            }
        })

        friend_request_btn.setOnClickListener {
            var flag = true
            val friend_phone = friend_edit_phone.text.toString()
            friend_edit_phone.setText("")

            if(friend_phone == gettString(this, "phone")){
                Toast.makeText(this@FriendActivity, "자신과는 친구를 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                flag = false
            }

            for(i in friendList){
                if(i.phone==friend_phone) {
                    Toast.makeText(this@FriendActivity, "친구목록에 있는 상대방입니다.", Toast.LENGTH_SHORT).show()
                    flag = false
                }
            }

            if(friendReqList.any{ e-> (e.phone == friend_phone) }){
                Toast.makeText(this@FriendActivity, "친구 요청 목록에 있는 상대방입니다.", Toast.LENGTH_SHORT).show()
                flag = false
            }

            if(flag) {
                reqFriend.postRequest(1, phone, friend_phone).enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("fuck", t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val result = response.body()
                        if (result == "Success") {
                            Toast.makeText(this@FriendActivity, "상대방이 수락하면 친구가 됩니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }
    }
}
