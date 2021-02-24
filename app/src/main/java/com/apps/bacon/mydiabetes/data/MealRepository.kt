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

    suspend fun insertPMJoin(productMealJoin: ProductMealJoin) = database.productMealJoin().insert(productMealJoin)

    fun getProductsForMeal(mealId: Int) = database.productMealJoin().getProductsForMeal(mealId)

    fun getPMJoinByMealId(id: Int) = database.productMealJoin().getPMJoinByMealId(id)

    fun isProductInMeal(productId: Int) = database.productMealJoin().isProductInMeal(productId)

    suspend fun deletePMJoin(productMealJoin: ProductMealJoin) = database.productMealJoin().delete(productMealJoin)
}