package com.lessons.petclock.alarmClockFragment.createAlarmFragment

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lessons.petclock.ALARM_NOTIFICATION_CHANNEL_ID
import com.lessons.petclock.R
import com.lessons.petclock.alarmDatabase.Alarm

private const val TAG = "AlarmNotificationService"
class AlarmNotificationService : Service() {
    private var tone: Ringtone? = null
    private lateinit var alarm:Alarm
    private val notificationId get() = alarm.name.hashCode()
    override fun onBind(intent: Intent): IBinder? {
        return null
    }



    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()
        this.registerReceiver(notificationReceiver, IntentFilter("ASK_RECEIVER"))
    }

    private val notificationReceiver:BroadcastReceiver  by lazy {
        object : BroadcastReceiver(){

            override fun onReceive(context: Context?, intent: Intent?) {
                val notificationManager = NotificationManagerCompat.from(context!!)

                if(intent?.getBooleanExtra("isSnoozing",false) == true){
                    val alarmScheduler = AlarmScheduler(context)
                    val intentForScheduler =
                        Intent("SCHEDULE_SNOOZE_NOTIFICATION").putExtra("alarm", alarm)
                    alarmScheduler.schedule(intentForScheduler)
                }
                notificationManager.cancel(notificationId)
                if(tone!=null && tone!!.isPlaying){
                    tone!!.stop()
                }
            }

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alarm = intent?.getSerializableExtra("alarm") as Alarm

        showNotification(alarm)
        val tonePath = alarm.alarmSoundUri
        if(tonePath != null){
            val toneUri = Uri.parse(tonePath)
            tone = RingtoneManager.getRingtone(this,toneUri)
            tone?.play()
        }




        return START_NOT_STICKY
    }
    private fun showNotification(alarm: Alarm){


        val dismissIntent = Intent("ASK_RECEIVER")
        val dismissPendingIntent = PendingIntent.getBroadcast(this,0,dismissIntent,PendingIntent.FLAG_IMMUTABLE)

        val snoozeIntent = Intent("ASK_RECEIVER").apply {
            putExtra("isSnoozing",true)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(this,alarm.name.hashCode(),snoozeIntent,PendingIntent.FLAG_IMMUTABLE )



        val notificationManager = NotificationManagerCompat.from(this)
        val notification = NotificationCompat.Builder(this, ALARM_NOTIFICATION_CHANNEL_ID)
            .setContentText("this is content text ${alarm.name}")
            .setContentTitle("this is content title")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_alarm_dismiss,

                "Dismiss",dismissPendingIntent)
            .addAction(
                R.drawable.ic_alarm_snooze,
                "Snooze",snoozePendingIntent)

            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_DENIED
        ) {
            notificationManager.notify(notificationId,notification)
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tone?.stop()
        this.unregisterReceiver(notificationReceiver)
    }
}