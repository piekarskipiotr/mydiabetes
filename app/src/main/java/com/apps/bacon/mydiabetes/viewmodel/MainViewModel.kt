package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.entities.MyDiabetesInfo
import com.apps.bacon.mydiabetes.data.entities.StaticMeal
import com.apps.bacon.mydiabetes.data.entities.StaticProduct
import com.apps.bacon.mydiabetes.data.entities.StaticProductMealJoin
import com.apps.bacon.mydiabetes.data.repositories.MainRepository
import javax.inject.Named

class MainViewModel @ViewModelInject
constructor(
    @Named("main_repository")
    private val repository: MainRepository
) : ViewModel() {
    var myDiabetesInfo: MutableLiveData<MyDiabetesInfo>? = null

    var products: MutableLiveData<List<StaticProduct>>? = null

    var meals: MutableLiveData<List<StaticMeal>>? = null

    var pmj: MutableLiveData<List<StaticProductMealJoin>>? = null

    fun getProducts(): LiveData<List<StaticProduct>>? {
        products = repository.productsApiCall()
        return products

    }

    fun getMeals(): LiveData<List<StaticMeal>>? {
        meals = repository.mealsApiCall()
        return meals

    }

    fun getPMJ(): LiveData<List<StaticProductMealJoin>>? {
        pmj = repository.pmjApiCall()
        return pmj

    }

    fun getVersion(): MutableLiveData<MyDiabetesInfo>? {
        myDiabetesInfo = repository.versionApiCall()
        return myDiabetesInfo
    }
}