package com.apps.bacon.mydiabetes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apps.bacon.mydiabetes.databinding.ActivitySaveProductBinding

class SaveProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}