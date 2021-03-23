package com.apps.bacon.mydiabetes

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apps.bacon.mydiabetes.databinding.ActivityProductBarcodeBinding
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductBarcodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBarcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBarcodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.getBooleanExtra("BARCODE", true)) {
            binding.deleteButton.visibility = View.GONE
        }
        val errorMessage = resources.getString(R.string.empty_field_message_error)
        val errorAlreadyExistsBarcodeMessage =
            resources.getString(R.string.barcode_exists_error_message)
        val productViewModel: ProductViewModel by viewModels()
        binding.saveButton.setOnClickListener {
            when {
                binding.barcodeTextInput.text.isNullOrEmpty() -> binding.barcodeTextInputLayout.error =
                    errorMessage
                productViewModel.checkForBarcodeExist(
                    binding.barcodeTextInput.text.toString().trim()
                ) -> binding.barcodeTextInputLayout.error = errorAlreadyExistsBarcodeMessage
                else -> {
                    binding.barcodeTextInputLayout.error = null
                    intent.putExtra("BARCODE", binding.barcodeTextInput.text.toString().trim())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }


        }

        binding.deleteButton.setOnClickListener {
            intent.putExtra("DELETE_BARCODE", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
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