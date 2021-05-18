package com.apps.bacon.mydiabetes.ui.settings.share

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject
constructor(
    private val productRepository: ProductRepository,
    private val mealRepository: MealRepository
) : ViewModel() {
    fun getAllLocalProducts() = productRepository.getAllLocal()

    fun getAllLocalMeals() = mealRepository.getAllLocal()

    fun getProductsForMeal(mealName: String) = mealRepository.getProductsForMeal(mealName)

    fun getPMJbyMealName(mealName: String) = mealRepository.getPMJbyMealName(mealName)


}