package com.apps.bacon.mydiabetes.ui.barcode

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.databinding.ActivityProductBarcodeBinding
import com.apps.bacon.mydiabetes.utilities.BaseActivity
import com.apps.bacon.mydiabetes.ui.product.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductBarcodeActivity : BaseActivity() {
    private lateinit var binding: ActivityProductBarcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBarcodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val currentBarcode: String? = intent.getStringExtra("BARCODE")
        if (currentBarcode != null) {
            binding.deleteButton.visibility = View.GONE
            binding.barcodeTextInput.setText(currentBarcode)
        }

        val errorMessage = resources.getString(R.string.empty_field_message_error)

        val errorAlreadyExistsBarcodeMessage =
            resources.getString(R.string.barcode_exists_error_message)

        val productBarcodeViewModel: ProductBarcodeViewModel by viewModels()

        binding.saveButton.setOnClickListener {
            when {
                binding.barcodeTextInput.text.isNullOrEmpty() -> binding.barcodeTextInputLayout.error =
                    errorMessage
                productBarcodeViewModel.checkForBarcodeExist(
                    binding.barcodeTextInput.text.toString().trim(), currentBarcode
                ) -> binding.barcodeTextInputLayout.error = errorAlreadyExistsBarcodeMessage
                binding.barcodeTextInput.text.toString().trim() == currentBarcode -> onBackPressed()
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