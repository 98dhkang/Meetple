package com.project.meetple

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up_first.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_first)
        sign_up_first_btn.setOnClickListener {
            if(sign_up_phone.text.isNullOrBlank()){
                Toast.makeText(this@SignUpFirstActivity, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                sign_up_phone.requestFocus()
            }
            else if(sign_up_pw.text.isNullOrBlank()){
                Toast.makeText(this@SignUpFirstActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                sign_up_pw.requestFocus()
            }
            else{
                val phone = sign_up_phone.text.toString()
                val password = sign_up_pw.text.toString()
                setString(this, "signUp_phone", phone)
                sign_up_firstStep.postRequest(1,phone,password).enqueue(object : Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("fuck", t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val result = response!!.body()
                        if(result == "Success"){
                            val intent = Intent(this@SignUpFirstActivity, SignUpSecondActivity::class.java)
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@SignUpFirstActivity).toBundle())
                        }
                        else if(result == "Duplicate_Phone"){
                            Toast.makeText(this@SignUpFirstActivity, "이미 가입된 전화번호입니다", Toast.LENGTH_LONG).show()
                            sign_up_phone.setText("")
                            sign_up_phone.requestFocus()
                        }
                        else if(result == "SQL_ERR"){
                            Log.d("fuck", "SQL ERR - step 1")
                        }
                    }
                })
            }
        }
    }
}