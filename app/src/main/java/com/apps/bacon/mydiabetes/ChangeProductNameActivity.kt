package com.apps.bacon.mydiabetes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.apps.bacon.mydiabetes.databinding.ActivityChangeProductNameBinding
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeProductNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeProductNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProductNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val productViewModel: ProductViewModel by viewModels()

        val errorEmptyMessage = resources.getString(R.string.empty_field_message_error)
        val errorAlreadyExistsNameMessage = resources.getString(R.string.product_name_exists_error_message)

        binding.changeNameButton.setOnClickListener {
            when {
                binding.productNameTextInput.text.isNullOrEmpty() -> binding.productNameTextInputLayout.error = errorEmptyMessage
                productViewModel.checkForProductExist(binding.productNameTextInput.text.toString()) -> binding.productNameTextInputLayout.error = errorAlreadyExistsNameMessage
                else -> {
                    binding.productNameTextInputLayout.error = null
                    intent.putExtra("PRODUCT_NAME", binding.productNameTextInput.text.toString().trim())
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
        finish()
    }
}