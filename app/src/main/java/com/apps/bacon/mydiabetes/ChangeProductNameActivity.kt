package com.apps.bacon.mydiabetes

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_change_product_name.*

class ChangeProductNameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_product_name)
        val errorMessage = "Pole nie może być puste"

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