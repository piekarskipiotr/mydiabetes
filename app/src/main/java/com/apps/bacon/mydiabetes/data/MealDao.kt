package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealDao {
    @Query("SELECT * FROM meals")
    fun getAll(): LiveData<List<Meal>>

    @Query("SELECT * FROM meals WHERE :id == meal_id")
    fun getMeal(id: Int): Meal

    @Query("SELECT MAX(meal_id) FROM meals")
    fun getLastId(): Int

    @Insert
    suspend fun insert(meal: Meal)

    @Update
    suspend fun update(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: MealServer)

    @Update
    suspend fun update(meal: MealServer)

    @Delete
    suspend fun delete(meal: MealServer)

}