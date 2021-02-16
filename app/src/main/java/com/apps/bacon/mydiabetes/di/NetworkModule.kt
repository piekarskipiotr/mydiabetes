package com.apps.bacon.mydiabetes.di

import com.apps.bacon.mydiabetes.api.ProductsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun providesBaseUrl() : String{
        return "https://raw.githubusercontent.com/piekarskipiotr/MyDiabetes2/master/API/"
    }

    @Provides
    @Singleton
    fun providesAPI(baseURL: String): ProductsAPI{
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsAPI::class.java)
    }
}