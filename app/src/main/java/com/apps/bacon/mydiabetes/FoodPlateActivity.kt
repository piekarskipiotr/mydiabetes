package com.apps.bacon.mydiabetes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.FoodPlateAdapter
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.ProductRepository
import com.apps.bacon.mydiabetes.viewmodel.ProductModelFactory
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.activity_food_plate.*
import kotlinx.android.synthetic.main.fragment_home.*

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

    }

    private fun initRecyclerView(){
        foodRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            foodPlateAdapter = FoodPlateAdapter( this@FoodPlateActivity)
            adapter = foodPlateAdapter

        }
    }

    override fun onProductClick(productID: Int) {
        TODO("Not yet implemented")
    }
}