package com.apps.bacon.mydiabetes.di

import android.content.Context
import com.apps.bacon.mydiabetes.R
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
object AppModule{

    @Provides
    @Singleton
    fun provideRunningDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)

//    @Provides
//    @Named("emptyFieldErrorMessage")
//    fun provideErrorEmptyFieldMessage(
//        @ApplicationContext context: Context
//    ) = context.resources.getString(R.string.empty_field_message_error)
//
//
//    @Provides
//    fun provideListOfTags(
//        @ApplicationContext context: Context
//    ): List<String> = listOf(
//        context.resources.getString(R.string.meat),
//        context.resources.getString(R.string.fish),
//        context.resources.getString(R.string.protein),
//        context.resources.getString(R.string.bread),
//        context.resources.getString(R.string.vegetables_and_fruits),
//        context.resources.getString(R.string.sweets_and_snacks),
//        context.resources.getString(R.string.drinks),
//        context.resources.getString(R.string.nuts),
//        context.resources.getString(R.string.others)
//    )

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

}