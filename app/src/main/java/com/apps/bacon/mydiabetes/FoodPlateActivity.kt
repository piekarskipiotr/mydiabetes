package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.adapters.FoodPlateAdapter
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.ProductRepository
import com.apps.bacon.mydiabetes.utilities.SwipeToRemove
import com.apps.bacon.mydiabetes.viewmodel.ProductModelFactory
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_food_plate.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.product_item_food_plate.view.*

class FoodPlateActivity : AppCompatActivity(), FoodPlateAdapter.OnProductClickListener {
    private lateinit var foodPlateAdapter: FoodPlateAdapter
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_plate)
        val database = AppDatabase.getInstance(this)
        val repository = ProductRepository(database)
        val factory = ProductModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        initRecyclerView()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        productViewModel.getProductsInPlate().observe(this, {
            foodPlateAdapter.updateData(it)

        })

        object : SwipeToRemove(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                productViewModel.updateProduct(
                    foodPlateAdapter.getProduct(viewHolder.adapterPosition).apply {
                        inFoodPlate = false
                    }
                )
                Toast.makeText(this@FoodPlateActivity, "Usunięto!", Toast.LENGTH_SHORT).show()
            }
        }.apply {
            ItemTouchHelper(this).attachToRecyclerView(foodRecyclerView)
        }

        calculateButton.setOnClickListener {
            sumValues()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView(){
        foodRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            foodPlateAdapter = FoodPlateAdapter( this@FoodPlateActivity)
            adapter = foodPlateAdapter
        }
    }

    private fun sumValues(){
        var carbohydrateExchangers = 0.0
        var proteinFatExchangers = 0.0
        var calories = 0.0
        for(i in 0 until foodPlateAdapter.itemCount){
            val view = foodRecyclerView.findViewHolderForAdapterPosition(i)!!.itemView
            carbohydrateExchangers += view.carbohydrateExchangers.text.toString().toDouble()
            proteinFatExchangers += view.proteinFatExchangers.text.toString().toDouble()
            calories += view.calories.text.toString().toDouble()

        }
    }

    override fun onProductClick(productID: Int) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productID)
        startActivity(intent)
    }


}