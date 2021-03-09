package com.apps.bacon.mydiabetes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apps.bacon.mydiabetes.data.entities.*

@Dao
interface ProductMealJoinDao {
    @Query("SELECT * FROM products INNER JOIN product_meal_join ON products.product_id = product_meal_join.productId WHERE product_meal_join.mealId = :mealId")
    fun getProductsForMeal(mealId: Int): LiveData<List<Product>>

    @Query("SELECT * FROM products INNER JOIN product_meal_join ON products.product_id = product_meal_join.productId WHERE product_meal_join.productId = :productId")
    fun isProductInMeal(productId: Int): Boolean

    @Query("SELECT * FROM product_meal_join WHERE product_meal_join.mealId = :mealId")
    fun getPMJoinByMealId(mealId: Int): List<ProductMealJoin>

    @Insert
    suspend fun insert(productMealJoin: ProductMealJoin)

    @Query("DELETE FROM product_meal_join WHERE mealId == :mealId")
    suspend fun deleteByMealId(mealId: Int)

    /*
    * Below is section of statics queries
    * */

    @Query("SELECT * FROM static_products INNER JOIN static_product_meal_join ON static_products.product_id = static_product_meal_join.productId WHERE static_product_meal_join.mealId = :mealId")
    fun getStaticProductsForStaticMeal(mealId: Int): LiveData<List<StaticProduct>>

    @Query("SELECT * FROM static_products INNER JOIN hybrid_product_meal_join ON static_products.product_id = hybrid_product_meal_join.productId WHERE hybrid_product_meal_join.mealId = :mealId")
    fun getStaticProductsForMeal(mealId: Int): LiveData<List<StaticProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(staticProductMealJoin: StaticProductMealJoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hybridProductMealJoin: HybridProductMealJoin)
}