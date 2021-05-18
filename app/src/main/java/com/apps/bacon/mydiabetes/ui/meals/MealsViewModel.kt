package com.apps.bacon.mydiabetes.ui.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject
constructor(
    private val repository: MealRepository
) : ViewModel() {
    private val myPagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        prefetchDistance = 5
    )

//    fun checkForMealExist(name: String, currentName: String?): Boolean {
//        return if (currentName.equals(name, ignoreCase = true))
//            false
//        else
//            repository.checkForMealExist(name)
//    }

    fun getPagingListOfMeals() = Pager(
        config = myPagingConfig, pagingSourceFactory = {
            repository.getAllPaging()
        }
    ).flow.cachedIn(viewModelScope)
}