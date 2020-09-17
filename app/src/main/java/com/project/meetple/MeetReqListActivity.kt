package com.project.meetple

import android.app.Activity
import android.app.ActivityOptions
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
import kotlinx.android.synthetic.main.activity_meet_req_list.*
import kotlinx.android.synthetic.main.recycler_meet_room.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetReqListActivity : AppCompatActivity() {

    var meetRoomReqList : Array<meetRoomReq> = arrayOf()

    inner class meetRoomReqListRecyclerAdapter : RecyclerView.Adapter<meetRoomReqListRecyclerAdapter.itemViewHolder>(){
        override fun getItemCount() = meetRoomReqList.size

        override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
            holder.bindMeetRoom()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): meetRoomReqListRecyclerAdapter.itemViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_meet_room, parent,false)
            return itemViewHolder(view)
        }

        inner class itemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bindMeetRoom(){
                itemView.meet_room_school.text = meetRoomReqList[position].school
                itemView.meet_room_NoP.text = meetRoomReqList[position].NoP.toString() + "명"
                itemView.meet_room_title.text = meetRoomReqList[position].title
                itemView.meet_room_rating.text = meetRoomReqList[position].rating.toString() + "/5"
                if(meetRoomReqList[position].sex == 1){
                    itemView.meet_room_sex.setImageResource(R.drawable.chatbox_man_mine)
                }
                else if(meetRoomReqList[position].sex == 2){
                    itemView.meet_room_sex.setImageResource(R.drawable.chatbox_woman_mine)
                }

                itemView.setOnClickListener {
                    var intent = Intent(itemView.context, MeetRoomDetail::class.java)
                    intent.putExtra("roomId", meetRoomReqList[position].roomId)
                    intent.putExtra("type", 2)
                    intent.putExtra("host", meetRoomReqList[position].host)
                    itemView.context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meet_req_list)

        var meetRoomReqListAdapter = meetRoomReqListRecyclerAdapter()
        meet_req_recycler.adapter = meetRoomReqListAdapter
        meet_req_recycler.layoutManager = LinearLayoutManager(this)

        val ext = intent
        val roomId = ext.getIntExtra("roomId", 0)

        reqMeetRoomReqList.postRequest(1, roomId).enqueue(object : Callback<JsonArrayRes> {
            override fun onFailure(call: Call<JsonArrayRes>, t: Throwable) {
                Log.d("fuck", t.toString())
            }

            override fun onResponse(call: Call<JsonArrayRes>, response: Response<JsonArrayRes>) {
                var mGson = Gson()
                meetRoomReqList = mGson.fromJson(
                    response.body()!!.result,
                    Array<meetRoomReq>::class.java
                )
                Log.d("fuck", meetRoomReqList.toString())
                meet_req_text.text = "신청목록("+meetRoomReqList.size+")"
                meetRoomReqListAdapter.notifyDataSetChanged()
            }
        })
    }
}
