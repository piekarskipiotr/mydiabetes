package com.apps.bacon.mydiabetes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.apps.bacon.mydiabetes.databinding.ActivityProductBarcodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductBarcodeActivity : AppCompatActivity() {
    private lateinit var errorMessage: String
    private lateinit var binding: ActivityProductBarcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBarcodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        errorMessage = resources.getString(R.string.empty_field_message_error)

        if (intent.getBooleanExtra("BARCODE", true)) {
            binding.deleteButton.visibility = View.GONE
        }

        binding.saveButton.setOnClickListener {
            if (binding.barcodeTextInput.text.isNullOrEmpty())
                binding.barcodeTextInputLayout.error = errorMessage
            else {
                binding.barcodeTextInputLayout.error = null
                intent.putExtra("BARCODE", binding.barcodeTextInput.text.toString().trim())
                setResult(Activity.RESULT_OK, intent)
                finish()

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