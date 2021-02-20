package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealDao {
    @Query("SELECT * FROM meals")
    fun getAll(): LiveData<List<Meal>>

    @Insert
    fun insert(meal: Meal)

    @Update
    fun update(meal: Meal)

    @Delete
    fun delete(meal: Meal)

}