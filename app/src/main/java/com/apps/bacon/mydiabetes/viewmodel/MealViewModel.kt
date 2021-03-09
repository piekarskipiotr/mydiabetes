package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.entities.*
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Named

class MealViewModel @ViewModelInject
constructor(
    @Named("meal_repository")
    private val repository: MealRepository
) : ViewModel() {
    fun checkForMealExist(name: String) = repository.checkForMealExist(name)

    fun getAll() = repository.getAll()

    fun getMeal(id: Int) = repository.getMeal(id)

    fun getLastId() = repository.getLastId()

    fun insert(meal: Meal) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(meal)
    }

    fun update(meal: Meal) = CoroutineScope(Dispatchers.IO).launch {
        repository.update(meal)
    }

    fun delete(meal: Meal) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(meal)
    }

    //pmj section

    fun getProductsForMeal(mealId: Int) = repository.getProductsForMeal(mealId)

    fun isProductInMeal(productId: Int) = repository.isProductInMeal(productId)

    fun getPMJbyMealId(mealId: Int) = repository.getPMJbyMealId(mealId)

    fun insertPMJoin(productMealJoin: ProductMealJoin) =
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertPMJoin(productMealJoin)
        }

    fun deletePMJoin(mealId: Int) = CoroutineScope(Dispatchers.IO).launch {
        repository.deletePMJoin(mealId)
    }

    /*
   * Below is section of statics functions
   * */

    fun checkForStaticMealExist(name: String) = repository.checkForStaticMealExist(name)

    fun getAllStatics() = repository.getAllStatics()

    fun getStaticMeal(id: Int) = repository.getStaticMeal(id)

    fun insert(staticMeal: StaticMeal) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(staticMeal)
    }

    //pmj section

    fun getStaticProductsForStaticMeal(mealId: Int) = repository.getStaticProductsForStaticMeal(mealId)

    fun getStaticProductsForMeal(mealId: Int) = repository.getStaticProductsForMeal(mealId)

    fun insertPMJoin(staticProductMealJoin: StaticProductMealJoin) =
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertPMJoin(staticProductMealJoin)
        }

    fun insertHPMJoin(hybridProductMealJoin: HybridProductMealJoin) =
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertHPMJoin(hybridProductMealJoin)
        }
}