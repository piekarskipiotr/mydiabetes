package com.apps.bacon.mydiabetes.api

import com.apps.bacon.mydiabetes.data.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductsAPI {
    @GET("products")
    suspend fun getProducts(): Response<List<Product>>
}