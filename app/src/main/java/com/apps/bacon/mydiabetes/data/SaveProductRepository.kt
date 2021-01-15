package com.apps.bacon.mydiabetes.data

class SaveProductRepository constructor(
    private val database: AppDatabase) {

    fun getAllTag() = database.tagDao().getAll()

    fun getTagById(id: Int) = database.tagDao().getTagById(id)

    suspend fun insertTag(tag: Tag) = database.tagDao().insert(tag)

    suspend fun insertProduct(product: Product) = database.productDao().insert(product)

    suspend fun deleteTagById(id: Int) = database.tagDao().deleteById(id)

}