package com.project.meetple

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_certify.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CertifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certify)

        val phone = gettString(this, "phone")

        postPhoneToCertify.postRequest(1, phone).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("fuck", t.toString())
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val email = response!!.body()
                certify_email.text = "@"+email
            }
        })

        showDialogBox(0)

        certify_btn.setOnClickListener {
            if(certify_id.text.isNullOrBlank()){
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                certify_id.requestFocus()
            }
            else{
                certify_btn.isEnabled=false
                val Email = certify_id.text.toString() + certify_email.text.toString()
                postEmailtoCertify.postRequest(2, phone, Email).enqueue(object : Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("fuck", t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val result = response!!.body()
                        if(result == "Duplicate_email"){
                            Toast.makeText(this@CertifyActivity, "이미 가입된 메일입니다.", Toast.LENGTH_SHORT).show()
                            certify_id.requestFocus()
                        }
                        else{
                            showDialogBox(1)
                        }
                    }
                })
            }
        }
    }

    private fun showDialogBox(i : Int){
        var dialog_listner = object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> {
                        if(i==1) {
                            finish()
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        finishAffinity()
                        System.runFinalization()
                        System.exit(0)
                    }
                }
            }
        }
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("이메일 인증")
        if(i==0) {
            dialog.setMessage("어플을 이용하기 위해선 학교 이메일 인증이 필요합니다.")
        }
        else if(i==1){
            dialog.setMessage("해당 이메일로 발송된 메일을 확인해주세요.")
        }
        dialog.setPositiveButton("확인", dialog_listner)
        dialog.setNegativeButton("종료", dialog_listner)
        dialog.show()
    }
}