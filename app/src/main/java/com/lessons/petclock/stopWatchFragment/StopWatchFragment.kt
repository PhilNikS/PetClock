package com.lessons.petclock.stopWatchFragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lessons.petclock.*
import com.lessons.petclock.databinding.FragmentStopWatchBinding


class StopWatchFragment : Fragment() {

    private var _binding:FragmentStopWatchBinding? = null
    private val binding get() = checkNotNull(_binding){"is binding access?"}
    private val viewModel: StopWatchFragmentViewModel by viewModels()
    private lateinit var serviceIntent:Intent
    private lateinit var chronometer:Chronometer


    private var isStart = false
    private var isRun = false
    private var isStop = true
    private var time = 0.0
    private var subTime = 0.0
    private var maxTime = 0.0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStopWatchBinding.inflate(inflater,container,false)
        requireActivity().registerReceiver(updateTime, IntentFilter(TIMER_UPDATE))
        chronometer = binding.chronometer
        return binding.root
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TIMER_EXTRA, 0.0)

            chronometer.text = getTimeStringFromDouble(time)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            startButton.setOnClickListener{
                isStart = true
                startButton.visibility = View.INVISIBLE
                buttonLeft.visibility = View.VISIBLE
                buttonRight.visibility = View.VISIBLE
                startTimer()

            }
            buttonLeft.setOnClickListener{
                if (isStart){
                    buttonLeft.setText(R.string.stopwatch_buttonLeft2)
                    buttonRight.setText(R.string.stopwatch_buttonRight2)
                    isStart = false
                    isStop = true
                    stopTimer()
                }
                else{
                    buttonLeft.setText(R.string.stopwatch_buttonLeft)
                    buttonRight.setText(R.string.stopwatch_buttonRight)
                    isStart = true
                    isStop = false
                    startTimer()
                }
            }
            buttonRight.setOnClickListener{
                if (isStop) {
                    startButton.visibility = View.VISIBLE
                    buttonLeft.visibility = View.INVISIBLE
                    buttonRight.visibility = View.INVISIBLE
                    buttonLeft.setText(R.string.stopwatch_buttonLeft)
                    buttonRight.setText(R.string.stopwatch_buttonRight)
                    isStart = true
                    isStop = false
                    isRun = false
                    resetTimer()

                } else {

                    isRun = true

                }

            }
        }
    }
    private fun startTimer() {
        serviceIntent = Intent(requireContext(), StopWatchService::class.java)
        serviceIntent.putExtra(TIMER_EXTRA, time)
        requireContext().startService(serviceIntent)
    }

    private fun stopTimer() {
        serviceIntent.putExtra(TIMER_EXTRA, time)
       requireContext().stopService(serviceIntent)
    }

    private fun resetTimer() {
        maxTime = 0.0
        subTime = maxTime
        time = subTime
        binding.apply {
            chronometer.text = getTimeStringFromDouble(time)
        }
        serviceIntent.putExtra(TIMER_EXTRA, time)
        requireContext().stopService(serviceIntent)
    }




    private fun getTimeStringFromDouble(time: Double): String {
        val result = time.toInt()
        val millis = result % 100
        val seconds = result / 100 % 60
        val minutes = result / (100 * 60) % 60
        return makeTimeString(minutes, seconds, millis)
    }

    private fun makeTimeString(
        hours: Int,
        minutes: Int,
        seconds: Int
    ): String {
        return String.format("%02d:%02d.%02d", hours, minutes, seconds)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}