package com.apps.bacon.mydiabetes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.mydiabetes.data.entities.Image

@Dao
interface ImageDao {

    @Query("SELECT * FROM images")
    fun getAll(): LiveData<List<Image>>

    @Query("SELECT * FROM images WHERE product_id == :id")
    fun getImageByProductId(id: Int): LiveData<List<Image>>

    @Query("SELECT * FROM images WHERE meal_id == :id")
    fun getImageByMealId(id: Int): LiveData<List<Image>>

    @Insert
    suspend fun insert(image: Image)

    @Update
    suspend fun update(image: Image)

    @Delete
    suspend fun delete(image: Image)
}