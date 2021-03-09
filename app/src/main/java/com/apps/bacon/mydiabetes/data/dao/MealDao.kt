package com.apps.bacon.mydiabetes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.data.entities.StaticMeal

@Dao
interface MealDao {
    @Query("SELECT EXISTS(SELECT * FROM meals WHERE :name == meal_name)")
    fun checkForMealExist(name: String): Boolean

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

    /*
    * Below is section of statics queries
    * */

    @Query("SELECT EXISTS(SELECT * FROM static_meals WHERE :name == static_meals.meal_name)")
    fun checkForStaticMealExist(name: String): Boolean

    @Query("SELECT * FROM static_meals")
    fun getAllStatics(): LiveData<List<StaticMeal>>

    @Query("SELECT * FROM static_meals WHERE :id == meal_id")
    fun getStaticMeal(id: Int): StaticMeal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(staticMeal: StaticMeal)
}