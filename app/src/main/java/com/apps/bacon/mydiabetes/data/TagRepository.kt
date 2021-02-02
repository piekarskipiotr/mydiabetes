package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class TagRepository @Inject constructor(
    private val database: AppDatabase
){
    fun getAll() = database.tagDao().getAll()

    fun getProductsByTag(id: Int) = database.productDao().getAll(id)



    fun updateProduct(product: Product) = database.productDao().update(product)

    suspend fun insert(tag: Tag) = database.tagDao().insert(tag)

    suspend fun delete(tag: Tag) = database.tagDao().delete(tag)

}