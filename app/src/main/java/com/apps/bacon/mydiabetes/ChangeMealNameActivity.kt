package com.apps.bacon.mydiabetes

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apps.bacon.mydiabetes.databinding.ActivityChangeMealNameBinding

class ChangeMealNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeMealNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeMealNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val errorMessage = resources.getString(R.string.empty_field_message_error)

        binding.changeNameButton.setOnClickListener {
            if (binding.mealNameTextInput.text.isNullOrEmpty())
                binding.mealNameTextInputLayout.error = errorMessage
            else {
                binding.mealNameTextInputLayout.error = null
                intent.putExtra("MEAL_NAME", binding.mealNameTextInput.text.toString().trim())
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