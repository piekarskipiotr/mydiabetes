package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.ProductRepository

@Suppress("UNCHECKED_CAST")
class ProductModelFactory constructor(
    private val repository: ProductRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(repository) as T
    }
}