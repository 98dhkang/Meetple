package com.project.meetple

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_select_friends.*
import kotlinx.android.synthetic.main.recycler_friend.view.*
import kotlinx.android.synthetic.main.recycler_friend_small.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectFriendsActivity : AppCompatActivity() {

    var friendList : ArrayList<friend_sv> = arrayListOf()
    var selected_friends : ArrayList<friend_sv> = arrayListOf()

    lateinit var friendListAdapter : friendListRecyclerAdapter
    lateinit var selectedFriendListAdapter : selectedFriendsRecyclerAdapter

    inner class friendListRecyclerAdapter : RecyclerView.Adapter<friendListRecyclerAdapter.itemViewHolder>() {
        override fun getItemCount() = friendList.size

        override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
            holder.bindfriendList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): friendListRecyclerAdapter.itemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_friend, parent, false)
            return itemViewHolder(view)
        }

        //이미지 수정
        inner class itemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindfriendList() {
                itemView.friend_btn.visibility=View.GONE
                itemView.friend_btn2.visibility=View.GONE
                itemView.friend_nickname.text=friendList[position].friend.nickname
                itemView.friend_phone.text=friendList[position].friend.phone
                if(friendList[position].friend.sex == 1)
                    itemView.friend_sex.setImageResource(R.drawable.chatbox_man_mine)
                else
                    itemView.friend_sex.setImageResource(R.drawable.chatbox_woman_mine)

                if(friendList[position].select)
                    itemView.setBackgroundColor(Color.rgb(189,189,189))
                else
                    itemView.setBackgroundColor(Color.rgb(255,255,255))

                itemView.setOnClickListener {
                    if(selected_friends.size > 2){
                        Toast.makeText(this@SelectFriendsActivity, "최대 3명까지만 초대 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        if(friendList[position].friend.sex.toString() == gettString(this@SelectFriendsActivity, "sex")) {
                            if (!friendList[position].select) {
                                friendList[position].select = true
                                itemView.setBackgroundColor(Color.rgb(189, 189, 189))
                                selected_friends.add(friendList[position])
                                selectedFriendListAdapter.notifyDataSetChanged()
                            } else {
                                friendList[position].select = false
                                itemView.setBackgroundColor(Color.rgb(255, 255, 255))
                                selected_friends.removeIf { a -> (a.friend.phone == friendList[position].friend.phone) }
                                selectedFriendListAdapter.notifyDataSetChanged()
                            }
                        }
                        else
                            Toast.makeText(this@SelectFriendsActivity, "동성 친구만 초대 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    inner class selectedFriendsRecyclerAdapter : RecyclerView.Adapter<selectedFriendsRecyclerAdapter.itemViewHolder>() {
        override fun getItemCount() = selected_friends.size

        override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
            holder.bindfriendList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectedFriendsRecyclerAdapter.itemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_friend_small, parent, false)
            return itemViewHolder(view)
        }

        //이미지 수정
        inner class itemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindfriendList() {
                itemView.friend_small_nickname.text=selected_friends[position].friend.nickname

                itemView.setOnClickListener {
                    friendList.find{ e-> (e.friend.phone == selected_friends[position].friend.phone) }!!.select=false
                    selected_friends.removeAt(position)
                    friendListAdapter.notifyDataSetChanged()
                    selectedFriendListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_friends)

        var extra = intent
        val type = extra.getIntExtra("type", 1)
        val roomId = extra.getIntExtra("roomId", 0)
        val org_NoP = extra.getIntExtra("NoP", 0)

        friendListAdapter = friendListRecyclerAdapter()
        select_friend_recycler.adapter = friendListAdapter
        select_friend_recycler.layoutManager = LinearLayoutManager(this)

        selectedFriendListAdapter = selectedFriendsRecyclerAdapter()
        select_recycler.adapter = selectedFriendListAdapter
        select_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        reqRealFriendList.postRequest(5, gettString(this, "phone")).enqueue(object : Callback<ArrayList<friend_sv>>{
            override fun onFailure(call: Call<ArrayList<friend_sv>>, t: Throwable) {
                Log.d("fuck", t.toString())
            }

            override fun onResponse(call: Call<ArrayList<friend_sv>>, response: Response<ArrayList<friend_sv>>) {
                friendList=response.body()!!
                friendListAdapter.notifyDataSetChanged()
            }
        })

        select_btn.setOnClickListener {
            val NoP = selected_friends.size + 1
            if(type == 2 && NoP != org_NoP)
                Toast.makeText(this, "인원 수를 맞춰주세요.", Toast.LENGTH_SHORT).show()
            else {
                var member = gettString(this, "phone")
                for (i in selected_friends) {
                    member += "," + i.friend.phone
                }

                val intent = Intent(this, MakeMeetRoomActivity::class.java)
                intent.putExtra("type", type)
                intent.putExtra("member", member)
                intent.putExtra("NoP", NoP)
                intent.putExtra("roomId", roomId)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
        }
    }
}
