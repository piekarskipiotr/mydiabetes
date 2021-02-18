package com.apps.bacon.mydiabetes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_language_settings.*
import java.util.*

class LanguageSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_settings)

        val sharedPreference = this.getSharedPreferences(
            "APP_PREFERENCES",
            Context.MODE_PRIVATE
        )
        val lang = sharedPreference.getString("APP_LANGUAGE", "pl")

        if(lang == "en"){
            englishRadioButton.isChecked = true
        }else if(lang == "pl"){
            polishRadioButton.isChecked = true
        }

        polishRadioButton.setOnClickListener {
            changeLanguage("pl")
            englishRadioButton.isChecked = false
            with(sharedPreference.edit()){
                putString("APP_LANGUAGE", "pl")
                apply()
            }

        }

        englishRadioButton.setOnClickListener {
            changeLanguage("en")
            polishRadioButton.isChecked = false
            with(sharedPreference.edit()){
                putString("APP_LANGUAGE", "en")
                apply()
            }

        }

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    @Suppress("DEPRECATION")
    private fun changeLanguage(languageCode: String){
        val config = resources.configuration
        val locale = Locale(languageCode)

        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        recreate()
    }
}