package com.lessons.petclock

import android.app.Application
import com.lessons.petclock.alarmDatabase.AlarmRepository

class PetClockApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        AlarmRepository.initialize(this)
    }
}