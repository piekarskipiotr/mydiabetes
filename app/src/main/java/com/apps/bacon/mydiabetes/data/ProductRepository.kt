package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getProduct(id: Int) = database.productDao().getProduct(id)

    fun getProductsInPlate() = database.productDao().getProductsInPlate()

    fun getTag(id: Int) = database.tagDao().getTagById(id)

    suspend fun deleteProduct(product: Product) = database.productDao().delete(product)

    suspend fun updateProduct(product: Product) = database.productDao().update(product)


}