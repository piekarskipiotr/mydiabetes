package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ExportMealsAdapter
import com.apps.bacon.mydiabetes.adapters.ExportProductsAdapter
import com.apps.bacon.mydiabetes.databinding.ActivityExportBinding
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExportActivity : AppCompatActivity(), ExportProductsAdapter.OnExportProductListener,
    ExportMealsAdapter.OnExportMealListener {
    private lateinit var binding: ActivityExportBinding
    private val mealViewModel: MealViewModel by viewModels()
    val productsAdapter = ExportProductsAdapter(this@ExportActivity)
    val mealsAdapter = ExportMealsAdapter(this@ExportActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val productViewModel: ProductViewModel by viewModels()
        val mealViewModel: MealViewModel by viewModels()

        productViewModel.getAll().observe(this, {
            productsAdapter.updateData(it)
        })

        mealViewModel.getAll().observe(this, {
            mealsAdapter.updateData(it)
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val mAdapter = when (tab?.position) {
                    0 -> {
                        productsAdapter
                    }
                    1 -> {
                        mealsAdapter
                    }
                    else -> null
                }
                binding.recyclerView.apply {
                    adapter = mAdapter

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        addTabs()


        binding.exportButton.setOnClickListener {
            val products = productsAdapter.getDataToExport()
            val meals = mealsAdapter.getDataToExport()
        }
    }

    private fun addTabs() {
        binding.tabLayout.removeAllTabs()

        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(resources.getString(R.string.products)), 0, true
        )

        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(resources.getString(R.string.meals)), 1
        )

    }

    override fun onProductClick(productId: Int) {
        intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }

    override fun onMealClick(mealId: Int) {
        intent = Intent(this, MealActivity::class.java)
        intent.putExtra("MEAL_ID", mealId)
        startActivity(intent)
    }

    override fun onMealCheckBoxClick(mealId: Int, isChecked: Boolean) {
        mealViewModel.getProductsForMeal(mealId).observe(this, {
            if(isChecked){
                productsAdapter.addProductsThatAreConnectedWithMeal(it)
            }else{
                productsAdapter.removeProductsThatAreConnectedWithMeal(it)
            }
        })
    }
}