package com.project.meetple

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import okhttp3.MultipartBody


var gson = GsonBuilder()
    .setLenient()
    .create()

var retrofit = Retrofit.Builder()
    .baseUrl("http://${serverIp}:${serverPort}")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

data class JsonArrayRes(
    @SerializedName(value="result")
    var result:JsonArray
)

data class ChatRoomRes(
    @SerializedName(value="num")
    var num:Int,
    @SerializedName(value="chatRoom")
    var chatRoom:Int,
    @SerializedName(value="phoneNumber")
    var phoneNumber:String
)

//interface ChatRoom_INSERT_REQ{
//    @FormUrlEncoded
//    @POST("/chatRoom")
//    fun postRequest(@Field(chat))
//}

data class test(
    @SerializedName(value="test")
    var test:String
)

interface getPush {
    @GET("/push")
    fun getRequest(@Query("token") token: String,
                   @Query("phone") phone: String): Call<String>
}
val tokenUpdate = retrofit.create(getPush::class.java)

interface postSignUpFirst{
    @FormUrlEncoded
    @POST("/signUp")
    fun postRequest(@Field("type") type: Int,
                    @Field("phone") phone: String,
                    @Field("password") password: String): Call<String>
}
val sign_up_firstStep = retrofit.create(postSignUpFirst::class.java)

interface postSignUpSecond{
    @FormUrlEncoded
    @POST("/signUp")
    fun postRequest(@Field("type") type: Int,
                    @Field("phone") phone: String,
                    @Field("school") school: String,
                    @Field("major") major: String): Call<String>
}
val sign_up_secondStep = retrofit.create(postSignUpSecond::class.java)

interface postSignUpThird{
    @FormUrlEncoded
    @POST("/signUp")
    fun postRequest(@Field("type") type: Int,
                    @Field("phone") phone: String,
                    @Field("age") age: String,
                    @Field("tall") tall: String,
                    @Field("sex") sex: Int): Call<String>
}
val sign_up_thirdStep = retrofit.create(postSignUpThird::class.java)

interface postSignUpFourth {
    @Multipart
    @POST("/getImage")
    fun postRequest(
        @Part("type") type: Int,
        @Part("phone") phone: String,
        @Part img: MultipartBody.Part): Call<String>
}
val sign_up_fourthStep = retrofit.create(postSignUpFourth::class.java)

interface postLoginInfo {
    @FormUrlEncoded
    @POST("/login")
    fun postRequest(
        @Field("phone") phone: String,
        @Field("password") password: String) : Call<String>
}
val postLogin = retrofit.create(postLoginInfo::class.java)

interface postPhoneforCertify {
    @FormUrlEncoded
    @POST("/certify")
    fun postRequest(
        @Field("type") type: Int,
        @Field("phone") phone: String) : Call<String>
}
val postPhoneToCertify = retrofit.create(postPhoneforCertify::class.java)

interface postEmailforCertify {
    @FormUrlEncoded
    @POST("/certify")
    fun postRequest(
        @Field("type") type: Int,
        @Field("phone") phone: String,
        @Field("email") email: String)  : Call<String>
}
val postEmailtoCertify = retrofit.create(postEmailforCertify::class.java)

data class meetRoom(
    var id:Int,
    var school:String,
    var title:String,
    var NoP:Int,
    var rating:Float,
    var sex:Int,
    var host:String
)
interface getMeetRoom{
    @GET("/meetRoom")
    fun getRequest() : Call<JsonArrayRes>
}
val reqMeetRoom = retrofit.create(getMeetRoom::class.java)

data class profile(
    var nickname:String,
    var school:String,
    var major:String,
    var age:Int,
    var tall:Int,
    var talent_img:Int,
    var rating:Float,
    var sex:Int
)
interface postMyProfileReq{
    @FormUrlEncoded
    @POST("/myProfile")
    fun postRequest(
        @Field("type") type: Int,
        @Field("phone") phone: String) :Call<profile>
}
val reqMyProfile = retrofit.create(postMyProfileReq::class.java)

data class meetRoomDeatail(
    var host: String,
    var id: Int,
    var NoP:Int,
    var memberList:Array<profile>,
    var title:String,
    var content:String
)
interface postMeetRoomDetail {
    @FormUrlEncoded
    @POST("/meetRoomDetail")
    fun postRequest(
        @Field("id") id: Int) : Call<meetRoomDeatail>
}
val reqMeetRoomDetail = retrofit.create(postMeetRoomDetail::class.java)

