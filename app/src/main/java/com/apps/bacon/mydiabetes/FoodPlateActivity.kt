package com.apps.bacon.mydiabetes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.FoodPlateAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class FoodPlateActivity : AppCompatActivity(), FoodPlateAdapter.OnProductClickListener {
    private lateinit var foodPlateAdapter: FoodPlateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_plate)
        initRecyclerView()



    }

    private fun initRecyclerView(){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            foodPlateAdapter = FoodPlateAdapter(this@FoodPlateActivity)
            adapter = foodPlateAdapter

        }
    }

    override fun onProductClick(productID: Int) {
        TODO("Not yet implemented")
    }
}