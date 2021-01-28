package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.databinding.DialogDeleteProductBinding
import com.apps.bacon.mydiabetes.databinding.DialogManagerTagBinding
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.backButton
import kotlinx.android.synthetic.main.activity_product.deleteButton
import kotlinx.android.synthetic.main.dialog_delete_product.*
import kotlin.math.round

private const val REQUEST_CODE_GET_TAG = 1
private const val REQUEST_CODE_GET_BARCODE = 2

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {
    private lateinit var product: Product
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        product = productViewModel.getProduct(productId)
        setProductInfo()

        scanBarcodeButton.setOnClickListener {
            intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("BARCODE", true)
            startActivityForResult(intent, REQUEST_CODE_GET_BARCODE)
        }

        manualBarcode.setOnClickListener {
            intent = Intent(this, ProductBarcodeActivity::class.java)
            intent.putExtra("BARCODE", true)
            startActivityForResult(intent, REQUEST_CODE_GET_BARCODE)
        }

        addButton.setOnClickListener {
            productViewModel.updateProduct(product.apply {
                inFoodPlate = true
            })
        }

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

        if(product.tag == null)
            addChip("Ustaw tag", 0)
        else{
            val tag = productViewModel.getTag(product.tag!!)
            addChip(tag.name, tag.id)
        }

        if(product.barcode == null){
            manualBarcode.text = "Wprowadź barcode ręcznie"
        }else{
            manualBarcode.text = product.barcode
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

            REQUEST_CODE_GET_BARCODE -> {
                if(resultCode == RESULT_OK){
                    product.barcode = data!!.getStringExtra("BARCODE")
                    setProductInfo()
                    productViewModel.updateProduct(product)

                }
            }

        }
    }
}