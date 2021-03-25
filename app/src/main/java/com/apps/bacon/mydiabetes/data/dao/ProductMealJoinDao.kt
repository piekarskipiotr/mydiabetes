package com.apps.bacon.mydiabetes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.ProductMealJoin

@Dao
interface ProductMealJoinDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM products INNER JOIN product_meal_join ON products.product_name == product_meal_join.product_name WHERE product_meal_join.meal_name = :name ORDER BY products.product_name ASC")
    fun getProductsForMeal(name: String): LiveData<List<Product>>

    @Query("SELECT * FROM products INNER JOIN product_meal_join ON products.product_name == product_meal_join.product_name WHERE product_meal_join.product_name = :name")
    fun isProductInMeal(name: String): Boolean

    @Query("SELECT * FROM product_meal_join WHERE product_meal_join.meal_name == :name")
    fun getPMJoinByMealName(name: String): List<ProductMealJoin>

    @Query("SELECT * FROM product_meal_join WHERE product_meal_join.product_name == :name")
    fun getPMJoinByProductName(name: String): List<ProductMealJoin>

    @Insert
    suspend fun insert(productMealJoin: ProductMealJoin)

    @Update
    suspend fun update(productMealJoin: ProductMealJoin)

    @Delete
    suspend fun delete(productMealJoin: ProductMealJoin)

    @Query("DELETE FROM product_meal_join WHERE meal_name == :name")
    suspend fun deleteByMealName(name: String)

    /*
        Rename selection below
     */

    @Update
    suspend fun update(product: Product)

    @Update
    suspend fun update(meal: Meal)

    @Transaction
    suspend fun renamePMJProductName(product: Product, oldName: String, newName: String) {
        product.name = newName
        val list = getPMJoinByProductName(oldName)
        for (pmj in list) {
            delete(pmj)
        }
        update(product)
        for (pmj in list) {
            pmj.productName = newName
            insert(pmj)
        }
    }

    @Transaction
    suspend fun renamePMJMealName(meal: Meal, oldName: String, newName: String) {
        meal.name = newName
        val list = getPMJoinByMealName(oldName)
        for (pmj in list) {
            delete(pmj)
        }
        update(meal)
        for (pmj in list) {
            pmj.mealName = newName
            insert(pmj)
        }
    }
}