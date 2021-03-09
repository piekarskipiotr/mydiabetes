package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.apps.bacon.mydiabetes.viewmodel.MainViewModel
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val DELAY: Long = 1000

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
        val productViewModel: ProductViewModel by viewModels()
        val mealViewModel: MealViewModel by viewModels()

        mainViewModel.getVersion()?.observe(this, { version ->
            val localProductsVersion = sharedPreference.getInt("PRODUCTS_VERSION", 0)

            if (version.productsVersion > localProductsVersion) {
                mainViewModel.getProducts()?.observe(this, {
                    for (product in it) {
                        productViewModel.insert(product)
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
                        mealViewModel.insert(meal)
                    }
                    mainViewModel.getPMJ()?.observe(this, { pmjList ->
                        for (pmj in pmjList) {
                            mealViewModel.insertPMJoin(pmj)
                        }
                    })
                    with(sharedPreference.edit()) {
                        putInt("MEALS_VERSION", version.mealsVersion)
                        apply()
                    }
                })

            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }, DELAY)

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