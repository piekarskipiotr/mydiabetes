package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.imageDao().getAll()

    fun getImageByProductId(id: Int) = database.imageDao().getImageByProductId(id)

    fun getImageByMealId(id: Int) = database.imageDao().getImageByMealId(id)

    suspend fun insert(image: Image) = database.imageDao().insert(image)

    suspend fun update(image: Image) = database.imageDao().update(image)

    suspend fun delete(image: Image) = database.imageDao().delete(image)

}