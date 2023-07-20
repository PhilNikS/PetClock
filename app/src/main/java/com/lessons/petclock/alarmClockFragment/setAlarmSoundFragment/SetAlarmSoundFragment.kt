package com.lessons.petclock.alarmClockFragment.setAlarmSoundFragment


import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import androidx.recyclerview.widget.LinearLayoutManager
import com.lessons.petclock.CreateAlarmViewModel
import com.lessons.petclock.CreateAlarmViewModelFactory

import com.lessons.petclock.databinding.FragmentSetAlarmSoundBinding
import kotlinx.coroutines.launch

import kotlin.collections.HashMap

private const val TAG = "SetAlarmSoundFragment"
class SetAlarmSoundFragment : Fragment() {
    private var _binding: FragmentSetAlarmSoundBinding? = null
    private val binding get() = checkNotNull(_binding){"is binding access?"}

    private var _tone:Ringtone? = null
    private val tone get() = checkNotNull( _tone)

    private val args: SetAlarmSoundFragmentArgs by navArgs()

    private var alarmSoundTitle:String = ""






    companion object{
        const val REQUEST_KEY_SET_ALARM_SOUND_TITLE = "REQUEST_KEY_SET_ALARM_SOUND_TITLE"
        const val SET_ALARM_SOUND_TITLE_RESULT_KEY = "SET_ALARM_SOUND_TITLE_RESULT_KEY"

        fun getMapOfAlarmSounds(context: Context): HashMap<String,String> {

            val soundMap: HashMap<String,String> = HashMap()

            val ringtoneManager = RingtoneManager(context)
            ringtoneManager.setType(RingtoneManager.TYPE_ALARM)
            val cursor = ringtoneManager.cursor

            while (cursor.moveToNext()) {
                val id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
                val uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
                val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)

                soundMap[title] = "$uri/$id"

            }

            return soundMap
        }

        fun getListOfAlarmSounds(context: Context): List<String> = getMapOfAlarmSounds(context).keys.toList().sorted()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetAlarmSoundBinding.inflate(inflater, container, false)
        binding.soundContainer.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmSoundTitle = args.soundTitle

        val soundMap = getMapOfAlarmSounds(requireContext())
        val adapter = AlarmSoundAdapter(getListOfAlarmSounds(requireContext()),alarmSoundTitle){ soundTitle ->

            soundMap[soundTitle]?.let {
                playAlarmSound(it)
            }
        }

        binding.soundContainer.adapter = adapter

        requireActivity().onBackPressedDispatcher.addCallback {

            setFragmentResult(REQUEST_KEY_SET_ALARM_SOUND_TITLE, bundleOf(
                SET_ALARM_SOUND_TITLE_RESULT_KEY to adapter.getLastClickedTitle()))

            if(_tone!=null){
                if(tone.isPlaying){
                    tone.stop()
                }
            }
            findNavController().popBackStack()
        }



    }



    private fun playAlarmSound(path:String){


        if(_tone!=null&&tone.isPlaying) tone.stop()

        try {
            _tone = RingtoneManager.getRingtone(requireContext(), Uri.parse(path))
            tone.play()
        }
        catch (ex:Exception){
            Log.d(TAG,"$ex")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _tone = null
    }
}