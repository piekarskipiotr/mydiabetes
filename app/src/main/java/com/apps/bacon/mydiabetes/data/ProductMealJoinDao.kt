package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductMealJoinDao {
    @Query("SELECT * FROM products INNER JOIN product_meal_join ON products.product_id = product_meal_join.productId WHERE product_meal_join.mealId = :mealId")
    fun getProductsForMeal(mealId: Int): LiveData<List<Product>>

    @Insert
    suspend fun insert(productMealJoin: ProductMealJoin)

    @Update
    suspend fun update(productMealJoin: ProductMealJoin)

    @Delete
    suspend fun delete(productMealJoin: ProductMealJoin)
}