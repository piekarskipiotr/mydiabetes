package com.apps.bacon.mydiabetes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.bacon.mydiabetes.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTabs()

    }

    private fun addTabs(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sample0"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sample1"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sample2"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sample3"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sample4"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sample5"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sample6"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }
}