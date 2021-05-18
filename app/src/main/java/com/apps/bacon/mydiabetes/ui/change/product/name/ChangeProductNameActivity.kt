package com.apps.bacon.mydiabetes.ui.change.product.name

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.databinding.ActivityChangeProductNameBinding
import com.apps.bacon.mydiabetes.utilities.BaseActivity
import com.apps.bacon.mydiabetes.ui.product.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeProductNameActivity : BaseActivity() {
    private lateinit var binding: ActivityChangeProductNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProductNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val currentName: String? = intent.getStringExtra("CURRENT_NAME")
        if (currentName != null) {
            binding.productNameTextInput.setText(currentName)
        }

        val changeProductNameViewModel: ChangeProductNameViewModel by viewModels()

        val errorEmptyMessage = resources.getString(R.string.empty_field_message_error)
        val errorAlreadyExistsNameMessage =
            resources.getString(R.string.product_name_exists_error_message)

        binding.changeNameButton.setOnClickListener {
            when {
                binding.productNameTextInput.text.isNullOrEmpty() -> binding.productNameTextInputLayout.error =
                    errorEmptyMessage
                changeProductNameViewModel.checkForProductExist(
                    binding.productNameTextInput.text.toString().trim().lowercase(),
                    currentName
                ) -> binding.productNameTextInputLayout.error = errorAlreadyExistsNameMessage
                binding.productNameTextInput.text.toString()
                    .trim() == currentName -> onBackPressed()
                else -> {
                    binding.productNameTextInputLayout.error = null
                    intent.putExtra(
                        "PRODUCT_NAME",
                        binding.productNameTextInput.text.toString().trim()
                    )
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