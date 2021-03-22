package com.apps.bacon.mydiabetes

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apps.bacon.mydiabetes.databinding.ActivityChangeMealNameBinding
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChangeMealNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeMealNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeMealNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val errorEmptyMessage = resources.getString(R.string.empty_field_message_error)
        val errorAlreadyExistsNameMessage =
            resources.getString(R.string.meal_name_exist_error_message)

        val mealViewModel: MealViewModel by viewModels()
        binding.changeNameButton.setOnClickListener {
            when {
                binding.mealNameTextInput.text.isNullOrEmpty() -> binding.mealNameTextInputLayout.error =
                    errorEmptyMessage
                mealViewModel.checkForMealExist(
                    binding.mealNameTextInput.text.toString().trim().toLowerCase(
                        Locale.ROOT
                    )
                ) -> binding.mealNameTextInputLayout.error =
                    errorAlreadyExistsNameMessage
                else -> {
                    binding.mealNameTextInputLayout.error = null
                    intent.putExtra("MEAL_NAME", binding.mealNameTextInput.text.toString().trim())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }

        binding.backButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, intent)
        super.onBackPressed()
        this.finish()
    }
}