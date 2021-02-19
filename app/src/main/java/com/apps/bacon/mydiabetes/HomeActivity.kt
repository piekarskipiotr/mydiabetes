package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.databinding.DialogFetchDataFromServerBinding
import com.apps.bacon.mydiabetes.utilities.TagTranslator
import com.apps.bacon.mydiabetes.viewmodel.*
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var lang: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportFragmentManager.beginTransaction().replace(fragmentContainer.id, HomeFragment()).commit()
        sharedPreference = this.getSharedPreferences(
        "APP_PREFERENCES",
            Context.MODE_PRIVATE
        )
        lang = sharedPreference.getString("APP_LANGUAGE", "pl") as String

        val homeViewModel: HomeViewModel by viewModels()
        val productViewModel: ProductViewModel by viewModels()
        val tagViewModel: TagViewModel by viewModels()

        TagTranslator().translate(tagViewModel, this)

        if(!homeViewModel.isErrorWithFetchData){
            fetchDataDialog(homeViewModel)
        }

        tagViewModel.getAll().observe(this, {
            addTabs(it)
        })

        productViewModel.getProductsInPlate().observe(this, {
            if (it.isEmpty()){
                notificationIconFoodPlate.visibility = View.GONE
            }else{
                notificationIconFoodPlate.visibility = View.VISIBLE
            }
        })

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                homeViewModel.currentTag.value = tab!!.tag as Int
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home_nav -> {
                    changeFragment(HomeFragment(), resources.getString(R.string.app_name), View.VISIBLE)
                    true
                }

                R.id.add_nav -> {
                    changeFragment(AddProductFragment(), resources.getString(R.string.value_calculation), View.GONE)
                    true
                }

                R.id.settings_nav -> {
                    changeFragment(SettingsFragment(), resources.getString(R.string.settings), View.GONE)
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
        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.all))
            .apply {
                   tag = 0
                   }, 0, true)

        for((j, i) in listOfTags.indices.withIndex()){
            tabLayout.addTab(tabLayout.newTab().setText(listOfTags[i].name)
                .apply {
                    tag = listOfTags[i].id
                }, j + 1
            )
        }

    }

    private fun changeFragment(fragment: Fragment, fragmentTitle: String, visibility: Int){
        appBarText.text = fragmentTitle
        tabLayout.visibility = visibility

        supportFragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_FADE)
                .replace(fragmentContainer.id, fragment)
                .commit()

    }

    private fun fetchDataDialog(homeViewModel: HomeViewModel){
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogFetchDataFromServerBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        homeViewModel.getProducts()?.observe(this, {
            val size = it.size
            var i = 1

            dialogBinding.fetchingDataProgressBar.max = size
            for(product in it){
                dialogBinding.counterText.text = "$i/$size"
                ++i

                dialogBinding.productNameText.text = product.name
                when (i) {
                    size -> {
                        homeViewModel.getProducts()!!.removeObservers(this)
                        alertDialog.dismiss()
                    }
                }
            }
        })

        alertDialog.show()
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        super.onResume()
        val oldLang = lang

        lang = sharedPreference.getString("APP_LANGUAGE", "pl") as String
        if (oldLang != lang){
            finish()
            startActivity(intent)
        }
    }
}