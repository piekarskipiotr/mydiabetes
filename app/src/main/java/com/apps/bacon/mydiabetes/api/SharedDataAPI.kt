package com.apps.bacon.mydiabetes.api

import com.apps.bacon.mydiabetes.data.MealServer
import com.apps.bacon.mydiabetes.data.MyDiabetesInfo
import com.apps.bacon.mydiabetes.data.ProductServer
import retrofit2.Response
import retrofit2.http.GET

interface SharedDataAPI {
    @GET("products")
    suspend fun getProducts(): Response<List<ProductServer>>

    @GET("meals")
    suspend fun getMeals(): Response<List<MealServer>>

    @GET("version")
    suspend fun getVersion(): Response<MyDiabetesInfo>
}