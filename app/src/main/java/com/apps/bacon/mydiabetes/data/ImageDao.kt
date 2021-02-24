package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageDao {

    @Query("SELECT * FROM images")
    fun getAll(): LiveData<List<Image>>

    @Query("SELECT * FROM images WHERE product_id == :id")
    fun getImageByProductId(id: Int): LiveData<List<Image>>

    @Insert
    suspend fun insert(image: Image)

    @Update
    suspend fun update(image: Image)

    @Delete
    suspend fun delete(image: Image)
}