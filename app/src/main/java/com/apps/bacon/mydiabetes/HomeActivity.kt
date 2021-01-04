package com.apps.bacon.mydiabetes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.apps.bacon.mydiabetes.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, HomeFragment()).commit()
        addTabs()

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home_nav -> {
                    binding.tabLayout.visibility = View.VISIBLE
                    binding.appBarText.text = "MyDiabetes"
                    changeFragment(HomeFragment())
                    true
                }

                R.id.add_nav -> {
                    binding.tabLayout.visibility = View.GONE
                    binding.appBarText.text = "Kalkulacja wartości"
                    changeFragment(AddProductFragment())
                    true
                }

                R.id.settings_nav -> {
                    binding.tabLayout.visibility = View.GONE
                    binding.appBarText.text = "Ustawienia"
                    changeFragment(SettingsFragment())
                    true
                }

                else -> false

            }

        }

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

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_FADE)
                .replace(binding.fragmentContainer.id, fragment)
                .commit()
    }
}