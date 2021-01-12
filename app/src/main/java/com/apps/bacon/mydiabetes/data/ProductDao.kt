package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE :tag == product_tag")
    fun getAll(tag: Int): LiveData<List<Product>>

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)
}