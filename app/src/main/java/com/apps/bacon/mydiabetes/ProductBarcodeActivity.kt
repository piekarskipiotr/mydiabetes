package com.apps.bacon.mydiabetes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_product_barcode.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ProductBarcodeActivity : AppCompatActivity() {
    private val errorMessage: String = resources.getString(R.string.empty_field_message_error)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_barcode)

        if (intent.getBooleanExtra("BARCODE", true)) {
            deleteButton.visibility = View.GONE
        }

        saveButton.setOnClickListener {
            if (barcodeTextInput.text.isNullOrEmpty())
                barcodeTextInputLayout.error = errorMessage
            else {
                barcodeTextInputLayout.error = null
                intent.putExtra("BARCODE", barcodeTextInput.text.toString().trim())
                setResult(Activity.RESULT_OK, intent)
                finish()

            }
        }

        deleteButton.setOnClickListener {
            intent.putExtra("DELETE_BARCODE", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        backButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }
}