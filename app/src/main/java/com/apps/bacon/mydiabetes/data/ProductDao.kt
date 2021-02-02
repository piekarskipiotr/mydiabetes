package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {

    @Query("SELECT EXISTS(SELECT * FROM products WHERE :name = product_name)")
    fun checkForProductExist(name: String): Boolean

    @Query("SELECT * FROM products")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE :tagId == product_tag")
    fun getAll(tagId: Int): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE :id == product_id")
    fun getProduct(id: Int): Product

    @Query("SELECT * FROM products WHERE :name == product_name")
    fun getProduct(name: String): Product

    @Query("SELECT * FROM products WHERE :barcode == barcode")
    fun getProductByBarcode(barcode: String): Product

    @Query("SELECT * FROM products WHERE 1 == in_food_plate")
    fun getProductsInPlate(): LiveData<List<Product>>

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)
}