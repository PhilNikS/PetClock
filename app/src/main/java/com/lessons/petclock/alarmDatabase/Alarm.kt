package com.lessons.petclock.alarmDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lessons.petclock.R
import java.io.Serializable
import java.util.*
@Entity
data class Alarm(
    @PrimaryKey val id:UUID = UUID.randomUUID(),
    val name:String = "",
    val timeHour:Int = 4,
    val timeMinute:Int = 20,
    val isTurnedOn:Boolean = false,
    val alarmSoundTitle:String? = null,
    val alarmSoundUri:String? = null,
    val vibration:Int = 0
    ) : Serializable
