package com.apps.bacon.mydiabetes.di

import android.content.Context
import com.apps.bacon.mydiabetes.api.ProductsAPI
import com.apps.bacon.mydiabetes.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRunningDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    @Named("home_repository")
    fun provideHomeRepository(
        api: ProductsAPI
    ) = HomeRepository(api)

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