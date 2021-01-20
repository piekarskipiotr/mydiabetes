package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.databinding.DialogDeleteProductBinding
import com.apps.bacon.mydiabetes.databinding.DialogManagerTagBinding
import com.apps.bacon.mydiabetes.viewmodel.ProductModelFactory
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.backButton
import kotlinx.android.synthetic.main.activity_product.deleteButton
import kotlinx.android.synthetic.main.dialog_delete_product.*
import kotlin.math.round

private const val REQUEST_CODE_GET_TAG = 1
class ProductActivity : AppCompatActivity() {
    private lateinit var product: Product
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val database = AppDatabase.getInstance(this)
        val repository = ProductRepository(database)
        val factory = ProductModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        product = productViewModel.getProduct(productId)
        setProductInfo()

        backButton.setOnClickListener {
            onBackPressed()
        }

        deleteButton.setOnClickListener {
            dialogDeleteProduct()
        }
    }

    private fun setProductInfo(){
        productName.text = product.name
        val measureText: String = if(product.weight != null)
            "(dla masy ${product.weight} g)"
        else
            "(dla masy ${product.pieces} szt.)"

        measureOfValues.text = measureText
        measureOfExchangers.text = measureText

        carbohydrates.text = product.carbohydrates.toString()
        calories.text = product.calories.toString()
        if(product.protein != null){
            protein.text = product.protein.toString()
            fat.text = product.fat.toString()
        }else{
            proteinContainer.visibility = View.GONE
            fatContainer.visibility = View.GONE
        }

        carbohydrateExchangers.text = product.carbohydrateExchangers.toString()
        proteinFatExchangers.text = product.proteinFatExchangers.toString()
        setProgressBar(product.carbohydrateExchangers, product.proteinFatExchangers)

        Log.d("CHUJ", product.tag.toString())
        if(product.tag == null)
            addChip("Ustaw tag", 0)
        else{
            val tag = productViewModel.getTag(product.tag!!)
            addChip(tag.name, tag.id)

        }
    }

    private fun setProgressBar(carbohydrateExchangers: Double, proteinFatExchangers: Double){
        val sizeOfBar = carbohydrateExchangers + proteinFatExchangers
        carbohydrateExchangersBar.progress = round((carbohydrateExchangers / sizeOfBar) * 100).toInt()
        proteinFatExchangersBar.progress = round((proteinFatExchangers / sizeOfBar) * 100).toInt()

    }

    private fun addChip(label: String, ID: Int){
        tagChipContainer.removeAllViewsInLayout()
        tagChipContainer.addChip(label, ID)
        tagChipContainer[0].setOnClickListener {
            if(product.tag == null){
                intent = Intent(this, AddTagActivity::class.java)
                intent.putExtra("TAG_MANAGER", true)
                startActivityForResult(intent, REQUEST_CODE_GET_TAG)
            }else
                dialogManagerTag(label)

        }

    }

    private fun ChipGroup.addChip(label: String, ID: Int){
        Chip(this@ProductActivity).apply {
            id = ID
            text = label
            isClickable = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }


    private fun dialogDeleteProduct(){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogDeleteProductBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.productNameText.text = product.name

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            productViewModel.deleteProduct(product)
            alertDialog.dismiss()
            finish()
        }
        alertDialog.show()
    }

    private fun dialogManagerTag(tagName: String){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogManagerTagBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tagNameText.text = tagName

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            product.tag = null
            setProductInfo()
            alertDialog.dismiss()
        }

        dialogBinding.changeButton.setOnClickListener {
            intent = Intent(this, AddTagActivity::class.java)
            intent.putExtra("TAG_MANAGER", true)
            startActivityForResult(intent, REQUEST_CODE_GET_TAG)
            alertDialog.dismiss()

        }
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_GET_TAG -> {
                if(resultCode == RESULT_OK){
                    product.tag = data!!.getIntExtra("TAG_ID", -1)
                    setProductInfo()
                    productViewModel.updateProduct(product)

                }
            }

        }
    }
}