package com.apps.bacon.mydiabetes.data

class ProductRepository constructor(
    private val database: AppDatabase
) {
    fun getProduct(id: Int) = database.productDao().getProduct(id)

    suspend fun deleteProduct(product: Product) = database.productDao().delete(product)

    suspend fun updateProduct(product: Product) = database.productDao().delete(product)


}