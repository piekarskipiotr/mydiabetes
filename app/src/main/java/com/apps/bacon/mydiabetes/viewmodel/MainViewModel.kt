package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.entities.*
import com.apps.bacon.mydiabetes.data.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject
constructor(
    @Named("main_repository")
    private val repository: MainRepository
) : ViewModel() {
    var myDiabetesInfo: MutableLiveData<MyDiabetesInfo>? = null

    var products: MutableLiveData<List<Product>>? = null

    var meals: MutableLiveData<List<Meal>>? = null

    var pmj: MutableLiveData<List<ProductMealJoin>>? = null

    fun getProducts(): LiveData<List<Product>>? {
        products = repository.productsApiCall()
        return products

    }

    fun getMeals(): LiveData<List<Meal>>? {
        meals = repository.mealsApiCall()
        return meals

    }

    fun getPMJ(): LiveData<List<ProductMealJoin>>? {
        pmj = repository.pmjApiCall()
        return pmj

    }

    fun getVersion(): MutableLiveData<MyDiabetesInfo>? {
        myDiabetesInfo = repository.versionApiCall()
        return myDiabetesInfo
    }
}