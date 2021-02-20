package com.apps.bacon.mydiabetes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apps.bacon.mydiabetes.databinding.ActivityChangeProductNameBinding

class ChangeProductNameActivity : AppCompatActivity() {
    private lateinit var errorMessage: String
    private lateinit var binding: ActivityChangeProductNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProductNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        errorMessage = resources.getString(R.string.empty_field_message_error)

        binding.changeNameButton.setOnClickListener {
            if (binding.productNameTextInput.text.isNullOrEmpty())
                binding.productNameTextInputLayout.error = errorMessage
            else {
                binding.productNameTextInputLayout.error = null
                intent.putExtra("PRODUCT_NAME", binding.productNameTextInput.text.toString().trim())
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