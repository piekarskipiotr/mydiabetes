package com.apps.bacon.mydiabetes

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apps.bacon.mydiabetes.databinding.ActivityLanguageSettingsBinding
import java.util.*

class LanguageSettings : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreference = this.getSharedPreferences(
            "APP_PREFERENCES",
            Context.MODE_PRIVATE
        )

        val defaultLang = if (Locale.getDefault().toLanguageTag() == "pl-PL")
            "pl"
        else
            "en"

        when (sharedPreference.getString("APP_LANGUAGE", defaultLang)) {
            "en" -> binding.englishRadioButton.isChecked = true
            "pl" -> binding.polishRadioButton.isChecked = true
        }

        binding.polishRadioButton.setOnClickListener {
            binding.englishRadioButton.isChecked = false
            with(sharedPreference.edit()) {
                putString("APP_LANGUAGE", "pl")
                apply()
            }

            changeLanguage("pl")

        }

        binding.englishRadioButton.setOnClickListener {
            binding.polishRadioButton.isChecked = false
            with(sharedPreference.edit()) {
                putString("APP_LANGUAGE", "en")
                apply()
            }

            changeLanguage("en")
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    @Suppress("DEPRECATION")
    private fun changeLanguage(languageCode: String) {
        val config = resources.configuration
        val locale = Locale(languageCode)

        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        recreate()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}