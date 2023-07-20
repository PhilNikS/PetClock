package com.lessons.petclock.alarmClockFragment.createAlarmFragment

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lessons.petclock.alarmDatabase.Alarm
import java.time.*
import java.util.Calendar
import kotlin.math.log

private const val TAG = "AlarmScheduler"
class AlarmScheduler(
    private val context: Context

) {
    private val alarmManager:AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private lateinit var alarm:Alarm

    fun schedule(intent: Intent){
        alarm = intent.getSerializableExtra("alarm") as Alarm
        val action = intent.action
        Log.d(TAG, "schedule: $action")
        when(intent.action){

            "SCHEDULE_NEW_NOTIFICATION" -> {
                val time = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY,alarm.timeHour)
                    set(Calendar.MINUTE,alarm.timeMinute)
                }
                scheduleNotification(time,"CREATE_NOTIFICATION")
            }
            
            "SCHEDULE_SNOOZE_NOTIFICATION"->{
                val time:Calendar = Calendar.getInstance()
                    time.add(Calendar.MINUTE,5)
                scheduleNotification(time,"REPEAT_NOTIFICATION")
            }
        }

    }
    private fun scheduleNotification(time:Calendar, actionString:String){

        if(Calendar.getInstance().timeInMillis > time.timeInMillis) time.add(Calendar.DAY_OF_MONTH, 1)



        val currentTime = Calendar.getInstance().timeInMillis
        
        val diffInHours = (time.timeInMillis/60/1000/60).toInt()
        val diffInMinutes = (time.timeInMillis/60/1000%60).toInt()
        Log.d(TAG, "scheduleNotification: ${time.get(Calendar.HOUR_OF_DAY)} : ${time.get(Calendar.MINUTE)}")
        
        
        val intent = Intent(context, AlarmNotificationService::class.java).apply {
            putExtra("alarm",alarm)
            action = actionString
        }
        val pendingIntent = PendingIntent.getService(context,alarm.id.hashCode(),intent,PendingIntent.FLAG_IMMUTABLE)
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time.timeInMillis, pendingIntent)
        val timeInterval:Long = 24*60*60*1000
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,time.timeInMillis,timeInterval,pendingIntent)


    }

}