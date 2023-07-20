package com.lessons.petclock.alarmClockFragment

import androidx.recyclerview.widget.RecyclerView
import com.lessons.petclock.alarmDatabase.Alarm
import com.lessons.petclock.databinding.AlarmClockViewBinding
import java.util.*

class AlarmCLockViewHolder(private val binding: AlarmClockViewBinding):RecyclerView.ViewHolder(binding.root) {

    fun bind(alarm:Alarm, onAlarmClicked:(alarmId: UUID)->Unit){
        binding.apply {
            alarmName.text = alarm.name
            alarmTime.text = "${alarm.timeHour}:${alarm.timeMinute}"
            alarmSwitch.isChecked = alarm.isTurnedOn

        }
        binding.root.setOnClickListener{
            onAlarmClicked(alarm.id)
        }
    }

}