package com.lessons.petclock.alarmClockFragment.createAlarmFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController

import com.lessons.petclock.databinding.FragmentSetNameDialogBinding


class SetNameDialogFragment : DialogFragment() {
    private var _binding:FragmentSetNameDialogBinding? =null
    val binding get() = checkNotNull(_binding){"Is binding access?"}

    companion object{
        const val REQUEST_KEY_SET_ALARM_NAME = "request_key_set_alarm_name"
        const val SET_ALARM_RESULT_KEY = "set_alarm_result_key"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetNameDialogBinding.inflate(inflater,container,false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var gottenText:String = ""
        binding.apply {
            editName.doOnTextChanged{text,_,_,_ ->
                gottenText = text.toString()
            }
            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }
            buttonOk.setOnClickListener {
                setFragmentResult(REQUEST_KEY_SET_ALARM_NAME, bundleOf(SET_ALARM_RESULT_KEY to gottenText))
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}