package com.lessons.petclock.alarmClockFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lessons.petclock.alarmDatabase.Alarm
import com.lessons.petclock.alarmDatabase.AlarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*

class AlarmCLockViewModel: ViewModel() {
    private val repository = AlarmRepository.getRepositoryInstance()
    private val _alarms:MutableStateFlow<List<Alarm>> = MutableStateFlow(emptyList())
    val alarms get() = _alarms.asStateFlow()
    init {
        viewModelScope.launch {
            repository.getAlarms().collect{
                _alarms.value = it
            }
        }
    }

    fun  createNewAlarm(){
        val alarm = Alarm(
            UUID.randomUUID(),
        )
        viewModelScope.launch {
            repository.addAlarm(alarm)
        }
    }
}