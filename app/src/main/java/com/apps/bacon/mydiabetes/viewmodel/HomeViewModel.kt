package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.HomeRepository
import com.apps.bacon.mydiabetes.data.Product
import javax.inject.Named

class HomeViewModel @ViewModelInject
constructor(
    @Named("home_repository")
    private val repository: HomeRepository
) : ViewModel(){
    var currentTag = MutableLiveData<Int>()

    var productsLiveData: MutableLiveData<List<Product>>? = null

    fun getProducts(): LiveData<List<Product>>? {
        productsLiveData = repository.productsApiCall()
        return productsLiveData

    }
}