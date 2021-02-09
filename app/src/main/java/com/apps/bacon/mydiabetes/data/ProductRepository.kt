package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getProduct(id: Int) = database.productDao().getProduct(id)

    fun getProductsInPlate() = database.productDao().getProductsInPlate()

    fun getLastId() = database.tagDao().getLastId()

    fun getProductByBarcode(barcode: String) = database.productDao().getProductByBarcode(barcode)

    fun getTag(id: Int) = database.tagDao().getTagById(id)

    fun getImagesByProductId(productId: Int) = database.imageDao().getImageByProductId(productId)

    suspend fun deleteProduct(product: Product) = database.productDao().delete(product)

    suspend fun updateProduct(product: Product) = database.productDao().update(product)

    suspend fun insertImage(image: Image) = database.imageDao().insert(image)

    suspend fun deleteImage(image: Image) = database.imageDao().delete(image)

}