package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ImageAdapter
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.Image
import com.apps.bacon.mydiabetes.data.Meal
import com.apps.bacon.mydiabetes.databinding.*
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter

class MealActivity : AppCompatActivity(), ProductsAdapter.OnProductClickListener,
    ImageAdapter.OnImageClickListener {
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var binding: ActivityMealBinding
    private lateinit var meal: Meal
    private val mealViewModel: MealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mealId = intent.getIntExtra("MEAL_ID", -1)
        meal = mealViewModel.getMeal(mealId)

        setMealInfo()

        initProductsRecyclerView()
        initPhotosRecyclerView()

        binding.mealName.setOnClickListener {
            intent = Intent(this, ChangeMealNameActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_MEAL_NAME)
        }

        binding.deleteButton.setOnClickListener {

        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setMealInfo() {
        binding.mealName.text = meal.name

        pieChart(meal.carbohydrateExchangers, meal.proteinFatExchangers, meal.calories)
    }

    private fun pieChart(
        carbohydrateExchangers: Double,
        proteinFatExchangers: Double,
        calories: Double
    ) {
        val pieChart: PieChart = binding.pieChart
        val data = ArrayList<PieEntry>()
        if (carbohydrateExchangers != 0.0)
            data.add(
                PieEntry(
                    carbohydrateExchangers.toFloat(),
                    resources.getString(R.string.pie_label_carbohydrate)
                )
            )
        if (proteinFatExchangers != 0.0)
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

    private fun initProductsRecyclerView() {
        binding.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            productsAdapter = ProductsAdapter(this@MealActivity)
            adapter = productsAdapter

        }
    }

    private fun initPhotosRecyclerView() {
        binding.photosRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            imageAdapter = ImageAdapter(this@MealActivity)
            adapter = imageAdapter

        }
    }

    override fun onProductClick(productId: Int) {
        intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }

    override fun onImageLongClick(image: Image) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_MEAL_NAME -> {
                when (resultCode) {
                    RESULT_OK -> {
                        data?.let {
                            meal.name = it.getStringExtra("MEAL_NAME") as String
                            binding.mealName.text = meal.name

                            mealViewModel.update(meal)
                        }
                    }
                }
            }

        }
    }

    companion object {

        private const val REQUEST_CODE_MEAL_NAME = 6
    }

}