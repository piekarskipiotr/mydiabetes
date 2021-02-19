package com.apps.bacon.mydiabetes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_change_product_name.*
import javax.inject.Inject
import javax.inject.Named

class ChangeProductNameActivity : AppCompatActivity() {
    private val errorMessage: String = resources.getString(R.string.empty_field_message_error)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_product_name)

        changeNameButton.setOnClickListener {
            if(productNameTextInput.text.isNullOrEmpty())
                productNameTextInputLayout.error = errorMessage

            else{
                productNameTextInputLayout.error = null
                intent.putExtra("PRODUCT_NAME", productNameTextInput.text.toString().trim())
                setResult(Activity.RESULT_OK, intent)
                finish()

            }
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