package com.lessons.petclock.alarmClockFragment.createAlarmFragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lessons.petclock.CreateAlarmViewModel
import com.lessons.petclock.CreateAlarmViewModelFactory
import com.lessons.petclock.R

import com.lessons.petclock.alarmClockFragment.setAlarmSoundFragment.SetAlarmSoundFragment

import com.lessons.petclock.alarmDatabase.Alarm

import com.lessons.petclock.databinding.FragmentCreateAlarmBinding
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime

private const val TAG = "CreateAlarmFragment"
class CreateAlarmFragment : Fragment() {
    private var _binding:FragmentCreateAlarmBinding? = null
    private val binding get()= checkNotNull(_binding){"is binding access?" }
    private val args: CreateAlarmFragmentArgs by navArgs()
    private val viewModel: CreateAlarmViewModel by lazy {
        ViewModelProvider(this,CreateAlarmViewModelFactory(args.alarmId,args.isCreating))[CreateAlarmViewModel::class.java]
    }
    private var hours:Int = 0
    private var minutes:Int = 0





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAlarmBinding.inflate(inflater,container,false)
        createNumberPicker()
        setFragmentResultListener(CloseCreatingDialogFragment.REQUEST_KEY_BACK_PRESSED){ _, bundle ->
            if (bundle.getBoolean(CloseCreatingDialogFragment.BACK_PRESSED_RESULT_KEY)){
                viewModel.accessCreatingAlarm()
                findNavController().popBackStack()
            }
            else{

                findNavController().popBackStack()
            }
        }
        setFragmentResultListener(SetNameDialogFragment.REQUEST_KEY_SET_ALARM_NAME){ _, bundle ->
            viewModel.updateAlarm { oldAlarm->
                oldAlarm.copy(name = bundle.getString(SetNameDialogFragment.SET_ALARM_RESULT_KEY,""))
            }
        }
        setFragmentResultListener(SetAlarmSoundFragment.REQUEST_KEY_SET_ALARM_SOUND_TITLE){_,bundle ->
            val title = bundle.getString(SetAlarmSoundFragment.SET_ALARM_SOUND_TITLE_RESULT_KEY,"")
            val uri = SetAlarmSoundFragment.getMapOfAlarmSounds(requireContext())[title]
            viewModel.updateAlarm { oldAlarm->
                oldAlarm.copy(alarmSoundTitle = title, alarmSoundUri = uri)
            }

        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.apply {

            minPicker.apply {
                setOnScrollChangeListener(View.OnScrollChangeListener { _, _, _, _, _ ->
                    minutes = value
                    viewModel.updateAlarm { oldAlarm ->
                        oldAlarm.copy(timeMinute = value)

                    }
                })
            }
            hourPicker.apply {
                setOnScrollChangeListener(View.OnScrollChangeListener { _, _, _, _, _ ->
                    hours = value
                    viewModel.updateAlarm { oldAlarm ->
                        oldAlarm.copy(timeHour = value)
                    }
                })
            }

            buttonSave.setOnClickListener{
                viewModel.showAlarmToast(requireContext())
                viewModel.accessCreatingAlarm()
                viewModel.accessScheduling(requireContext())
                findNavController().popBackStack()

            }
            buttonClose.setOnClickListener{
                viewModel.deleteAlarm()
                findNavController().popBackStack()
            }
            alarmNameLayout.setOnClickListener{
                viewModel.alarm.value.let{
                    it?.let{
                        findNavController().navigate(CreateAlarmFragmentDirections.setAlarmName())
                    }
                }

            }
            alarmSoundLayout.setOnClickListener {
                viewModel.alarm.value.let{
                    it?.let{
                        val title = binding.soundTitle.text.toString()
                        findNavController().navigate(CreateAlarmFragmentDirections.setAlarmSound(args.alarmId, title))
                    }
                }

            }

            alarmVidroLayout.setOnClickListener {
                findNavController().navigate(CreateAlarmFragmentDirections.setAlarmVibration())
            }

            dateView.setOnClickListener {

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.alarm.collect { alarm ->
                        alarm?.let {alarm->
                            createTime(alarm.timeHour, alarm.timeMinute)

                        }
                    }
                }

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if(viewModel.isChanged) {
                findNavController().navigate(CreateAlarmFragmentDirections.onBackPressedAction())
            }
            else{
                findNavController().popBackStack()
            }
        }
        






        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.alarm.collect{alarm ->
                    alarm?.let {
                        updateUi(it)
                    }
                }
            }
        }
    }



    private fun updateUi(alarm: Alarm){


        binding.apply {
            textNaming.text = alarm.name.ifEmpty { getString(R.string.default_alarm_name) }
            soundTitle.text = alarm.alarmSoundTitle?:SetAlarmSoundFragment.getListOfAlarmSounds(requireContext())[0]
            hourPicker.value = alarm.timeHour
            minPicker.value = alarm.timeMinute

        }
    }
    private fun createNumberPicker() {
        binding.hourPicker.apply {
            setFormatter { value -> String.format("%02d", value) }
            minValue = 0
            maxValue = 23
            setOnScrollChangeListener(View.OnScrollChangeListener { _, _, _, _, _ ->
                hours = value
            })

        }
         binding.minPicker.apply {
             setFormatter{ value -> String.format("%02d", value) }
             minValue = 0
             maxValue = 59
             setOnScrollChangeListener(View.OnScrollChangeListener { _, _, _, _, _ ->
                 minutes = value
             })
         }
        minutes = binding.minPicker.value
        hours = binding.hourPicker.value
    }

    private fun createTime(selectedHour:Int, selectedMinute:Int){
        val localTime = LocalTime.now()
        val chosenTime = LocalTime.of(selectedHour,selectedMinute)

        if (chosenTime.isAfter(localTime)) {

            val difference = Duration.between( localTime,chosenTime)

            var hourDifference = difference.toHours()
            var minuteDifference = difference.toMinutes()%60

        }
        else {
            val difference = Duration.between(localTime,chosenTime)

            var hourDifference = difference.toHours() + 23
            var minuteDifference = (difference.toMinutes() % 60) + 59


            Log.d(TAG, "createTime else: ${23 +hourDifference} : ${59+ minuteDifference}")
        }

    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}




