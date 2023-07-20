package com.lessons.petclock.alarmClockFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lessons.petclock.alarmDatabase.Alarm
import com.lessons.petclock.databinding.AlarmClockViewBinding
import java.util.*

class AlarmClockAdapter(private val alarms:List<Alarm>,
private val onAlarmClicked:(alarmId: UUID)->Unit):RecyclerView.Adapter<AlarmCLockViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmCLockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlarmClockViewBinding.inflate(inflater,parent,false)
        return AlarmCLockViewHolder(binding)
    }



    override fun onBindViewHolder(holder: AlarmCLockViewHolder, position: Int) {
        holder.bind(alarms[position],onAlarmClicked)
    }

    override fun getItemCount(): Int {
        return alarms.size
    }
}