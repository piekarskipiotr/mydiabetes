package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.Meal
import com.apps.bacon.mydiabetes.data.MealRepository
import com.apps.bacon.mydiabetes.data.MealServer
import com.apps.bacon.mydiabetes.data.ProductMealJoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Named

class MealViewModel @ViewModelInject
constructor(
    @Named("meal_repository")
    private val repository: MealRepository
) : ViewModel() {

    fun getAll() = repository.getAll()

    fun getMeal(id: Int) = repository.getMeal(id)

    fun getProductsForMeal(mealId: Int) = repository.getProductsForMeal(mealId)

    fun getPMJbyMealId(mealId: Int) = repository.getPMJbyMealId(mealId)

    fun getLastId() = repository.getLastId()

    fun isProductInMeal(productId: Int) = repository.isProductInMeal(productId)

    fun insert(meal: Meal) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(meal)
    }

    fun update(meal: Meal) = CoroutineScope(Dispatchers.IO).launch {
        repository.update(meal)
    }

    fun delete(meal: Meal) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(meal)
    }

    fun insert(meal: MealServer) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(meal)
    }

    fun update(meal: MealServer) = CoroutineScope(Dispatchers.IO).launch {
        repository.update(meal)
    }

    fun delete(meal: MealServer) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(meal)
    }

    fun insertPMJoin(productMealJoin: ProductMealJoin) = CoroutineScope(Dispatchers.IO).launch {
        repository.insertPMJoin(productMealJoin)
    }

    fun deletePMJoin(mealId: Int) = CoroutineScope(Dispatchers.IO).launch {
        repository.deletePMJoin(mealId)
    }
}