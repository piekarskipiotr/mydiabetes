package com.apps.bacon.mydiabetes.ui.settings.lang

import android.content.Context
import android.os.Bundle
import com.apps.bacon.mydiabetes.databinding.ActivityLanguageSettingsBinding
import com.apps.bacon.mydiabetes.utilities.BaseActivity
import java.util.*

class LanguageSettings : BaseActivity() {
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

        val defaultLang = if (Locale.getDefault().toLanguageTag() == "pl")
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
                putBoolean("LANGUAGE_CHANGED", true)
                putString("APP_LANGUAGE", "pl")
                apply()
            }

            recreate()
        }

        binding.englishRadioButton.setOnClickListener {
            binding.polishRadioButton.isChecked = false
            with(sharedPreference.edit()) {
                putBoolean("LANGUAGE_CHANGED", true)
                putString("APP_LANGUAGE", "en")
                apply()
            }

            recreate()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}