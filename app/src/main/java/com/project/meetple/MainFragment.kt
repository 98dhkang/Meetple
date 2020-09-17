package com.project.meetple

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainFragment : Fragment(), View.OnClickListener {

    var per = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //코루틴 추가
        tokenUpdate.getRequest(gettString(requireContext(),"token"),gettString(requireContext(),"phone")).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("fuck","Failed to tokenUpdate")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("fuck", response!!.body())
            }
        })

        TedPermission.with(requireContext())
            .setPermissionListener(permisionListener)
            .setRationaleMessage("사진을 불러오기 위해 갤러리 접근 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()

        reqMyProfile.postRequest(1, gettString(requireContext(), "phone")).enqueue(object : Callback<profile>{
            override fun onFailure(call: Call<profile>, t: Throwable) {
                Log.d("fuck", t.toString())
            }

            override fun onResponse(call: Call<profile>, response: Response<profile>) {
                Log.d("fuck",response.body()!!.toString())
                val result = response.body()!!
                main_nickname.text = result.nickname
                main_school_major.text = result.school + " " + result.major
                main_age_tall.text = result.age.toString()+"세 "+result.tall.toString()+"cm"
                main_rating.text = result.rating.toString()+"/5"
                setString(requireContext(), "sex", result.sex.toString())
                setString(requireContext(), "school", result.school)
            }
        })

        main_friend_btn.setOnClickListener(this)
        main_img_btn.setOnClickListener(this)
        main_myRoomBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.main_friend_btn -> {
                val intent = Intent(requireContext(), FriendActivity::class.java)
                startActivity(intent)
            }
            R.id.main_img_btn -> {
                if(per){
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.setType("image/*")
                    startActivityForResult(intent, OPEN_GALLERY)
                }
                else
                    Toast.makeText(requireContext(), "설정 > 권한에서 허용으로 바꿔주세요.", Toast.LENGTH_SHORT).show()
            }
            R.id.main_myRoomBtn -> {
                    val intent = Intent(requireContext(), myRoomActivity::class.java)
                    startActivity(intent)
            }
        }
    }

    val permisionListener = object : PermissionListener {
        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            per=false
        }
        override fun onPermissionGranted() {
            per=true
        }
    }
}
