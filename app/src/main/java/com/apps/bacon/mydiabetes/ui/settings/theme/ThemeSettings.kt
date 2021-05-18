package com.apps.bacon.mydiabetes.ui.settings.theme

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate.*
import com.apps.bacon.mydiabetes.databinding.ActivityThemeSettingsBinding
import com.apps.bacon.mydiabetes.utilities.BaseActivity

class ThemeSettings : BaseActivity() {
    private lateinit var binding: ActivityThemeSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeSettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = this.getSharedPreferences(
            "APP_PREFERENCES",
            Context.MODE_PRIVATE
        )

        when (sharedPreferences.getInt("THEME", MODE_NIGHT_NO)) {
            MODE_NIGHT_NO -> binding.turnOffRadioButton.isChecked = true
            MODE_NIGHT_YES -> binding.turnOnRadioButton.isChecked = true
        }

        binding.turnOnRadioButton.setOnClickListener {
            binding.turnOffRadioButton.isChecked = false
            with(sharedPreferences.edit()) {
                putInt("THEME", MODE_NIGHT_YES)
                putBoolean("THEME_CHANGED", true)
                apply()
            }

            setDefaultNightMode(MODE_NIGHT_YES)
        }

        binding.turnOffRadioButton.setOnClickListener {
            binding.turnOnRadioButton.isChecked = false
            with(sharedPreferences.edit()) {
                putInt("THEME", MODE_NIGHT_NO)
                putBoolean("THEME_CHANGED", true)
                apply()
            }

            setDefaultNightMode(MODE_NIGHT_NO)
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