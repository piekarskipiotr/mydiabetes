package com.apps.bacon.mydiabetes.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apps.bacon.mydiabetes.api.SharedDataAPI
import com.apps.bacon.mydiabetes.data.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainRepository constructor(
    private val api: SharedDataAPI
) {
    private val products = MutableLiveData<List<Product>>()

    private val meals = MutableLiveData<List<Meal>>()

    private val pmj = MutableLiveData<List<ProductMealJoin>>()

    private var myDiabetesInfo = MutableLiveData<MyDiabetesInfo>()

    fun productsApiCall(): MutableLiveData<List<Product>> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getProducts()
                val data = arrayListOf<Product>()
                if (response.isSuccessful) {
                    for (product in response.body()!!) {
                        data.add(product)
                    }

                    products.postValue(data)
                }
            } catch (e: Throwable) {
                e.message?.let {
                    Log.d("ProductsCatch:", it)
                }
            }
        }

        return products
    }

    fun mealsApiCall(): MutableLiveData<List<Meal>> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getMeals()
                val data = arrayListOf<Meal>()
                if (response.isSuccessful) {
                    for (meal in response.body()!!) {
                        data.add(meal)
                    }

                    meals.postValue(data)
                }
            } catch (e: Throwable) {
                e.message?.let {
                    Log.d("MealsCatch:", it)
                }
            }
        }

        return meals
    }

    fun pmjApiCall(): MutableLiveData<List<ProductMealJoin>> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getPMJ()
                val data = arrayListOf<ProductMealJoin>()
                if (response.isSuccessful) {
                    for (pmj in response.body()!!) {
                        data.add(pmj)
                    }

                    pmj.postValue(data)
                }
            } catch (e: Throwable) {
                e.message?.let {
                    Log.d("pmjCatch:", it)
                }
            }
        }

        return pmj
    }

    fun versionApiCall(): MutableLiveData<MyDiabetesInfo> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getVersion()
                if (response.isSuccessful) {
                    val mdInfo = response.body()!!
                    myDiabetesInfo.postValue(mdInfo)
                }
            } catch (e: Throwable) {
                e.message?.let {
                    Log.d("VersionCatch:", it)
                }
            }
        }

        return myDiabetesInfo
    }
}