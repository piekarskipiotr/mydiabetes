package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.FoodPlateRepository

@Suppress("UNCHECKED_CAST")
class FoodPlateModelFactory constructor(
    private val repository: FoodPlateRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodPlateViewModel(repository) as T
    }
}