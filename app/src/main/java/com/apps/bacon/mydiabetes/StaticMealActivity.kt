package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.adapters.StaticImageAdapter
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.databinding.ActivityStaticMealBinding
import com.apps.bacon.mydiabetes.databinding.DialogReportBinding
import com.apps.bacon.mydiabetes.viewmodel.ImageViewModel
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StaticMealActivity : BaseActivity(), ProductsAdapter.OnProductClickListener {
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var staticImageAdapter: StaticImageAdapter
    private lateinit var binding: ActivityStaticMealBinding
    private lateinit var staticMeal: Meal
    private val mealViewModel: MealViewModel by viewModels()

    @Inject
    lateinit var storageReference: StorageReference

    @Inject
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaticMealBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mealId = intent.getIntExtra("MEAL_ID", -1)
        staticMeal = mealViewModel.getMeal(mealId)

        setMealInfo()

        initProductsRecyclerView()
        initPhotosRecyclerView()

        val imageViewModel: ImageViewModel by viewModels()

        imageViewModel.getURL(storageReference, "meal", staticMeal.name)?.observe(this, {
            staticImageAdapter.updateData(it)
        })

        binding.reportButton.setOnClickListener {
            reportDialog()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun reportDialog() {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogReportBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tagErrorCheckBox.visibility = View.GONE

        dialogBinding.nameErrorCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogBinding.valuesErrorCheckBox.isChecked = false
                dialogBinding.photosErrorCheckBox.isChecked = false

            }
        }

        dialogBinding.valuesErrorCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogBinding.nameErrorCheckBox.isChecked = false
                dialogBinding.photosErrorCheckBox.isChecked = false

            }
        }

        dialogBinding.photosErrorCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogBinding.nameErrorCheckBox.isChecked = false
                dialogBinding.valuesErrorCheckBox.isChecked = false

            }
        }

        dialogBinding.reportButton.setOnClickListener {
            val errorMassage = when {
                dialogBinding.nameErrorCheckBox.isChecked -> dialogBinding.nameErrorCheckBox.text.toString()
                dialogBinding.valuesErrorCheckBox.isChecked -> dialogBinding.valuesErrorCheckBox.text.toString()
                dialogBinding.photosErrorCheckBox.isChecked -> dialogBinding.photosErrorCheckBox.text.toString()
                else -> null
            }

            if (errorMassage != null) {
                val productReference = database.child("Meal Errors/${staticMeal.name}/${System.currentTimeMillis()}/")
                productReference.setValue(errorMassage)
                alertDialog.dismiss()
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.report_error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun setMealInfo() {
        binding.mealName.text = staticMeal.name

        mealViewModel.getProductsForMeal(staticMeal.name).observe(this, {
            productsAdapter.updateData(it)
        })

        pieChart(
            staticMeal.carbohydrateExchangers,
            staticMeal.proteinFatExchangers,
            staticMeal.calories
        )
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
            productsAdapter = ProductsAdapter(this@StaticMealActivity)
            adapter = productsAdapter

        }
    }

    private fun initPhotosRecyclerView() {
        binding.photosRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            staticImageAdapter = StaticImageAdapter()
            adapter = staticImageAdapter

        }
    }

    override fun onProductClick(productId: Int, isEditable: Boolean) {
        intent = Intent(this, StaticProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}