package com.project.meetple

import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_meet_room_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetRoomDetail : AppCompatActivity(), View.OnClickListener{

    lateinit var meetRoomInfo : meetRoomDeatail
    var isMyroom = false
    var type = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meet_room_detail)

        meet_room_detail_img1.setOnClickListener(this)
        meet_room_detail_img2.setOnClickListener(this)
        meet_room_detail_img3.setOnClickListener(this)
        meet_room_detail_img4.setOnClickListener(this)
        meet_room_detail_btn.setOnClickListener(this)
        meet_room_detail_delete.setOnClickListener(this)

        val intent = intent
        val id = intent.getIntExtra("roomId", 0)
        type = intent.getIntExtra("type", 1)

        if(type==1) {
            reqMeetRoomDetail.postRequest(id).enqueue(object : Callback<meetRoomDeatail> {
                override fun onFailure(call: Call<meetRoomDeatail>, t: Throwable) {
                    Log.d("fuck", t.toString())
                }

                override fun onResponse(call: Call<meetRoomDeatail>, response: Response<meetRoomDeatail>) {
                    meetRoomInfo = response.body()!!
                    imgSet(meetRoomInfo.NoP)
                    meet_room_detail_title.text = meetRoomInfo.title
                    meet_room_detail_content.text = meetRoomInfo.content

                    if (meetRoomInfo.host == gettString(this@MeetRoomDetail, "phone")) {
                        isMyroom = true
                        meet_room_detail_btn.text = "신청목록"
                        meet_room_detail_delete.visibility = View.VISIBLE
                    }
                }
            })
        }
        else if(type==2){
            meet_room_detail_delete.visibility = View.VISIBLE
            meet_room_detail_btn.text = "수락"
            meet_room_detail_delete.text = "거절"
            val host = intent.getStringExtra("host")

            reqMeetRoomReq.postRequest(3, id, host).enqueue(object : Callback<meetRoomDeatail> {
                override fun onFailure(call: Call<meetRoomDeatail>, t: Throwable) {
                    Log.d("fuck", t.toString())
                }

                override fun onResponse(call: Call<meetRoomDeatail>, response: Response<meetRoomDeatail>) {
                    meetRoomInfo = response.body()!!
                    Log.d("fuck", meetRoomInfo.toString())
                    imgSet(meetRoomInfo.NoP)
                    meet_room_detail_title.text = meetRoomInfo.title
                    meet_room_detail_content.text = meetRoomInfo.content
                }
            })
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.meet_room_detail_img1 -> {getProfile(0)}
            R.id.meet_room_detail_img2 -> {getProfile(1)}
            R.id.meet_room_detail_img3 -> {getProfile(2)}
            R.id.meet_room_detail_img4 -> {getProfile(3)}
            R.id.meet_room_detail_btn -> {
                if(type==1) {
                    if (isMyroom) {
                        val intent = Intent(this, MeetReqListActivity::class.java)
                        intent.putExtra("roomId", meetRoomInfo.id)
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                    } else {
                        if(meetRoomInfo.memberList[0].sex == gettString(this, "sex").toInt())
                            Toast.makeText(this, "이성에게만 신청이 가능합니다!", Toast.LENGTH_SHORT).show()
                        else {
                            val intent = Intent(this, SelectFriendsActivity::class.java)
                            intent.putExtra("roomId", meetRoomInfo.id)
                            intent.putExtra("type", 2)
                            intent.putExtra("NoP", meetRoomInfo.NoP)
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                        }
                    }
                }
                else if(type==2){
                    var dialog_listner = object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which){
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val school = gettString(this@MeetRoomDetail, "school") + "," + meetRoomInfo.memberList[0].school
                                    reqOkMeetReq.postRequest(4, meetRoomInfo.id, meetRoomInfo.host, school, true).enqueue(object : Callback<String>{
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Log.d("fuck", t.toString())
                                        }

                                        //채팅방 추가 (수정)
                                        override fun onResponse(call: Call<String>, response: Response<String>) {
                                            Toast.makeText(this@MeetRoomDetail, "수락되었습니다!\n채팅방이 개설됐습니다.", Toast.LENGTH_LONG).show()
                                            val intent = Intent(this@MeetRoomDetail, MainActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent)
                                        }
                                    })
                                }
                                DialogInterface.BUTTON_NEGATIVE -> {
                                }
                            }
                        }
                    }
                    var dialog = AlertDialog.Builder(this)
                    dialog.setTitle("미팅 요청")
                    dialog.setMessage("수락하시겠습니까?")
                    dialog.setPositiveButton("확인", dialog_listner)
                    dialog.setNegativeButton("취소", dialog_listner)
                    dialog.show()
                }
            }
            R.id.meet_room_detail_delete -> {
                if(type==1){
                    var dialog_listner = object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which){
                                DialogInterface.BUTTON_POSITIVE -> {
                                    reqeDelMeetRoom.postRequest(3, meetRoomInfo.id).enqueue(object : Callback<String>{
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Log.d("fuck", t.toString())
                                        }

                                        //채팅방 추가 (수정)
                                        override fun onResponse(call: Call<String>, response: Response<String>) {
                                            Toast.makeText(this@MeetRoomDetail, "삭제되었습니다.", Toast.LENGTH_LONG).show()
                                            val intent = Intent(this@MeetRoomDetail, MainActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent)
                                        }
                                    })
                                }
                                DialogInterface.BUTTON_NEGATIVE -> {
                                }
                            }
                        }
                    }
                    var dialog = AlertDialog.Builder(this)
                    dialog.setTitle("미팅방")
                    dialog.setMessage("삭제하시겠습니까?")
                    dialog.setPositiveButton("확인", dialog_listner)
                    dialog.setNegativeButton("취소", dialog_listner)
                    dialog.show()
                }
                else if(type==2){
                    var dialog_listner = object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which){
                                DialogInterface.BUTTON_POSITIVE -> {
                                    reqOkMeetReq.postRequest(4, meetRoomInfo.id, meetRoomInfo.host, "", false).enqueue(object : Callback<String>{
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Log.d("fuck", t.toString())
                                        }

                                        override fun onResponse(call: Call<String>, response: Response<String>) {
                                            Toast.makeText(this@MeetRoomDetail, "신청목록에서 삭제되었습니다.", Toast.LENGTH_LONG).show()
                                            val intent = Intent(this@MeetRoomDetail, MainActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent)
                                        }
                                    })
                                }
                                DialogInterface.BUTTON_NEGATIVE -> {
                                }
                            }
                        }
                    }
                    var dialog = AlertDialog.Builder(this)
                    dialog.setTitle("미팅 요청")
                    dialog.setMessage("거절하시겠습니까?")
                    dialog.setPositiveButton("확인", dialog_listner)
                    dialog.setNegativeButton("취소", dialog_listner)
                    dialog.show()
                }
            }
        }
    }

    //talent_img 별 이미지 변경 수정
    private fun imgSet(NoP:Int){
        when(NoP){
            1 ->{
                meet_room_detail_img1.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img1.visibility = View.VISIBLE
                meet_room_detail_img2.visibility = View.GONE
                meet_room_detail_img3.visibility = View.GONE
                meet_room_detail_img4.visibility = View.GONE
            }
            2 ->{
                meet_room_detail_img1.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img2.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img1.visibility = View.VISIBLE
                meet_room_detail_img2.visibility = View.VISIBLE
                meet_room_detail_img3.visibility = View.GONE
                meet_room_detail_img4.visibility = View.GONE
            }
            3 ->{
                meet_room_detail_img1.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img2.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img3.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img1.visibility = View.VISIBLE
                meet_room_detail_img2.visibility = View.VISIBLE
                meet_room_detail_img3.visibility = View.VISIBLE
                meet_room_detail_img4.visibility = View.GONE
            }
            4 ->{
                meet_room_detail_img1.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img2.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img3.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img4.setImageResource(R.drawable.chatbox_woman_mine)
                meet_room_detail_img1.visibility = View.VISIBLE
                meet_room_detail_img2.visibility = View.VISIBLE
                meet_room_detail_img3.visibility = View.VISIBLE
                meet_room_detail_img4.visibility = View.VISIBLE
            }
        }
    }

    private fun getProfile(id: Int){
        var intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("nickname", meetRoomInfo.memberList[id].nickname)
        intent.putExtra("school", meetRoomInfo.memberList[id].school)
        intent.putExtra("major", meetRoomInfo.memberList[id].major)
        intent.putExtra("age", meetRoomInfo.memberList[id].age)
        intent.putExtra("tall", meetRoomInfo.memberList[id].tall)
        intent.putExtra("talent_img", meetRoomInfo.memberList[id].talent_img)
        intent.putExtra("rating", meetRoomInfo.memberList[id].rating)
        intent.putExtra("sex", meetRoomInfo.memberList[id].sex)
        startActivity(intent)
    }
}
