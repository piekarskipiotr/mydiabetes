package com.apps.bacon.mydiabetes.di

import android.content.Context
import com.apps.bacon.mydiabetes.api.SharedDataAPI
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.repositories.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRunningDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMainRepository(
        api: SharedDataAPI
    ) = MainRepository(api)

    @Provides
    @Singleton
    fun provideProductRepository(
        database: AppDatabase
    ) = ProductRepository(database)

    @Provides
    @Singleton
    fun provideTagRepository(
        database: AppDatabase
    ) = TagRepository(database)

    @Provides
    @Singleton
    fun provideImageRepository(
        database: AppDatabase
    ) = ImageRepository(database)

    @Provides
    @Singleton
    fun provideMealRepository(
        database: AppDatabase
    ) = MealRepository(database)
}