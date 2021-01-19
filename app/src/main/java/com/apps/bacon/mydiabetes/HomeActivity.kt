package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.databinding.ActivityHomeBinding
import com.apps.bacon.mydiabetes.viewmodel.*
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, HomeFragment()).commit()
        val database = AppDatabase.getInstance(this)
        val repository = HomeRepository(database)
        val factory = HomeModelFactory(repository)
        val homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        homeViewModel.getAllTags().observe(this, {
            addTabs(it)
        })

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                homeViewModel.currentTag.value = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home_nav -> {
                    changeFragment(HomeFragment(), "MyDiabetes", View.VISIBLE)
                    true
                }

                R.id.add_nav -> {
                    changeFragment(AddProductFragment(), "Kalkulacja wartości", View.GONE)
                    true
                }

                R.id.settings_nav -> {
                    changeFragment(SettingsFragment(), "Ustawienia", View.GONE)
                    true
                }

                else -> false

            }

        }

        binding.searchForProduct.setOnClickListener {
            intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    }

    private fun addTabs(listOfTags: List<Tag>){
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Wszystko"), 0, true)
        for(i in listOfTags.indices)
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(listOfTags[i].name), listOfTags[i].id)

    }

    private fun changeFragment(fragment: Fragment, fragmentTitle: String, visibility: Int){
        binding.appBarText.text = fragmentTitle
        binding.tabLayout.visibility = visibility
        supportFragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_FADE)
                .replace(binding.fragmentContainer.id, fragment)
                .commit()

    }

}