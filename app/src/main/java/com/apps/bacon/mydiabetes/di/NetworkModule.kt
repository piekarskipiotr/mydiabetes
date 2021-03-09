package com.apps.bacon.mydiabetes.di

import android.content.Context
import com.apps.bacon.mydiabetes.api.SharedDataAPI
import com.apps.bacon.mydiabetes.network.NetworkConnectionInterceptor
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Provides
    fun providesFirebaseStorage(): StorageReference = Firebase.storage.reference

    @Provides
    @Named("firebase_database_url")
    fun providesFirebaseDatabaseUrl(): String =
        "https://mydiabetes-6500f-default-rtdb.europe-west1.firebasedatabase.app/"

    @Provides
    fun providesFirebaseDatabase(
        @Named("firebase_database_url") url: String
    ): DatabaseReference = FirebaseDatabase.getInstance(url).reference

    @Provides
    @Named("api_url")
    fun providesBaseUrl(): String =
        "https://raw.githubusercontent.com/piekarskipiotr/mydiabetes/master/API/"

    @Provides
    fun providesOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().addInterceptor(NetworkConnectionInterceptor(context))

    @Provides
    @Singleton
    fun providesAPI(
        @Named("api_url") baseURL: String,
        okHttpClient: OkHttpClient.Builder
    ): SharedDataAPI = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient.build())
        .build()
        .create(SharedDataAPI::class.java)
}