package com.apps.bacon.mydiabetes.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.apps.bacon.mydiabetes.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreference = this.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)
        val theme = sharedPreference.getInt("THEME", MODE_NIGHT_NO)

        val defaultLang = if (Locale.getDefault().toLanguageTag() == "pl-PL")
            "pl"
        else
            "en"

        val lang = sharedPreference.getString("APP_LANGUAGE", defaultLang) as String
        changeLanguage(lang)
        setDefaultNightMode(theme)
        super.onCreate(savedInstanceState)
        val mainViewModel: MainViewModel by viewModels()

        mainViewModel.getVersion()?.observe(this, { version ->
            val localProductsVersion = sharedPreference.getInt("PRODUCTS_VERSION", 0)

            if (version.productsVersion > localProductsVersion) {
                mainViewModel.getProducts()?.observe(this, {
                    for (product in it) {
                        mainViewModel.insert(product)
                    }
                    with(sharedPreference.edit()) {
                        putInt("PRODUCTS_VERSION", version.productsVersion)
                        apply()
                    }
                })
            }

            val localMealsVersion = sharedPreference.getInt("MEALS_VERSION", 0)

            if (version.mealsVersion > localMealsVersion) {
                mainViewModel.getMeals()?.observe(this, {
                    for (meal in it) {
                        mainViewModel.insert(meal)
                    }
                    mainViewModel.getPMJ()?.observe(this, { pmjList ->
                        for (pmj in pmjList) {
                            mainViewModel.insert(pmj)
                        }
                    })
                    with(sharedPreference.edit()) {
                        putInt("MEALS_VERSION", version.mealsVersion)
                        apply()
                    }
                })
            }
        })

        intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Suppress("DEPRECATION")
    private fun changeLanguage(languageCode: String) {
        val config = resources.configuration
        val locale = Locale(languageCode)

        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onBackPressed() {

    }
}