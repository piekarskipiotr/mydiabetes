package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.MainRepository
import com.apps.bacon.mydiabetes.data.MealServer
import com.apps.bacon.mydiabetes.data.MyDiabetesInfo
import com.apps.bacon.mydiabetes.data.ProductServer
import javax.inject.Named

class MainViewModel @ViewModelInject
constructor(
    @Named("main_repository")
    private val repository: MainRepository
) : ViewModel() {
    var myDiabetesInfo: MutableLiveData<MyDiabetesInfo>? = null

    var products: MutableLiveData<List<ProductServer>>? = null

    var meals: MutableLiveData<List<MealServer>>? = null

    fun getProducts(): LiveData<List<ProductServer>>? {
        products = repository.productsApiCall()
        return products

    }

    fun getMeals(): LiveData<List<MealServer>>? {
        meals = repository.mealsApiCall()
        return meals

    }

    fun getVersion(): MutableLiveData<MyDiabetesInfo>? {
        myDiabetesInfo = repository.versionApiCall()
        return myDiabetesInfo
    }

}