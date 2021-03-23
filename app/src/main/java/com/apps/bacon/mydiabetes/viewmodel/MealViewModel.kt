package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.apps.bacon.mydiabetes.data.entities.*
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MealViewModel @Inject
constructor(
    @Named("meal_repository")
    private val repository: MealRepository
) : ViewModel() {
    private val myPagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        prefetchDistance = 5
    )

    fun checkForMealExist(name: String) = repository.checkForMealExist(name)

    fun getAll() = repository.getAll()

    fun getAllLocal() = repository.getAllLocal()

    fun getPagingListOfMeals() = Pager(
        config = myPagingConfig, pagingSourceFactory = { repository.getAllPaging() }
    ).flow.cachedIn(viewModelScope)

    fun getMeal(id: Int) = repository.getMeal(id)

    fun getLastId() = repository.getLastId()

    fun insert(meal: Meal) = CoroutineScope(Dispatchers.Default).launch {
        repository.insert(meal)
    }

    fun update(meal: Meal) = CoroutineScope(Dispatchers.Default).launch {
        repository.update(meal)
    }

    fun delete(meal: Meal) = CoroutineScope(Dispatchers.Default).launch {
        repository.delete(meal)
    }

    //pmj section

    fun getProductsForMeal(name: String) = repository.getProductsForMeal(name)

    fun isProductInMeal(name: String) = repository.isProductInMeal(name)

    fun getPMJbyMealName(name: String) = repository.getPMJbyMealName(name)


    fun renamePMJMealName(meal: Meal, oldName: String, newName: String) =
        CoroutineScope(Dispatchers.Default).launch {
            repository.renamePMJMealName(meal, oldName, newName)
        }
    fun insertPMJoin(productMealJoin: ProductMealJoin) =
        CoroutineScope(Dispatchers.Default).launch {
            repository.insertPMJoin(productMealJoin)
        }

    fun deletePMJoin(name: String) = CoroutineScope(Dispatchers.Default).launch {
        repository.deletePMJoin(name)
    }
}