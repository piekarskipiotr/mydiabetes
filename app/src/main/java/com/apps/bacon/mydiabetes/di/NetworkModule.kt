package com.apps.bacon.mydiabetes.di

import android.content.Context
import com.apps.bacon.mydiabetes.api.ProductsAPI
import com.apps.bacon.mydiabetes.network.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun providesBaseUrl(): String{
        return "https://raw.githubusercontent.com/piekarskipiotr/MyDiabetes2/master/API/"
    }

    @Provides
    fun providesOkHttpClient(@ApplicationContext context: Context): OkHttpClient.Builder{
        return OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(context))

    }

    @Provides
    @Singleton
    fun providesAPI(baseURL: String, okHttpClient: OkHttpClient.Builder): ProductsAPI{
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
            .create(ProductsAPI::class.java)
    }
}