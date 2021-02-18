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
import com.apps.bacon.mydiabetes.api.ProductsAPI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DELAY: Long = 1000

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var api: ProductsAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreference = this.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)
        val theme = sharedPreference.getInt("THEME", MODE_NIGHT_NO)
        setDefaultNightMode(theme)
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }, DELAY)

    }

    override fun onBackPressed() {
    }
}