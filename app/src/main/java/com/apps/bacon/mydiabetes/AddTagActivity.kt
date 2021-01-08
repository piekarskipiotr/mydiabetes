package com.apps.bacon.mydiabetes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apps.bacon.mydiabetes.databinding.ActivityAddTagBinding

class AddTagActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTagBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTagBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val errorMessage = "Pole nie może być puste"

        binding.addTagButton.setOnClickListener {
            if(binding.tagNameTextInput.text.isNullOrEmpty())
                binding.tagNameTextInputLayout.error = errorMessage
            else{
                binding.tagNameTextInputLayout.error = null
                intent.putExtra("TAG_NAME", binding.tagNameTextInput.text.toString().trim())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        binding.backButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }
}