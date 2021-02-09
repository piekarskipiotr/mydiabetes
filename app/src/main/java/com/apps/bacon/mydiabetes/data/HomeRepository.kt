package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.productDao().getAll()

    fun isSomethingInFoodPlate() = database.productDao().getProductsInPlate()

    fun getProductsByTag(id: Int) = database.productDao().getAll(id)

    fun getAllTags() = database.tagDao().getAll()
}