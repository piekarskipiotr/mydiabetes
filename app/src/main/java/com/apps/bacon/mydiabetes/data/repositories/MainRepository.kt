package com.apps.bacon.mydiabetes.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apps.bacon.mydiabetes.api.SharedDataAPI
import com.apps.bacon.mydiabetes.data.entities.MyDiabetesInfo
import com.apps.bacon.mydiabetes.data.entities.StaticMeal
import com.apps.bacon.mydiabetes.data.entities.StaticProduct
import com.apps.bacon.mydiabetes.data.entities.StaticProductMealJoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainRepository constructor(
    private val api: SharedDataAPI
) {
    private val products = MutableLiveData<List<StaticProduct>>()

    private val meals = MutableLiveData<List<StaticMeal>>()

    private val pmj = MutableLiveData<List<StaticProductMealJoin>>()

    private var myDiabetesInfo = MutableLiveData<MyDiabetesInfo>()

    fun productsApiCall(): MutableLiveData<List<StaticProduct>> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getProducts()
                val data = arrayListOf<StaticProduct>()
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

    fun mealsApiCall(): MutableLiveData<List<StaticMeal>> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getMeals()
                val data = arrayListOf<StaticMeal>()
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

    fun pmjApiCall(): MutableLiveData<List<StaticProductMealJoin>> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getPMJ()
                val data = arrayListOf<StaticProductMealJoin>()
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