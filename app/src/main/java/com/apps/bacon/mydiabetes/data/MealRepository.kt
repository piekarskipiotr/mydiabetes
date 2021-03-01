package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class MealRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.mealDao().getAll()

    fun getMeal(id: Int) = database.mealDao().getMeal(id)

    fun getLastId() = database.mealDao().getLastId()

    suspend fun insert(meal: Meal) = database.mealDao().insert(meal)

    suspend fun update(meal: Meal) = database.mealDao().update(meal)

    suspend fun delete(meal: Meal) = database.mealDao().delete(meal)

    suspend fun insertPMJoin(productMealJoin: ProductMealJoin) = database.productMealJoinDao().insert(productMealJoin)

    fun getProductsForMeal(mealId: Int) = database.productMealJoinDao().getProductsForMeal(mealId)

    fun isProductInMeal(productId: Int) = database.productMealJoinDao().isProductInMeal(productId)

    fun getPMJbyMealId(mealId: Int) = database.productMealJoinDao().getPMJoinByMealId(mealId)

    suspend fun deletePMJoin(mealId: Int) = database.productMealJoinDao().deleteByMealId(mealId)

}