package com.project.meetple

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.fragment_meetple.*
import kotlinx.android.synthetic.main.recycler_meet_room.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetpleFragment : Fragment() {

    var meetRoomList : Array<meetRoom> = arrayOf()

    inner class meetRoomListRecyclerAdapter : RecyclerView.Adapter<meetRoomListRecyclerAdapter.itemViewHolder>(){
        override fun getItemCount() = meetRoomList.size

        override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
            holder.bindMeetRoom()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): meetRoomListRecyclerAdapter.itemViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_meet_room, parent,false)
            return itemViewHolder(view)
        }

        inner class itemViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
            fun bindMeetRoom(){
                itemView.meet_room_school.text = meetRoomList[position].school
                itemView.meet_room_NoP.text = meetRoomList[position].NoP.toString() + "명"
                itemView.meet_room_title.text = meetRoomList[position].title
                itemView.meet_room_rating.text = meetRoomList[position].rating.toString() + "/5"
                if(meetRoomList[position].sex == 1){
                    itemView.meet_room_sex.setImageResource(R.drawable.chatbox_man_mine)
                }
                else if(meetRoomList[position].sex == 2){
                    itemView.meet_room_sex.setImageResource(R.drawable.chatbox_woman_mine)
                }

                itemView.setOnClickListener {
                    var intent = Intent(itemView.context, MeetRoomDetail::class.java)
                    intent.putExtra("roomId", meetRoomList[position].id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meetple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var meetRoomListAdapter = meetRoomListRecyclerAdapter()
        meetple_recycler.adapter = meetRoomListAdapter
        meetple_recycler.layoutManager = LinearLayoutManager(requireContext())

        reqMeetRoom.getRequest().enqueue(object : Callback<JsonArrayRes>{
            override fun onFailure(call: Call<JsonArrayRes>, t: Throwable) {
                Log.d("fuck", t.toString())
            }

            override fun onResponse(call: Call<JsonArrayRes>, response: Response<JsonArrayRes>) {
                var mGson = Gson()
                meetRoomList = mGson.fromJson(
                    response.body()!!.result,
                    Array<meetRoom>::class.java
                )
                meetRoomListAdapter.notifyDataSetChanged()
            }
        })

        meetple_create_btn.setOnClickListener {
            val intent = Intent(requireContext(), SelectFriendsActivity::class.java)
            intent.putExtra("type", 1)
            startActivity(intent)
        }
    }
}
