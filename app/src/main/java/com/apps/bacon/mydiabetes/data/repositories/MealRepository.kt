package com.apps.bacon.mydiabetes.data.repositories

import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.entities.*
import javax.inject.Inject

class MealRepository @Inject constructor(
    private val database: AppDatabase
) {

    fun checkForMealExist(name: String) = database.mealDao().checkForMealExist(name)

    fun getAll() = database.mealDao().getAll()

    fun getAllPaging() = database.mealDao().getAllPaging()

    fun getMeal(id: Int) = database.mealDao().getMeal(id)

    fun getLastId() = database.mealDao().getLastId()

    suspend fun insert(meal: Meal) = database.mealDao().insert(meal)

    suspend fun update(meal: Meal) = database.mealDao().update(meal)

    suspend fun delete(meal: Meal) = database.mealDao().delete(meal)

    //pmj section

    fun getProductsForMeal(mealId: Int) = database.productMealJoinDao().getProductsForMeal(mealId)

    fun isProductInMeal(productId: Int) = database.productMealJoinDao().isProductInMeal(productId)

    fun getPMJbyMealId(mealId: Int) = database.productMealJoinDao().getPMJoinByMealId(mealId)

    suspend fun insertPMJoin(productMealJoin: ProductMealJoin) =
        database.productMealJoinDao().insert(productMealJoin)

    suspend fun deletePMJoin(mealId: Int) = database.productMealJoinDao().deleteByMealId(mealId)

    /*
   * Below is section of statics functions
   * */

    fun checkForStaticMealExist(name: String) = database.mealDao().checkForStaticMealExist(name)

    fun getAllStatics() = database.mealDao().getAllStatics()

    fun getAllStaticsPaging() = database.mealDao().getAllStaticsPaging()

    fun getStaticMeal(id: Int) = database.mealDao().getStaticMeal(id)

    suspend fun insert(staticMeal: StaticMeal) = database.mealDao().insert(staticMeal)

    //static pmj section

    fun getStaticProductsForStaticMeal(mealId: Int) = database.productMealJoinDao().getStaticProductsForStaticMeal(mealId)

    fun getStaticProductsForMeal(mealId: Int) = database.productMealJoinDao().getStaticProductsForMeal(mealId)

    suspend fun insertPMJoin(staticProductMealJoin: StaticProductMealJoin) =
        database.productMealJoinDao().insert(staticProductMealJoin)

    suspend fun insertHPMJoin(hybridProductMealJoin: HybridProductMealJoin) =
        database.productMealJoinDao().insert(hybridProductMealJoin)
}

