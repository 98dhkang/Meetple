package com.project.meetple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_room.*
import kotlinx.android.synthetic.main.fragment_meetple.*
import kotlinx.android.synthetic.main.recycler_meet_room.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class myRoomActivity : AppCompatActivity() {

    var myMeetRoomList : Array<meetRoom> = arrayOf()

    inner class myMeetRoomListRecyclerAdapter : RecyclerView.Adapter<myMeetRoomListRecyclerAdapter.itemViewHolder>(){
        override fun getItemCount() = myMeetRoomList.size

        override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
            holder.bindMeetRoom()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myMeetRoomListRecyclerAdapter.itemViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_meet_room, parent,false)
            return itemViewHolder(view)
        }

        inner class itemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bindMeetRoom(){
                itemView.meet_room_school.text = myMeetRoomList[position].school
                itemView.meet_room_NoP.text = myMeetRoomList[position].NoP.toString() + "명"
                itemView.meet_room_title.text = myMeetRoomList[position].title
                itemView.meet_room_rating.text = myMeetRoomList[position].rating.toString() + "/5"
                if(myMeetRoomList[position].sex == 1){
                    itemView.meet_room_sex.setImageResource(R.drawable.chatbox_man_mine)
                }
                else if(myMeetRoomList[position].sex == 2){
                    itemView.meet_room_sex.setImageResource(R.drawable.chatbox_woman_mine)
                }

                itemView.setOnClickListener {
                    var intent = Intent(itemView.context, MeetRoomDetail::class.java)
                    intent.putExtra("roomId", myMeetRoomList[position].id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_room)

        var myMeetRoomListAdapter = myMeetRoomListRecyclerAdapter()
        myRoom_recycler.adapter = myMeetRoomListAdapter
        myRoom_recycler.layoutManager = LinearLayoutManager(this)

        reqMyMeetRoom.postRequest(2, gettString(this, "phone")).enqueue(object : Callback<JsonArrayRes>{
            override fun onFailure(call: Call<JsonArrayRes>, t: Throwable) {
                Log.d("fuck", t.toString())
            }

            override fun onResponse(call: Call<JsonArrayRes>, response: Response<JsonArrayRes>) {
                var mGson = Gson()
                myMeetRoomList = mGson.fromJson(
                    response.body()!!.result,
                    Array<meetRoom>::class.java
                )
                myRoom_text1.text = "미팅방(" + myMeetRoomList.size + ")"
                myMeetRoomListAdapter.notifyDataSetChanged()
            }
        })
    }
}
