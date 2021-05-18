package com.apps.bacon.mydiabetes.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.data.entities.MyDiabetesInfo
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.ProductMealJoin
import com.apps.bacon.mydiabetes.data.repositories.MainRepository
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject
constructor(
    private val mainRepository: MainRepository,
    private val productRepository: ProductRepository,
    private val mealRepository: MealRepository
) : ViewModel() {
    var myDiabetesInfo: MutableLiveData<MyDiabetesInfo>? = null

    var products: MutableLiveData<List<Product>>? = null

    var meals: MutableLiveData<List<Meal>>? = null

    var pmj: MutableLiveData<List<ProductMealJoin>>? = null

    fun getProducts(): LiveData<List<Product>>? {
        products = mainRepository.productsApiCall()
        return products

    }

    fun getMeals(): LiveData<List<Meal>>? {
        meals = mainRepository.mealsApiCall()
        return meals

    }

    fun getPMJ(): LiveData<List<ProductMealJoin>>? {
        pmj = mainRepository.pmjApiCall()
        return pmj

    }

    fun getVersion(): MutableLiveData<MyDiabetesInfo>? {
        myDiabetesInfo = mainRepository.versionApiCall()
        return myDiabetesInfo
    }

    fun insert(product: Product) = viewModelScope.launch(Dispatchers.Default) {
        productRepository.insert(product)
    }

    fun insert(meal: Meal) = viewModelScope.launch(Dispatchers.Default) {
        mealRepository.insert(meal)
    }

    fun insert(productMealJoin: ProductMealJoin) = viewModelScope.launch(Dispatchers.Default) {
        mealRepository.insertPMJoin(productMealJoin)
    }
}