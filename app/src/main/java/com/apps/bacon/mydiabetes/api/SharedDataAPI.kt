package com.apps.bacon.mydiabetes.api

import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.data.entities.MyDiabetesInfo
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.ProductMealJoin
import retrofit2.Response
import retrofit2.http.GET

interface SharedDataAPI {
    @GET("products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("meals")
    suspend fun getMeals(): Response<List<Meal>>

    @GET("pmj")
    suspend fun getPMJ(): Response<List<ProductMealJoin>>

    @GET("version")
    suspend fun getVersion(): Response<MyDiabetesInfo>
}