package com.apps.bacon.mydiabetes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.StaticProduct

@Dao
interface ProductDao {

    @Query("SELECT EXISTS(SELECT * FROM products WHERE :name == products.product_name)")
    fun checkForProductExist(name: String): Boolean

    @Query("SELECT * FROM products")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE :tagId == product_tag")
    fun getAllByTag(tagId: Int): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE :id == product_id")
    fun getProduct(id: Int): Product

//    @Query("SELECT * FROM products WHERE :name == product_name")
//    fun getProductByName(name: String): Product

    @Query("SELECT * FROM products WHERE :barcode == barcode")
    fun getProductByBarcode(barcode: String): Product?

    @Query("SELECT * FROM products WHERE in_food_plate == 1")
    fun getProductsInPlate(): LiveData<List<Product>>

    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    /*
    * Below is section of statics queries
    * */

    @Query("SELECT EXISTS(SELECT * FROM static_products WHERE :name == static_products.product_name)")
    fun checkForStaticProductExist(name: String): Boolean

    @Query("SELECT * FROM static_products")
    fun getAllStatics(): LiveData<List<StaticProduct>>

    @Query("SELECT * FROM static_products WHERE :tagId == product_tag")
    fun getAllStaticsByTag(tagId: Int): LiveData<List<StaticProduct>>

    @Query("SELECT * FROM static_products WHERE :id == product_id")
    fun getStaticProduct(id: Int): StaticProduct

//    @Query("SELECT * FROM static_product WHERE :name == product_name")
//    fun getStaticProductByName(name: String): StaticProduct

    @Query("SELECT * FROM static_products WHERE :barcode == barcode")
    fun getStaticProductByBarcode(barcode: String): StaticProduct?

    @Query("SELECT * FROM static_products WHERE in_food_plate == 1")
    fun getStaticProductsInPlate(): LiveData<List<StaticProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(staticProduct: StaticProduct)

    @Update
    suspend fun update(staticProduct: StaticProduct)
}