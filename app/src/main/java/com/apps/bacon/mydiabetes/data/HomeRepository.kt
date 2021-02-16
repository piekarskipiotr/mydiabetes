package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.MutableLiveData
import com.apps.bacon.mydiabetes.api.ProductsAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeRepository constructor(
    private val api: ProductsAPI
) {
    private val products = MutableLiveData<List<Product>>()

    fun productsApiCall() : MutableLiveData<List<Product>>{
        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getProducts()
            val data = arrayListOf<Product>()
            if(response.isSuccessful){
                for(product in response.body()!!){
                    data.add(product)
                }

                products.postValue(data)
            }
        }
        return products
    }
}