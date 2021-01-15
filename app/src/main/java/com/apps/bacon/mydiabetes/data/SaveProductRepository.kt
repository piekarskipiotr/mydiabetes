package com.apps.bacon.mydiabetes.data

class SaveProductRepository constructor(
    private val database: AppDatabase) {

    fun getAllTag() = database.tagDao().getAll()

    suspend fun insertTag(tag: Tag) = database.tagDao().insert(tag)

    suspend fun insertProduct(product: Product) = database.productDao().insert(product)

}