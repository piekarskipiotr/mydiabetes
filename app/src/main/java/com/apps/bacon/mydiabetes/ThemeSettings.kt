package com.apps.bacon.mydiabetes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate.*
import kotlinx.android.synthetic.main.activity_theme_settings.*

class ThemeSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_settings)

        val sharedPreference = this.getSharedPreferences(
            "PREFERENCE_NAME",
            Context.MODE_PRIVATE
        )
        val theme = sharedPreference.getInt("THEME", MODE_NIGHT_NO)

        if(theme == MODE_NIGHT_NO){
            turnOffRadioButton.isChecked = true
        }else if(theme == MODE_NIGHT_YES){
            turnOnRadioButton.isChecked = true
        }

        if(turnOnRadioButton.isChecked){
            turnOnRadioButton.isEnabled = false
        }else if(turnOffRadioButton.isChecked){
            turnOffRadioButton.isEnabled = false
        }

        turnOnRadioButton.setOnClickListener {
            turnOffRadioButton.isChecked = false
            with(sharedPreference.edit()) {
                putInt("THEME", MODE_NIGHT_YES)
                apply()
            }
            setDefaultNightMode(MODE_NIGHT_YES)
        }

        turnOffRadioButton.setOnClickListener {
            turnOnRadioButton.isChecked = false
            with(sharedPreference.edit()) {
                putInt("THEME", MODE_NIGHT_NO)
                apply()
            }
            setDefaultNightMode(MODE_NIGHT_NO)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}