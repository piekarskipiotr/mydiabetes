package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class SaveProductRepository @Inject
constructor(
    private val database: AppDatabase) {

    fun checkForProductExist(name: String) = database.productDao().checkForProductExist(name)

    fun getProduct(name: String) = database.productDao().getProduct(name)

    fun getAllTag() = database.tagDao().getAll()

    fun getTagById(id: Int) = database.tagDao().getTagById(id)

    suspend fun insertProduct(product: Product) = database.productDao().insert(product)

    suspend fun deleteTagById(id: Int) = database.tagDao().deleteById(id)

}