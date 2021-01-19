package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.HomeRepository

@Suppress("UNCHECKED_CAST")
class HomeModelFactory constructor(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}