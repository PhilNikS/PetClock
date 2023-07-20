package com.lessons.petclock.stopWatchFragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Chronometer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class StopWatchFragmentViewModel: ViewModel() {
    private var _time: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val time :StateFlow<Double> = _time.asStateFlow()
    var isRun = false


    private lateinit var serviceIntent:Intent

    private lateinit var chronometer: Chronometer









}