package com.project.meetple

import android.app.ActivityOptions
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, MainFragment()).commit()
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, MeetpleFragment()).commit()
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ChatFragment()).commit()
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        slideAnimation(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initChannel()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().replace(R.id.main_frame, MainFragment()).commit()
    }

    private fun initChannel(){
        if(Build.VERSION.SDK_INT < 26)
            return
        val nChannel = NotificationChannel("mmetPleNChannel", "meetPleNotificationChannel", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(nChannel)
        Log.d("fuck", "init channel")
    }
}
