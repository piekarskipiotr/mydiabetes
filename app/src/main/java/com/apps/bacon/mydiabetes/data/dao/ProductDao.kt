package com.apps.bacon.mydiabetes.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*
import com.apps.bacon.mydiabetes.data.entities.Product

@Dao
interface ProductDao {

    @Query("SELECT EXISTS(SELECT * FROM products WHERE LOWER(products.product_name) == :name)")
    fun checkForProductExist(name: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM products WHERE products.barcode == :barcode)")
    fun checkForBarcodeExist(barcode: String): Boolean

    @Query("SELECT * FROM products ORDER BY product_name ASC")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE is_editable == 1 ORDER BY product_name ASC")
    fun getAllLocal(): LiveData<List<Product>>

    @Query("SELECT * FROM products ORDER BY product_name ASC")
    fun getAllPaging(): PagingSource<Int, Product>

    @Query("SELECT * FROM products WHERE product_tag == :tagId ORDER BY product_name ASC")
    fun getAllByTag(tagId: Int): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE product_tag == :tagId ORDER BY product_name ASC")
    fun getAllByTagPaging(tagId: Int): PagingSource<Int, Product>

    @Query("SELECT * FROM products WHERE product_id == :id")
    fun getProduct(id: Int): Product

    @Query("SELECT * FROM products WHERE barcode == :barcode")
    fun getProductByBarcode(barcode: String): Product?

    @Query("SELECT * FROM products WHERE in_food_plate == 1")
    fun getProductsInPlate(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)
}