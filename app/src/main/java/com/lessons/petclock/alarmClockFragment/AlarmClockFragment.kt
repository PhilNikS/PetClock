package com.lessons.petclock.alarmClockFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lessons.petclock.MainFragmentDirections
import com.lessons.petclock.databinding.FragmentAlarmClockBinding
import kotlinx.coroutines.launch
import java.util.*


class AlarmClockFragment : Fragment() {

    private var _binding:FragmentAlarmClockBinding? = null
    private val binding get() = checkNotNull(_binding){ "is binding access?" }
    private val viewModel:AlarmCLockViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmClockBinding.inflate(inflater,container, false)

        binding.alarmContainer.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.alarms.collect{alarms->
                    binding.alarmContainer.adapter = AlarmClockAdapter(alarms){ id ->

                        findNavController().navigate(MainFragmentDirections.createNewAlarm(id,false))
                    }
                }
            }
        }

        binding.addAlarmButton.setOnClickListener{
            findNavController().navigate(MainFragmentDirections.createNewAlarm(UUID.randomUUID(), true))
//            viewModel.createNewAlarm()
        }

    }
}