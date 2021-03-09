package com.apps.bacon.mydiabetes.api

import com.apps.bacon.mydiabetes.data.entities.MyDiabetesInfo
import com.apps.bacon.mydiabetes.data.entities.StaticProductMealJoin
import com.apps.bacon.mydiabetes.data.entities.StaticMeal
import com.apps.bacon.mydiabetes.data.entities.StaticProduct
import retrofit2.Response
import retrofit2.http.GET

interface SharedDataAPI {
    @GET("products")
    suspend fun getProducts(): Response<List<StaticProduct>>

    @GET("meals")
    suspend fun getMeals(): Response<List<StaticMeal>>

    @GET("pmj")
    suspend fun getPMJ(): Response<List<StaticProductMealJoin>>

    @GET("version")
    suspend fun getVersion(): Response<MyDiabetesInfo>
}