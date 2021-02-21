package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val DELAY: Long = 1000

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreference = this.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)
        val theme = sharedPreference.getInt("THEME", MODE_NIGHT_NO)
        Log.d("CHUJ", Locale.getDefault().toLanguageTag())
        val defaultLang = if(Locale.getDefault().toLanguageTag() == "pl-PL")
            "pl"
        else
            "en"

        val lang = sharedPreference.getString("APP_LANGUAGE", defaultLang) as String
        changeLanguage(lang)
        setDefaultNightMode(theme)
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

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