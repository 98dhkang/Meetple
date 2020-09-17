package com.project.meetple

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_sign_up_fourth.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList
import java.util.jar.Manifest

class SignUpFourthActivity : AppCompatActivity() {
    var uri : String? = null
    lateinit var bitmap : Bitmap

    val permisionListener = object : PermissionListener{
        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            finish()
        }
        override fun onPermissionGranted() {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_fourth)

        TedPermission.with(this)
            .setPermissionListener(permisionListener)
            .setRationaleMessage("사진을 불러오기 위해 갤러리 접근 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()

        sign_up_gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("image/*")
            startActivityForResult(intent, OPEN_GALLERY)
        }

        sign_up_fourth_btn.setOnClickListener {
            if(uri == null){
                Toast.makeText(this, "대표 사진을 정해주세요.", Toast.LENGTH_LONG).show()
            }
            else{
                Log.d("fuck", uri.toString())
                val file= File(uri)
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val img = MultipartBody.Part.createFormData("img", file.name, requestFile)
                val phone = gettString(this, "signUp_phone")
                sign_up_fourthStep.postRequest(4, phone, img).enqueue(object : Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("fuck", t.toString())
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val result = response!!.body()
                        if(result == "Success"){
                            val intent = Intent(this@SignUpFourthActivity, LoginActivity::class.java)
                            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                val ImageUri = data!!.data
                uri = getRealPathFromUri(ImageUri)

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImageUri)
                    sign_up_img.setImageBitmap(bitmap)
                }
                catch (e: Exception) {
                    Log.d("fuck", e.toString())
                }
            }
            else {
                Log.d("fuck", "Wrong request code")
            }
        }
        else{
            Log.d("fuck", "Wrong result code")
        }
    }

    private fun getRealPathFromUri(uri : Uri) : String?{
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var c = contentResolver.query(uri, proj, null, null, null)
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        return c.getString(index)
    }
}