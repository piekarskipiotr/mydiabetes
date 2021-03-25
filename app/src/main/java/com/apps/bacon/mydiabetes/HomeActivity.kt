package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.apps.bacon.mydiabetes.data.entities.Tag
import com.apps.bacon.mydiabetes.databinding.ActivityHomeBinding
import com.apps.bacon.mydiabetes.utilities.TagTranslator
import com.apps.bacon.mydiabetes.viewmodel.HomeViewModel
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.apps.bacon.mydiabetes.viewmodel.TagViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var binding: ActivityHomeBinding
    private val tagViewModel: TagViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, HomeFragment())
            .commit()
        sharedPreference = this.getSharedPreferences(
            "APP_PREFERENCES",
            Context.MODE_PRIVATE
        )

        val homeViewModel: HomeViewModel by viewModels()
        val productViewModel: ProductViewModel by viewModels()

        tagViewModel.getAll().observe(this, {
            addTabs(it)
        })

        productViewModel.getProductsInPlate().observe(this, {
            if (it.isEmpty()) {
                binding.notificationIconFoodPlate.visibility = View.GONE
            } else {
                binding.notificationIconFoodPlate.visibility = View.VISIBLE
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                homeViewModel.currentTag.value = tab!!.tag as Int
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav -> {
                    changeFragment(
                        HomeFragment(),
                        resources.getString(R.string.app_name),
                        View.VISIBLE
                    )
                    true
                }

                R.id.add_nav -> {
                    changeFragment(
                        AddProductFragment(),
                        resources.getString(R.string.value_calculation),
                        View.GONE
                    )
                    true
                }

                R.id.meals_nav -> {
                    changeFragment(MealsFragment(), resources.getString(R.string.meals), View.GONE)
                    true
                }

                R.id.settings_nav -> {
                    changeFragment(
                        SettingsFragment(),
                        resources.getString(R.string.settings),
                        View.GONE
                    )
                    true
                }

                else -> false
            }
        }

        binding.searchForProduct.setOnClickListener {
            intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.foodPlate.setOnClickListener {
            intent = Intent(this, FoodPlateActivity::class.java)
            startActivity(intent)
        }

    }

    private fun addTabs(listOfTags: List<Tag>) {
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(resources.getString(R.string.all))
                .apply {
                    tag = 0
                }, 0, true
        )

        for ((j, i) in listOfTags.indices.withIndex()) {
            binding.tabLayout.addTab(
                binding.tabLayout.newTab().setText(listOfTags[i].name)
                    .apply {
                        tag = listOfTags[i].id
                    }, j + 1
            )
        }
    }

    private fun changeFragment(fragment: Fragment, fragmentTitle: String, visibility: Int) {
        binding.appBarText.text = fragmentTitle
        binding.tabLayout.visibility = visibility

        supportFragmentManager.beginTransaction()
            .setTransition(TRANSIT_FRAGMENT_FADE)
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    override fun onBackPressed() {}

    override fun onStart() {
        super.onStart()

        val isActivityAfterLangChange =
            sharedPreference.getBoolean("ACTIVITY_AFTER_LANGUAGE_CHANGE", false)
        if (isActivityAfterLangChange) {
            with(sharedPreference.edit()) {
                putBoolean("ACTIVITY_AFTER_LANGUAGE_CHANGE", false)
                apply()
            }
            TagTranslator().translate(tagViewModel, this)
        }
    }

    override fun onResume() {
        super.onResume()

        val isLangChanged = sharedPreference.getBoolean("LANGUAGE_CHANGED", false)
        if (isLangChanged) {
            with(sharedPreference.edit()) {
                putBoolean("LANGUAGE_CHANGED", false)
                putBoolean("ACTIVITY_AFTER_LANGUAGE_CHANGE", true)
                apply()
            }
            recreate()
            binding.bottomNavigation.selectedItemId = R.id.home_nav
        }

        if (sharedPreference.getBoolean("THEME_CHANGED", false)) {
            with(sharedPreference.edit()) {
                putBoolean("THEME_CHANGED", false)
                apply()
            }
            binding.bottomNavigation.selectedItemId = R.id.settings_nav
        }
    }
}