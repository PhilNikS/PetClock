package com.lessons.petclock

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import com.lessons.petclock.databinding.ActivityMainBinding

const val ALARM_NOTIFICATION_CHANNEL_ID = "ALARM_NOTIFICATION_CHANNEL_ID"
class MainActivity : AppCompatActivity(){
    private lateinit var binding:ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val alarmNotificationChannel = NotificationChannel(
                ALARM_NOTIFICATION_CHANNEL_ID,
                "Alarm notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(null,null)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(alarmNotificationChannel)

        }



    onBackPressedDispatcher

    }



}