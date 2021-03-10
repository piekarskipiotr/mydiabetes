package com.apps.bacon.mydiabetes.data.repositories

import androidx.paging.DataSource
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.StaticProduct
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun checkForProductExist(name: String) = database.productDao().checkForProductExist(name)

    fun getAll() = database.productDao().getAll()

    fun getAllPaging() = database.productDao().getAllPaging()

    fun getAllByTag(tagId: Int) = database.productDao().getAllByTag(tagId)

    fun getAllByTagPaging(tagId: Int) = database.productDao().getAllByTagPaging(tagId)

    fun getProduct(id: Int) = database.productDao().getProduct(id)

    fun getProductByBarcode(barcode: String) = database.productDao().getProductByBarcode(barcode)

    fun getProductsInPlate() = database.productDao().getProductsInPlate()

    suspend fun insert(product: Product) = database.productDao().insert(product)

    suspend fun update(product: Product) = database.productDao().update(product)

    suspend fun delete(product: Product) = database.productDao().delete(product)

    /*
    * Below is section of statics functions
    * */

    fun checkForStaticProductExist(name: String) = database.productDao().checkForStaticProductExist(name)

    fun getAllStatics() = database.productDao().getAllStatics()

    fun getAllStaticsPaging() = database.productDao().getAllStaticsPaging()

    fun getAllStaticsByTag(tagId: Int) = database.productDao().getAllStaticsByTag(tagId)

    fun getAllStaticsByTagPaging(tagId: Int) = database.productDao().getAllStaticsByTagPaging(tagId)

    fun getStaticProduct(id: Int) = database.productDao().getStaticProduct(id)

    fun getStaticProductByBarcode(barcode: String) = database.productDao().getStaticProductByBarcode(barcode)

    fun getStaticProductsInPlate() = database.productDao().getStaticProductsInPlate()

    suspend fun insert(staticProduct: StaticProduct) = database.productDao().insert(staticProduct)

    suspend fun update(staticProduct: StaticProduct) = database.productDao().update(staticProduct)
}