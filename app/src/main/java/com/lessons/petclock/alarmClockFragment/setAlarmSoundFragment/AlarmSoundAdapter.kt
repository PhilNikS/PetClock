package com.lessons.petclock.alarmClockFragment.setAlarmSoundFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lessons.petclock.databinding.AlarmSoundItemViewBinding

private const val TAG = "AlarmSoundAdapter"
class AlarmSoundAdapter(
    private val alarmTitleSet: List<String>,
    savedTitle: String = "empty",
    private val onSoundClicked:(soundTitle:String)->Unit
):RecyclerView.Adapter<AlarmSoundViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmSoundViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlarmSoundItemViewBinding.inflate(inflater,parent,false)
        return AlarmSoundViewHolder(binding)
    }

    private var lastClickedTitle:String = savedTitle


    override fun onBindViewHolder(holder: AlarmSoundViewHolder, position: Int) {

        val title = alarmTitleSet[position]
        holder.binding.apply {
            soundTitle.text = title
            isActive.isChecked = title == lastClickedTitle
            root.setOnClickListener{
                onSoundClicked(title)
                lastClickedTitle = title
                notifyDataSetChanged()

            }
        }

    }

    override fun getItemCount(): Int {
        return alarmTitleSet.size
    }

    fun getLastClickedTitle():String{
        Log.d(TAG, "getLastClickedTitle: in fun $lastClickedTitle")
        return lastClickedTitle
    }
}