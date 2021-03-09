package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.adapters.FoodPlateAdapter
import com.apps.bacon.mydiabetes.adapters.StaticFoodPlateAdapter
import com.apps.bacon.mydiabetes.data.entities.HybridProductMealJoin
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.data.entities.ProductMealJoin
import com.apps.bacon.mydiabetes.databinding.ActivityFoodPlateBinding
import com.apps.bacon.mydiabetes.databinding.DialogMealNameBinding
import com.apps.bacon.mydiabetes.databinding.DialogSummaryResultsBinding
import com.apps.bacon.mydiabetes.utilities.Calculations
import com.apps.bacon.mydiabetes.utilities.SwipeToRemove
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodPlateActivity : AppCompatActivity(), FoodPlateAdapter.OnProductClickListener,
    StaticFoodPlateAdapter.OnProductClickListener {
    private lateinit var foodPlateAdapter: FoodPlateAdapter
    private lateinit var staticFoodPlateAdapter: StaticFoodPlateAdapter
    private lateinit var conAdapter: ConcatAdapter
    private val productViewModel: ProductViewModel by viewModels()
    private val mealViewModel: MealViewModel by viewModels()
    private lateinit var bottomDialogBinding: DialogSummaryResultsBinding
    private lateinit var binding: ActivityFoodPlateBinding
    private var newMealId = 0
    private var carbohydrateExchangers = 0.0
    private var proteinFatExchangers = 0.0
    private var calories = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodPlateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        bottomDialogBinding = DialogSummaryResultsBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        newMealId = mealViewModel.getLastId().inc()

        initRecyclerView()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        productViewModel.getProductsInPlate().observe(this, {
            foodPlateAdapter.updateData(it)
            binding.calculateButton.isEnabled = conAdapter.itemCount != 0
        })

        productViewModel.getStaticProductsInPlate().observe(this, {
            staticFoodPlateAdapter.updateData(it)
            binding.calculateButton.isEnabled = conAdapter.itemCount != 0
        })

        object : SwipeToRemove() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                try {
                    productViewModel.update(
                        foodPlateAdapter.getProduct(viewHolder.bindingAdapterPosition).apply {
                            inFoodPlate = false
                        }
                    )
                } catch (e: Exception) {
                    productViewModel.update(
                        staticFoodPlateAdapter.getProduct(viewHolder.bindingAdapterPosition).apply {
                            inFoodPlate = false
                        }
                    )
                }

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
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
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
                for (i in 0 until staticFoodPlateAdapter.itemCount) {
                    productViewModel.update(
                        staticFoodPlateAdapter.getProduct(i).apply {
                            inFoodPlate = false
                        }
                    )
                }
                bottomSheetDialog.dismiss()
            }

            bottomDialogBinding.saveMealButton.setOnClickListener {
                bottomSheetDialog.dismiss()
                val bottomDialogMealNameBinding = DialogMealNameBinding.inflate(layoutInflater)
                bottomSheetDialog.setContentView(bottomDialogMealNameBinding.root)
                bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetDialog.show()

                val errorEmptyMessage = resources.getString(R.string.empty_field_message_error)
                val errorAlreadyExistsNameMessage =
                    resources.getString(R.string.meal_name_exist_error_message)

                bottomDialogMealNameBinding.saveNameButton.setOnClickListener {
                    when {
                        bottomDialogMealNameBinding.mealNameTextInput.text.isNullOrEmpty() -> bottomDialogMealNameBinding.mealNameTextInputLayout.error =
                            errorEmptyMessage
                        mealViewModel.checkForMealExist(bottomDialogMealNameBinding.mealNameTextInput.text.toString()) -> bottomDialogMealNameBinding.mealNameTextInputLayout.error =
                            errorAlreadyExistsNameMessage
                        mealViewModel.checkForStaticMealExist(bottomDialogMealNameBinding.mealNameTextInput.text.toString()) -> bottomDialogMealNameBinding.mealNameTextInputLayout.error =
                            errorAlreadyExistsNameMessage
                        else -> {
                            bottomDialogMealNameBinding.mealNameTextInputLayout.error = null
                            val mealName =
                                bottomDialogMealNameBinding.mealNameTextInput.text.toString().trim()
                            val meal = Meal(
                                newMealId,
                                mealName,
                                calories,
                                carbohydrateExchangers,
                                proteinFatExchangers,
                                null
                            )
                            mealViewModel.insert(meal)

                            val listOfProducts = foodPlateAdapter.getData()
                            val listOfStaticProducts = staticFoodPlateAdapter.getData()

                            for (product in listOfProducts) {
                                mealViewModel.insertPMJoin(ProductMealJoin(product.id, meal.id))
                            }

                            for (product in listOfStaticProducts) {
                                mealViewModel.insertHPMJoin(
                                    HybridProductMealJoin(
                                        product.id,
                                        meal.id
                                    )
                                )
                            }

                            for (i in 0 until foodPlateAdapter.itemCount) {
                                productViewModel.update(
                                    foodPlateAdapter.getProduct(i).apply {
                                        inFoodPlate = false
                                    }
                                )
                            }

                            for (i in 0 until staticFoodPlateAdapter.itemCount) {
                                productViewModel.update(
                                    staticFoodPlateAdapter.getProduct(i).apply {
                                        inFoodPlate = false
                                    }
                                )
                            }

                            bottomSheetDialog.dismiss()
                            intent = Intent(this, MealActivity::class.java)
                            intent.putExtra("MEAL_ID", meal.id)
                            startActivity(intent)
                        }
                    }
                }
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
            staticFoodPlateAdapter = StaticFoodPlateAdapter(this@FoodPlateActivity)
            conAdapter = ConcatAdapter(foodPlateAdapter, staticFoodPlateAdapter)
            adapter = conAdapter
        }
    }

    private fun sumValues() {

        for (i in 0 until foodPlateAdapter.itemCount) {
            carbohydrateExchangers += foodPlateAdapter.getCarbohydrateExchangers(i)
            proteinFatExchangers += foodPlateAdapter.getProteinFat(i)
            calories += foodPlateAdapter.getCalories(i)!!

        }

        for (i in 0 until staticFoodPlateAdapter.itemCount) {
            carbohydrateExchangers += staticFoodPlateAdapter.getCarbohydrateExchangers(i)
            proteinFatExchangers += staticFoodPlateAdapter.getProteinFat(i)
            calories += staticFoodPlateAdapter.getCalories(i)!!

        }

        carbohydrateExchangers = Calculations().roundToOneDecimal(carbohydrateExchangers)
        proteinFatExchangers = Calculations().roundToOneDecimal(carbohydrateExchangers)
        calories = Calculations().roundToOneDecimal(calories)

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

    override fun onStaticProductClick(productId: Int) {
        val intent = Intent(this, StaticProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }


}