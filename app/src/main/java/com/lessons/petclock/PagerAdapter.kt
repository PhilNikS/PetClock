package com.lessons.petclock

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lessons.petclock.alarmClockFragment.AlarmClockFragment
import com.lessons.petclock.stopWatchFragment.StopWatchFragment

class PagerAdapter(activity: FragmentActivity):FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> AlarmClockFragment()
            1->  StopWatchFragment()
            2-> StopWatchFragment()
            else -> return AlarmClockFragment()
        }
    }
}