package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.Meal
import com.apps.bacon.mydiabetes.data.MealRepository
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

    fun insert(meal: Meal) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(meal)
    }

    fun update(meal: Meal) = CoroutineScope(Dispatchers.Main).launch {
        repository.update(meal)
    }

    fun delete(meal: Meal) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(meal)
    }
}