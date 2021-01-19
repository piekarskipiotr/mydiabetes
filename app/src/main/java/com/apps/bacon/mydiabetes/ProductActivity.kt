package com.apps.bacon.mydiabetes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.viewmodel.ProductModelFactory
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_product.*
import kotlin.math.round

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

    }

    private fun setProductInfo(){
        val measureText: String
        productName.text = product.name
        if(product.weight != null)
            measureText = "(dla masy ${product.weight} g"
        else
            measureText = "(dla masy ${product.pieces} szt."

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
    }

    private fun setProgressBar(carbohydrateExchangers: Double, proteinFatExchangers: Double){
        val sizeOfBar = carbohydrateExchangers + proteinFatExchangers
        carbohydrateExchangersBar.progress = round((carbohydrateExchangers / sizeOfBar) * 100).toInt()
        proteinFatExchangersBar.progress = round((proteinFatExchangers / sizeOfBar) * 100).toInt()

    }

    private fun addChip(label: String, ID: Int){
        tagChipContainer.removeAllViewsInLayout()
        tagChipContainer.addChip(label, ID)
        tagChipContainer[0].setOnLongClickListener {
            //for implementation: change tag
            true
        }

    }

    private fun ChipGroup.addChip(label: String, ID: Int){
        Chip(this@ProductActivity).apply {
            id = ID
            text = label
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }
}