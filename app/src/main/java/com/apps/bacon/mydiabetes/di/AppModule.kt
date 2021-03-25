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
import javax.inject.Named
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
    @Named("main_repository")
    fun provideMainRepository(
        api: SharedDataAPI
    ) = MainRepository(api)

    @Provides
    @Singleton
    @Named("product_repository")
    fun provideProductRepository(
        database: AppDatabase
    ) = ProductRepository(database)

    @Provides
    @Singleton
    @Named("tag_repository")
    fun provideTagRepository(
        database: AppDatabase
    ) = TagRepository(database)

    @Provides
    @Singleton
    @Named("image_repository")
    fun provideImageRepository(
        database: AppDatabase
    ) = ImageRepository(database)

    @Provides
    @Singleton
    @Named("meal_repository")
    fun provideMealRepository(
        database: AppDatabase
    ) = MealRepository(database)
}