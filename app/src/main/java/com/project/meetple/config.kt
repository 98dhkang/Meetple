package com.project.meetple

import android.content.Context
import android.os.Build
import android.transition.Slide
import android.view.Gravity
import android.view.Window

val PREFS_FILENAME="data"
val serverIp="13.58.198.34"
val serverPort="65002"
val OPEN_GALLERY=1

fun gettString(context: Context, key:String) :String{
    val prefs=context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    return prefs.getString(key,"1")
}
fun setString(context: Context, key:String, value:String?){
    val prefs=context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    val editor=prefs!!.edit()
    editor.putString(key,value).apply()
}
fun slideAnimation(window: Window){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an slide transition
            enterTransition = Slide(Gravity.END)
            exitTransition = Slide(Gravity.START)
        }
    }
}