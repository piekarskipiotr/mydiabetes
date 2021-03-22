package com.apps.bacon.mydiabetes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.mydiabetes.data.entities.*

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