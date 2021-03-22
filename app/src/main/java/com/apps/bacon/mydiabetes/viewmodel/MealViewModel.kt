package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
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
    private val myPagingConfig = Config(
        pageSize = 6,
        prefetchDistance = 150,
        enablePlaceholders = true
    )

    fun checkForMealExist(name: String) = repository.checkForMealExist(name)

    fun getAll() = repository.getAll()

    fun getAllLocal() = repository.getAllLocal()

    fun getPagingListOfMeals(): LiveData<PagedList<Meal>> {
        return LivePagedListBuilder(repository.getAllPaging(), myPagingConfig).build()
    }

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