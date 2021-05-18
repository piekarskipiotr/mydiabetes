package com.apps.bacon.mydiabetes.ui.foodplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.ProductMealJoin
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodPlateViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val mealRepository: MealRepository
) : ViewModel(){
    fun getLastId() = mealRepository.getLastId()

    fun getProductsInPlate() = productRepository.getProductsInPlate()

    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.Default) {
        productRepository.update(product)
    }

    fun checkForMealExist(name: String, currentName: String?): Boolean {
        return if (currentName.equals(name, ignoreCase = true))
            false
        else
            mealRepository.checkForMealExist(name)
    }

    fun insertMeal(meal: Meal) = viewModelScope.launch(Dispatchers.Default) {
        mealRepository.insert(meal)
    }

    fun insertPMJoin(productMealJoin: ProductMealJoin) = viewModelScope.launch(Dispatchers.Default) {
        mealRepository.insertPMJoin(productMealJoin)
    }
}