package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.adapters.FoodPlateAdapter
import com.apps.bacon.mydiabetes.databinding.ActivityFoodPlateBinding
import com.apps.bacon.mydiabetes.databinding.DialogSummaryResultsBinding
import com.apps.bacon.mydiabetes.utilities.SwipeToRemove
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodPlateActivity : AppCompatActivity(), FoodPlateAdapter.OnProductClickListener {
    private lateinit var foodPlateAdapter: FoodPlateAdapter
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var bottomDialogBinding: DialogSummaryResultsBinding
    private lateinit var binding: ActivityFoodPlateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodPlateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        bottomDialogBinding = DialogSummaryResultsBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        initRecyclerView()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        productViewModel.getProductsInPlate().observe(this, {
            binding.calculateButton.isEnabled = it.isNotEmpty()
            foodPlateAdapter.updateData(it)

        })

        object : SwipeToRemove() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                productViewModel.update(
                    foodPlateAdapter.getProduct(viewHolder.adapterPosition).apply {
                        inFoodPlate = false
                    }
                )
                Toast.makeText(
                    this@FoodPlateActivity,
                    resources.getString(R.string.removed_exclamation_mark),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.apply {
            ItemTouchHelper(this).attachToRecyclerView(binding.foodRecyclerView)
        }

        binding.calculateButton.setOnClickListener {
            bottomSheetDialog.setContentView(bottomDialogBinding.root)
            bottomSheetDialog.show()
            sumValues()
            bottomDialogBinding.clearButton.setOnClickListener {
                for (i in 0 until foodPlateAdapter.itemCount) {
                    productViewModel.update(
                        foodPlateAdapter.getProduct(i).apply {
                            inFoodPlate = false
                        }
                    )
                }
                bottomSheetDialog.dismiss()
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        binding.foodRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            foodPlateAdapter = FoodPlateAdapter(this@FoodPlateActivity)
            adapter = foodPlateAdapter
        }
    }

    private fun sumValues() {
        var carbohydrateExchangers = 0.0
        var proteinFatExchangers = 0.0
        var calories = 0.0
        for (i in 0 until foodPlateAdapter.itemCount) {
            carbohydrateExchangers += foodPlateAdapter.getCarbohydrateExchangers(i)
            proteinFatExchangers += foodPlateAdapter.getProteinFat(i)
            calories += foodPlateAdapter.getCalories(i)!!

        }

        pieChart(carbohydrateExchangers, proteinFatExchangers, calories)
    }

    private fun pieChart(
        carbohydrateExchangers: Double,
        proteinFatExchangers: Double,
        calories: Double
    ) {
        val pieChart: PieChart = bottomDialogBinding.pieChart
        val data = ArrayList<PieEntry>()
        data.add(
            PieEntry(
                carbohydrateExchangers.toFloat(),
                resources.getString(R.string.pie_label_carbohydrate)
            )
        )
        data.add(
            PieEntry(
                proteinFatExchangers.toFloat(),
                resources.getString(R.string.pie_label_protein_fat)
            )
        )

        val dataSet = PieDataSet(data, "")
        dataSet.setColors(
            ContextCompat.getColor(this, R.color.strong_yellow),
            ContextCompat.getColor(this, R.color.blue_purple)
        )

        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.black)
        dataSet.valueTextSize = 16f


        val pieData = PieData(dataSet)
        pieData.setValueFormatter(DefaultValueFormatter(1))

        pieChart.data = pieData
        pieChart.centerText = "$calories\n${resources.getString(R.string.calories_value)}"
        pieChart.description.isEnabled = false

        pieChart.legend.textColor = ContextCompat.getColor(this, R.color.independent)

        pieChart.setDrawEntryLabels(false)

        pieChart.rotationAngle = 50f
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.animate()
    }

    override fun onProductClick(productId: Int) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }


}