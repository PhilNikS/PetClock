package com.lessons.petclock.alarmClockFragment.createAlarmFragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable.Creator
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lessons.petclock.R
import com.lessons.petclock.alarmClockFragment.setAlarmSoundFragment.AlarmSoundAdapter
import com.lessons.petclock.databinding.FragmentSetAlarmVibrationBinding


class SetAlarmVibrationFragment : Fragment() {

    private var _binding:FragmentSetAlarmVibrationBinding? = null
    private val binding get() = checkNotNull(_binding){"can not access _binding"}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetAlarmVibrationBinding.inflate(inflater,container,false)
        binding.vibrationContainer.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var map: Map<String,Long> = mapOf(Pair("Basic call",100L), Pair("Heartbeat",300L),Pair("Ticktock",700L),Pair("Waltz",1000L))
        val list2: List<String> = listOf("Basic call" , "Heartbeat", "Ticktock", "Waltz")
        var list: List<Long> = listOf(1000L,300L,50L,700L)

        val adapter = AlarmSoundAdapter(list2){title ->
             map[title]?.let {
                 vibratePhone(it)
             }
        }
        binding.vibrationContainer.adapter = adapter


    }

    private fun Fragment.vibratePhone(long: Long){
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val array = longArrayOf(0,10,200,500,700,1000)
        if(Build.VERSION.SDK_INT >= 26){
            vibrator.vibrate(VibrationEffect.createWaveform(array,-2))

        }


    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}