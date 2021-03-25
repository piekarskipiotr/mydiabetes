package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.*

open class BaseActivity : AppCompatActivity(){
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val sharedPreferences = getSharedPreferences("APP_PREFERENCES",
            Context.MODE_PRIVATE)
        val defaultLang = if (Locale.getDefault().toLanguageTag() == "pl-PL" || Locale.getDefault().toLanguageTag() == "pl")
            "pl"
        else
            "en"
        val languageCode = sharedPreferences.getString("APP_LANGUAGE", defaultLang) as String

        val config = newBase!!.resources.configuration
        val locale = Locale(languageCode)
        config.setLocale(locale)
        applyOverrideConfiguration(config)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}