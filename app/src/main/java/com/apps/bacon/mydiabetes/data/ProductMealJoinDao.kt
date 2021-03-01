package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductMealJoinDao {
    @Query("SELECT * FROM products INNER JOIN product_meal_join ON products.product_id = product_meal_join.productId WHERE product_meal_join.mealId = :mealId")
    fun getProductsForMeal(mealId: Int): LiveData<List<Product>>

    @Query("SELECT * FROM products INNER JOIN product_meal_join ON products.product_id = product_meal_join.productId WHERE product_meal_join.productId = :productId")
    fun isProductInMeal(productId: Int): Boolean

    @Insert
    suspend fun insert(productMealJoin: ProductMealJoin)

    @Update
    suspend fun update(productMealJoin: ProductMealJoin)

    @Query("DELETE FROM product_meal_join WHERE mealId == :mealId")
    suspend fun deleteByMealId(mealId: Int)

    @Query("SELECT * FROM product_meal_join WHERE product_meal_join.mealId = :mealId")
    fun getPMJoinByMealId(mealId: Int): List<ProductMealJoin>
}