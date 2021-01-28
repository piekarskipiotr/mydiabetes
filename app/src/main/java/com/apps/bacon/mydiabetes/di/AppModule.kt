package com.apps.bacon.mydiabetes.di

import android.content.Context
import com.apps.bacon.mydiabetes.HomeActivity
import com.apps.bacon.mydiabetes.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named
import javax.inject.Scope
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideRunningDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    @Named("home_repository")
    fun provideHomeRepository(
        database: AppDatabase
    ) = HomeRepository(database)

    @Provides
    @Singleton
    @Named("product_repository")
    fun provideProductRepository(
        database: AppDatabase
    ) = ProductRepository(database)

    @Provides
    @Singleton
    @Named("save_product_repository")
    fun provideSaveProductRepository(
        database: AppDatabase
    ) = SaveProductRepository(database)

    @Provides
    @Singleton
    @Named("search_repository")
    fun provideSearchRepository(
        database: AppDatabase
    ) = SearchRepository(database)

    @Provides
    @Singleton
    @Named("tag_repository")
    fun provideTagRepository(
        database: AppDatabase
    ) = TagRepository(database)


}