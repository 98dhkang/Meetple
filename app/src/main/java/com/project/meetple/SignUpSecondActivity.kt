package com.project.meetple

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpSecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_second)

        sign_up_second_btn.setOnClickListener{
            if(sign_up_school.text.isNullOrBlank()){
                Toast.makeText(this@SignUpSecondActivity, "학교를 입력해주세요.", Toast.LENGTH_SHORT).show()
                sign_up_school.requestFocus()
            }
            else if(sign_up_major.text.isNullOrBlank()){
                Toast.makeText(this@SignUpSecondActivity, "학과를 입력해주세요.", Toast.LENGTH_SHORT).show()
                sign_up_major.requestFocus()
            }
            else{
                val school = sign_up_school.text.toString()
                val major = sign_up_major.text.toString()
                val phone = gettString(this, "signUp_phone")
                sign_up_secondStep.postRequest(2,phone,school,major).enqueue(object : Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("fuck", t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val result = response!!.body()
                        if(result == "Success"){
                            val intent = Intent(this@SignUpSecondActivity, SignUpThirdActivity::class.java)
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@SignUpSecondActivity).toBundle())
                        }
                        else if(result == "SQL_ERR"){
                            Log.d("fuck", "SQL ERR - step 2")
                        }
                        //나중에 학교, 학과별 case 추가
                    }
                })
            }
        }
    }
}