package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageDao {

    @Query("SELECT * FROM images")
    fun getAll(): LiveData<List<Image>>

    @Query("SELECT * FROM images WHERE product_id == :id")
    fun getProductImages(id: Int): LiveData<List<Image>>

    @Insert
    fun insert(image: Image)

    @Update
    fun update(image: Image)

    @Delete
    fun delete(image: Image)
}