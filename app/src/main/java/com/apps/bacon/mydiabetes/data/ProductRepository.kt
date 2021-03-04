package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun checkForProductExist(name: String) = database.productDao().checkForProductExist(name)

    fun getAll() = database.productDao().getAll()

    fun getAllByTag(tagId: Int) = database.productDao().getAllByTag(tagId)

    fun getProduct(id: Int) = database.productDao().getProduct(id)

    fun getProductByName(name: String) = database.productDao().getProductByName(name)

    fun getProductByBarcode(barcode: String) = database.productDao().getProductByBarcode(barcode)

    fun getProductsInPlate() = database.productDao().getProductsInPlate()

    suspend fun insert(product: Product) = database.productDao().insert(product)

    suspend fun update(product: Product) = database.productDao().update(product)

    suspend fun delete(product: Product) = database.productDao().delete(product)

    suspend fun insert(product: ProductServer) = database.productDao().insert(product)

    suspend fun update(product: ProductServer) = database.productDao().update(product)

    suspend fun delete(product: ProductServer) = database.productDao().delete(product)

}