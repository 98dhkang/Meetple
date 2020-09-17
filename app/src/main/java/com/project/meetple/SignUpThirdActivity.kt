package com.project.meetple

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up_third.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_third)

        var sex = 1

        sign_up_sex_man.setOnClickListener {
            sex = 1
            sign_up_sex_man.setBackgroundColor(Color.BLUE)
            sign_up_sex_woman.setBackgroundColor(Color.TRANSPARENT)
        }
        sign_up_sex_woman.setOnClickListener {
            sex = 2
            sign_up_sex_woman.setBackgroundColor(Color.BLUE)
            sign_up_sex_man.setBackgroundColor(Color.TRANSPARENT)
        }


        sign_up_third_btn.setOnClickListener {
            if(sign_up_age.text.isNullOrBlank()){
                Toast.makeText(this@SignUpThirdActivity, "나이를 입력해주세요.", Toast.LENGTH_SHORT).show()
                sign_up_age.requestFocus()
            }
            else if(sign_up_tall.text.isNullOrBlank()){
                Toast.makeText(this@SignUpThirdActivity, "키를 입력해주세요.", Toast.LENGTH_SHORT).show()
                sign_up_tall.requestFocus()
            }
            else{
                val age = sign_up_age.text.toString()
                val tall = sign_up_tall.text.toString()
                val phone = gettString(this, "signUp_phone")
                sign_up_thirdStep.postRequest(3, phone, age, tall, sex).enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("fuck", t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val result = response!!.body()
                        if(result == "Success"){
                            val intent = Intent(this@SignUpThirdActivity, SignUpFourthActivity::class.java)
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@SignUpThirdActivity).toBundle())
                        }
                        else if(result == "SQL_ERR"){
                            Log.d("fuck", "SQL ERR - step 3")
                        }
                    }
                })
            }
        }
    }
}