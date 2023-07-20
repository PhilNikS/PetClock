package com.lessons.petclock.stopWatchFragment

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

const val TIMER_EXTRA = "timer_extra"
const val TIMER_UPDATE = "timer_update"

class StopWatchService : Service() {

    private val timer = Timer()
    private var time =0.0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        time = intent!!.getDoubleExtra(TIMER_EXTRA,0.0)
        Log.d("service","started")

        timer.scheduleAtFixedRate(TimeTask(),0,10)

        return START_NOT_STICKY

    }
    internal inner class TimeTask : TimerTask() {
        override fun run() {
            val intent = Intent(TIMER_UPDATE)
            time++
            intent.putExtra(TIMER_EXTRA,time)
            sendBroadcast(intent)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }


}
