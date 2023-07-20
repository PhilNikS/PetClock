package com.lessons.petclock

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lessons.petclock.alarmClockFragment.createAlarmFragment.AlarmScheduler
import com.lessons.petclock.alarmDatabase.Alarm
import com.lessons.petclock.alarmDatabase.AlarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "CreateAlarmViewModel"
class CreateAlarmViewModel(alarmId: UUID, isCreating: Boolean): ViewModel() {

    private val alarmRepository = AlarmRepository.getRepositoryInstance()
    private val _alarm: MutableStateFlow<Alarm?> = MutableStateFlow(null)
    val alarm: StateFlow<Alarm?> get() = _alarm.asStateFlow()
    var isChanged = false
    private val isCreating = isCreating




    init {
        if (isCreating){
            val newAlarm = Alarm(id = alarmId)
            _alarm.value = newAlarm
            Log.d(TAG, "creating")
        }
        else {
            viewModelScope.launch {
                _alarm.value = alarmRepository.getAlarm(alarmId)
            }
            Log.d(TAG, "not creating")
        }

    }

    fun updateAlarm(onUpdate:(Alarm) -> Alarm){
        isChanged = true
        _alarm.update {oldAlarm ->
            oldAlarm?.let{
                Log.d(TAG, "updateAlarm: ${it.alarmSoundTitle}")
                onUpdate(it)
            }
        }
    }

    fun accessCreatingAlarm() {

        updateAlarm { oldAlarm ->
            oldAlarm.copy(isTurnedOn = true)
        }

        _alarm.value?.let {
            if (isCreating) {
                viewModelScope.launch {
                    Log.d(TAG, "accessCreatingAlarm: adding ${it.alarmSoundTitle}")
                    alarmRepository.addAlarm(it)
                }
            } else {
                Log.d(TAG, "accessCreatingAlarm: updating ${it.alarmSoundTitle}")
                alarmRepository.updateAlarm(it)
            }

        }

    }

    fun deleteAlarm(){
        viewModelScope.launch {
            _alarm.value?.let {
                alarmRepository.deleteAlarm(it)
            }
        }
    }

    fun accessScheduling(requireContext: Context) {
        val alarmScheduler = AlarmScheduler(requireContext)
        alarm.value?.let {
            val notificationIntent = Intent().apply {
                action = "SCHEDULE_NEW_NOTIFICATION"
                putExtra("alarm", it)
            }
            alarmScheduler.schedule(notificationIntent)
        }
        
    }
    fun showAlarmToast(context:Context){
        val currentTime = Calendar.getInstance().timeInMillis
        val selectedTime = Calendar.getInstance().apply {
            set(Calendar.HOUR, _alarm.value!!.timeHour)
            set(Calendar.MINUTE, _alarm.value!!.timeMinute)
        }
        if (currentTime>selectedTime.timeInMillis) selectedTime.add(Calendar.DAY_OF_MONTH,1)
        val diffInMillis = selectedTime.timeInMillis-currentTime
        val diffInHours = (diffInMillis/60/1000/60).toInt()
        val diffInMinutes = (diffInMillis/60/1000%60).toInt()
        if (diffInHours!=0) {
            Toast.makeText(
                context,
                "Alarm set for $diffInHours hour and $diffInMinutes minutes from now.",
                Toast.LENGTH_LONG
            ).show()
        }
        else{
            Toast.makeText(
                context,
                "Alarm set for $diffInMinutes minutes from now.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}



class CreateAlarmViewModelFactory(private val alarmId: UUID, private val isCreating: Boolean = false):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateAlarmViewModel(alarmId, isCreating) as T
    }
}

