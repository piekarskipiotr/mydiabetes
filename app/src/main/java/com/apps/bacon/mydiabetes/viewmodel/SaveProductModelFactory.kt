package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.SaveProductRepository

@Suppress("UNCHECKED_CAST")
class SaveProductModelFactory constructor(
    private val repository: SaveProductRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SaveProductViewModel(repository) as T
    }

}