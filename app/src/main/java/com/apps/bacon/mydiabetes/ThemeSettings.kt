package com.apps.bacon.mydiabetes

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import com.apps.bacon.mydiabetes.databinding.ActivityThemeSettingsBinding

class ThemeSettings : AppCompatActivity() {
    private lateinit var binding: ActivityThemeSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeSettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreference = this.getSharedPreferences(
            "APP_PREFERENCES",
            Context.MODE_PRIVATE
        )

        when (sharedPreference.getInt("THEME", MODE_NIGHT_NO)) {
            MODE_NIGHT_NO -> binding.turnOffRadioButton.isChecked = true
            MODE_NIGHT_YES -> binding.turnOnRadioButton.isChecked = true
        }

        binding.turnOnRadioButton.setOnClickListener {
            binding.turnOffRadioButton.isChecked = false
            with(sharedPreference.edit()) {
                putInt("THEME", MODE_NIGHT_YES)
                putBoolean("THEME_HAS_CHANGED", true)
                apply()
            }
            setDefaultNightMode(MODE_NIGHT_YES)
        }

        binding.turnOffRadioButton.setOnClickListener {
            binding.turnOnRadioButton.isChecked = false
            with(sharedPreference.edit()) {
                putInt("THEME", MODE_NIGHT_NO)
                putBoolean("THEME_HAS_CHANGED", true)
                apply()
            }
            setDefaultNightMode(MODE_NIGHT_NO)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}