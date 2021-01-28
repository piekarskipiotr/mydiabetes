package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.viewmodel.*
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportFragmentManager.beginTransaction().replace(fragmentContainer.id, HomeFragment()).commit()

        val homeViewModel: HomeViewModel by viewModels()

        homeViewModel.getAllTags().observe(this, {
            addTabs(it)
        })

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                homeViewModel.currentTag.value = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        bottomNavigation.setOnNavigationItemSelectedListener { item ->
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

        searchForProduct.setOnClickListener {
            intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        foodPlate.setOnClickListener {
            intent = Intent(this, FoodPlateActivity::class.java)
            startActivity(intent)
        }

    }

    private fun addTabs(listOfTags: List<Tag>){
        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setText("Wszystko"), 0, true)
        for(i in listOfTags.indices)
            tabLayout.addTab(tabLayout.newTab().setText(listOfTags[i].name), listOfTags[i].id)

    }

    private fun changeFragment(fragment: Fragment, fragmentTitle: String, visibility: Int){
        appBarText.text = fragmentTitle
        tabLayout.visibility = visibility
        supportFragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_FADE)
                .replace(fragmentContainer.id, fragment)
                .commit()

    }

    override fun onBackPressed() {}

}