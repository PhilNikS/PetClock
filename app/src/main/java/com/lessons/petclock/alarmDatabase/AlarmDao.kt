package com.lessons.petclock.alarmDatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAlarms(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarm WHERE id =(:id)")
    suspend fun getAlarm(id:UUID):Alarm

    @Insert
    suspend fun addAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}