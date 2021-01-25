package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FoodPlateDao {

    @Query("SELECT * FROM food_plate")
    fun getAll(): LiveData<List<FoodPlate>>

    @Insert
    fun insert(product: FoodPlate)

    @Delete
    fun delete(product: FoodPlate)

}