package com.apps.bacon.mydiabetes.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apps.bacon.mydiabetes.api.SharedDataAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainRepository constructor(
    private val api: SharedDataAPI
) {
    private val products = MutableLiveData<List<ProductServer>>()
    private val meals = MutableLiveData<List<MealServer>>()

    fun productsApiCall(): MutableLiveData<List<ProductServer>> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getProducts()
                val data = arrayListOf<ProductServer>()
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

    fun mealsApiCall(): MutableLiveData<List<MealServer>> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getMeals()
                val data = arrayListOf<MealServer>()
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

    private var myDiabetesInfo = MutableLiveData<MyDiabetesInfo>()

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