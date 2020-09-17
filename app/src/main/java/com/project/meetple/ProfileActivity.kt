package com.project.meetple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val intent = intent
        val nickname = intent.getStringExtra("nickname")
        val school = intent.getStringExtra("school")
        val major = intent.getStringExtra("major")
        val age = intent.getIntExtra("age", 0)
        val tall = intent.getIntExtra("tall", 0)
        val talent_img = intent.getIntExtra("talent_img", 0)
        val rating = intent.getFloatExtra("rating", 0.0f)
        val sex = intent.getIntExtra("sex", 1)

        //img 부분 수정
        profile_img.setImageResource(R.drawable.chatbox_woman_mine)
        profile_nickname.text = nickname
        profile_school.text = school
        profile_major.text = major
        profile_age.text = age.toString() + "세"
        profile_tall.text = tall.toString() + "cm"
        profile_rating.text = rating.toString() + "/5"
        if(sex == 1) {
            profile_sex.text = "남자"
        }
        else if(sex ==2) {
            profile_sex.text = "여자"
        }
    }
}
