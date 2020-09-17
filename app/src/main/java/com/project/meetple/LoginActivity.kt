package com.project.meetple

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            if(login_phone.text.isNullOrBlank()){
                Toast.makeText(this,"전화번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
                login_phone.requestFocus()
            }
            else if(login_pw.text.isNullOrBlank()){
                Toast.makeText(this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
                login_pw.requestFocus()
            }
            else{
                val phone = login_phone.text.toString()
                val pw = login_pw.text.toString()
                postLogin.postRequest(phone, pw).enqueue(object : Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("fuck", t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val result = response!!.body()
                        if(result == "Success"){
                            setString(this@LoginActivity, "phone", phone)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else if(result == "NoCertify"){
                            setString(this@LoginActivity, "phone", phone)
                            val intent = Intent(this@LoginActivity, CertifyActivity::class.java)
                            startActivity(intent)
                        }
                        else if(result == "MissingMatching"){
                            Toast.makeText(this@LoginActivity, "로그인 정보가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                        }
                        else if(result == "SQL_ERR"){
                            Log.d("fuck", "SQL ERR - login")
                        }
                    }
                })
            }
        }

        login_btn_sign_up.setOnClickListener {
            val intent = Intent(this, SignUpFirstActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
    }
}