package com.apps.bacon.mydiabetes.data.repositories

import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.entities.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun checkForProductExist(name: String) = database.productDao().checkForProductExist(name)

    fun getAll() = database.productDao().getAll()

    fun getAllLocal() = database.productDao().getAllLocal()

    fun getAllPaging() = database.productDao().getAllPaging()

    fun getAllByTag(tagId: Int) = database.productDao().getAllByTag(tagId)

    fun getAllByTagPaging(tagId: Int) = database.productDao().getAllByTagPaging(tagId)

    fun getProduct(id: Int) = database.productDao().getProduct(id)

    fun getProductByBarcode(barcode: String) = database.productDao().getProductByBarcode(barcode)

    fun getProductsInPlate() = database.productDao().getProductsInPlate()

    suspend fun renamePMJProductName(product: Product, oldName: String, newName: String) = database.productMealJoinDao().renamePMJProductName(product, oldName, newName)

    suspend fun insert(product: Product) = database.productDao().insert(product)

    suspend fun update(product: Product) = database.productDao().update(product)

    suspend fun delete(product: Product) = database.productDao().delete(product)
}