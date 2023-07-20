package com.lessons.petclock.alarmClockFragment.createAlarmFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.lessons.petclock.R
import com.lessons.petclock.databinding.FragmentCloseCreatingDialogBinding
import java.util.zip.Inflater

class CloseCreatingDialogFragment : DialogFragment() {

    private var _binding:FragmentCloseCreatingDialogBinding? = null
    private val binding:FragmentCloseCreatingDialogBinding
    get() = checkNotNull(_binding){"is binding access?"}

    companion object{
        const val REQUEST_KEY_BACK_PRESSED = "request_key_back_pressed"
        const val BACK_PRESSED_RESULT_KEY = "request_result_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCloseCreatingDialogBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonClose.setOnClickListener{
                findNavController().popBackStack()
            }
            buttonDiscard.setOnClickListener{
//                setFragmentResult(REQUEST_KEY_BACK_PRESSED, bundleOf(BACK_PRESSED_RESULT_KEY to false))
                setResult(false)
                findNavController().popBackStack()
            }
            buttonSave.setOnClickListener {
//                setFragmentResult(REQUEST_KEY_BACK_PRESSED, bundleOf(BACK_PRESSED_RESULT_KEY to true))
                setResult(true)
                findNavController().popBackStack()
            }

        }
    }
    private fun setResult(isCreatingApply:Boolean){
        setFragmentResult(
            REQUEST_KEY_BACK_PRESSED, bundleOf(
            BACK_PRESSED_RESULT_KEY to isCreatingApply))
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}