interface postFriendREQ{
    @FormUrlEncoded
    @POST("/friend")
    fun postRequest(
        //1 = REQ, 2 = LIST
        @Field("type") type: Int,
        @Field("phone") phone: String,
        @Field("friend_phone") friend_phone: String) : Call<String>
}
val reqFriend = retrofit.create(postFriendREQ::class.java)

data class friend(
    var talent_img: Int,
    var nickname: String,
    var phone: String,
    var sex: Int
)
data class friendListData(
    var friendReqList: ArrayList<friend>,
    var friendList: ArrayList<friend>
)
interface postFriendList{
    @FormUrlEncoded
    @POST("/friend")
    fun postRequest(
        //1 = REQ, 2 = LIST
        @Field("type") type: Int,
        @Field("phone") phone: String) : Call<friendListData>
}
val reqFriendList = retrofit.create(postFriendList::class.java)

interface postFriendReqResult{
    @FormUrlEncoded
    @POST("/friend")
    fun postRequest(
        @Field("type") type: Int,
        @Field("phone") phone: String,
        @Field("friend_phone") friend_phone: String,
        @Field("result") result: Boolean) :Call<String>
}
val resFriendReq = retrofit.create(postFriendReqResult::class.java)

interface postFriendDelete{
    @FormUrlEncoded
    @POST("/friend")
    fun postRequest(
        @Field("type") type: Int,
        @Field("phone") phone: String,
        @Field("friend_phone") friend_phone: String) :Call<String>
}
val delFriend = retrofit.create(postFriendDelete::class.java)


data class friend_sv(
    var friend: friend,
    var select: Boolean
)
interface postRealFriendList{
    @FormUrlEncoded
    @POST("/friend")
    fun postRequest(
        @Field("type") type: Int,
        @Field("phone") phone: String) : Call<ArrayList<friend_sv>>
}
val reqRealFriendList = retrofit.create(postRealFriendList::class.java)

interface postMeetRoom{
    @FormUrlEncoded
    @POST("/meetRoom")
    fun postRequest(
        @Field("type") type: Int,
        @Field("sex") sex: Int,
        @Field("school") school: String,
        @Field("member") member: String,
        @Field("NoP") NoP: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("host") host: String) : Call<String>
}
val reqMakeMeetRoom = retrofit.create(postMeetRoom::class.java)

interface postMyMeetRoom{
    @FormUrlEncoded
    @POST("/meetRoom")
    fun postRequest(
        @Field("type") type: Int,
        @Field("phone") phone: String) : Call<JsonArrayRes>
}
val reqMyMeetRoom = retrofit.create(postMyMeetRoom::class.java)

data class meetRoomReq(
    var roomId:Int,
    var school:String,
    var title:String,
    var NoP:Int,
    var rating:Float,
    var sex:Int,
    var host:String
)

interface postMeetRoomReqList{
    @FormUrlEncoded
    @POST("/meetRoomReq")
    fun postRequest(
        @Field("type") type: Int,
        @Field("roomId") roomId: Int) : Call<JsonArrayRes>
}
val reqMeetRoomReqList = retrofit.create(postMeetRoomReqList::class.java)

interface postMeetReq{
    @FormUrlEncoded
    @POST("/meetRoomReq")
    fun postRequest(
        @Field("type") type: Int,
        @Field("sex") sex: Int,
        @Field("school") school: String,
        @Field("member") member: String,
        @Field("NoP") NoP: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("host") host: String,
        @Field("roomId") roomId: Int) : Call<String>
}
val reqMeetReq = retrofit.create(postMeetReq::class.java)

interface postMeetRoomReqDetail {
    @FormUrlEncoded
    @POST("/meetRoomReq")
    fun postRequest(
        @Field("type") type: Int,
        @Field("id") id: Int,
        @Field("host") host: String) : Call<meetRoomDeatail>
}
val reqMeetRoomReq = retrofit.create(postMeetRoomReqDetail::class.java)

interface postOkMeetReq{
    @FormUrlEncoded
    @POST("/meetRoomReq")
    fun postRequest(
        @Field("type") type: Int,
        @Field("id") id: Int,
        @Field("host") host: String,
        @Field("school") school: String,
        @Field("ok") ok: Boolean) : Call<String>
}
val reqOkMeetReq = retrofit.create(postOkMeetReq::class.java)

interface postDelMeetRoom{
    @FormUrlEncoded
    @POST("/meetRoom")
    fun postRequest(
        @Field("type") type: Int,
        @Field("id") id: Int) : Call<String>
}
val reqeDelMeetRoom = retrofit.create(postDelMeetRoom::class.java)