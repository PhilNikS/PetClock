package com.lessons.petclock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.lessons.petclock.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding:FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding){"is binding access?"}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.viewPagerMain
        val pagerAdapter = PagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayoutMain,viewPager){tab, position ->
            tab.text = getTabLayoutTitle(position)
        }.attach()

    }

    private fun getTabLayoutTitle(position:Int):String{
        return when(position){
            0-> "first"
            1-> "second"
            2-> "third"
            else ->"null"
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()

    }
}