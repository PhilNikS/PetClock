package com.lessons.petclock.alarmDatabase

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "alarm_database"
class AlarmRepository private constructor(
    context: Context,
    private val scope:CoroutineScope = GlobalScope
) {


    companion object{
        private var INSTANCE:AlarmRepository? = null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = AlarmRepository(context)
            }
        }

        fun getRepositoryInstance():AlarmRepository {
            return INSTANCE?: throw IllegalStateException("Repository must be initialized")
        }
    }

    private val database:AlarmDatabase = Room.databaseBuilder(
        context.applicationContext,
        AlarmDatabase::class.java,
        DATABASE_NAME
    ).build()

    fun getAlarms(): Flow<List<Alarm>> =  database.alarmDao().getAlarms()

    suspend fun getAlarm(id:UUID):Alarm = database.alarmDao().getAlarm(id)

    suspend fun addAlarm(alarm: Alarm) = database.alarmDao().addAlarm(alarm)

    fun updateAlarm(alarm: Alarm) {
        scope.launch {
            database.alarmDao().updateAlarm(alarm)
        }


    }

    suspend fun deleteAlarm(alarm: Alarm) = database.alarmDao().deleteAlarm(alarm)

}