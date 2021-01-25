package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.FoodPlate
import com.apps.bacon.mydiabetes.data.FoodPlateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodPlateViewModel constructor(
    private val repository: FoodPlateRepository
) : ViewModel(){

    fun getAll() = repository.getAll()

    fun insert(product: FoodPlate) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(product)
    }

    fun delete(product: FoodPlate) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(product)
    }
}