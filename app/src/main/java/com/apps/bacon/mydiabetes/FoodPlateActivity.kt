package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.adapters.FoodPlateAdapter
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
import kotlinx.android.synthetic.main.activity_food_plate.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.product_item_food_plate.view.*

@AndroidEntryPoint
class FoodPlateActivity : AppCompatActivity(), FoodPlateAdapter.OnProductClickListener {
    private lateinit var foodPlateAdapter: FoodPlateAdapter
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var bottomDialogBinding: DialogSummaryResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_plate)
        bottomDialogBinding = DialogSummaryResultsBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        initRecyclerView()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        productViewModel.getProductsInPlate().observe(this, {
            calculateButton.isEnabled = it.isNotEmpty()
            foodPlateAdapter.updateData(it)

        })


        object : SwipeToRemove(){
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
            bottomSheetDialog.setContentView(bottomDialogBinding.root)
            bottomSheetDialog.show()
            sumValues()
            bottomDialogBinding.clearButton.setOnClickListener {
                for(i in 0 until foodPlateAdapter.itemCount){
                    productViewModel.updateProduct(
                        foodPlateAdapter.getProduct(i).apply {
                            inFoodPlate = false
                        }
                    )
                }
                bottomSheetDialog.dismiss()
            }
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

        pieChart(carbohydrateExchangers, proteinFatExchangers, calories)
    }

    private fun pieChart(carbohydrateExchangers: Double, proteinFatExchangers: Double, calories: Double){
        val pieChart: PieChart = bottomDialogBinding.pieChart
        val data = ArrayList<PieEntry>()
        data.add(PieEntry(carbohydrateExchangers.toFloat(), "W. węglowodanowe"))
        data.add(PieEntry(proteinFatExchangers.toFloat(), "W. białkowo-tłuszczowe"))

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
        pieChart.centerText  = "$calories\nKaloryczność"
        pieChart.description.isEnabled = false

        pieChart.setDrawEntryLabels(false)

        pieChart.rotationAngle = 50f
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.animate()
    }

    override fun onProductClick(productID: Int) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productID)
        startActivity(intent)
    }


}