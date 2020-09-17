package com.project.meetple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_make_meet_room.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeMeetRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_meet_room)

        val type = intent.getIntExtra("type", 1)
        if(type==2)
            make_meet_room_btn.text="신청하기"

        make_meet_room_btn.setOnClickListener {

            if (make_meet_room_title.text.isNullOrBlank()) {
                Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                make_meet_room_title.requestFocus()
            }
            else if (make_meet_room_content.text.isNullOrBlank()) {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                make_meet_room_content.requestFocus()
            }
            else {
                val intent = intent
                val member = intent.getStringExtra("member")
                val sex = gettString(this, "sex").toInt()
                val school = gettString(this, "school")
                val NoP = intent.getIntExtra("NoP", 1)
                val title = make_meet_room_title.text.toString()
                val content = make_meet_room_content.text.toString()
                val host = gettString(this, "phone")
                val roomId = intent.getIntExtra("roomId", 0)

                if (type == 1) {
                    reqMakeMeetRoom.postRequest(1, sex, school, member, NoP, title, content, host)
                        .enqueue(object : Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.d("fuck", t.toString())
                            }

                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.body() == "Success") {
                                    Toast.makeText(this@MakeMeetRoomActivity, "생성되었습니다!", Toast.LENGTH_LONG).show()
                                    val mainIntent = Intent(this@MakeMeetRoomActivity, MainActivity::class.java)
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(mainIntent)
                                }
                            }
                        })
                } else if (type == 2) {
                    reqMeetReq.postRequest(2, sex, school, member, NoP, title, content, host, roomId)
                        .enqueue(object : Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.d("fuck", t.toString())
                            }

                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.body() == "DUP")
                                    Toast.makeText(this@MakeMeetRoomActivity, "이미 신청한 방입니다.", Toast.LENGTH_LONG).show()
                                else {
                                    Toast.makeText(this@MakeMeetRoomActivity, "신청되었습니다!", Toast.LENGTH_LONG).show()
                                    val mainIntent = Intent(this@MakeMeetRoomActivity, MainActivity::class.java)
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(mainIntent)
                                }
                            }
                        })
                }
            }
        }
    }
}
