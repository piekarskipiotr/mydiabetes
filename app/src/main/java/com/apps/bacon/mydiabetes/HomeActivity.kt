package com.apps.bacon.mydiabetes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.Tag
import com.apps.bacon.mydiabetes.data.TagRepository
import com.apps.bacon.mydiabetes.databinding.ActivityHomeBinding
import com.apps.bacon.mydiabetes.viewmodel.TagViewModel
import com.apps.bacon.mydiabetes.viewmodel.TagViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, HomeFragment()).commit()
        val database = AppDatabase.getInstance(this)
        val repository = TagRepository(database)
        val factory = TagViewModelFactory(repository)
        val tagViewModel = ViewModelProvider(this, factory).get(TagViewModel::class.java)
        tagViewModel.getAll().observe(this, {
            addTabs(it)
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

    }

    private fun addTabs(listOfTags: List<Tag>){
        for(i in listOfTags.indices)
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(listOfTags[i].name))

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