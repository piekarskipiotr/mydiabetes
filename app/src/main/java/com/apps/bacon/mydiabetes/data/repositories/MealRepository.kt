package com.apps.bacon.mydiabetes.data.repositories

import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.data.entities.ProductMealJoin
import javax.inject.Inject

class MealRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun checkForMealExist(name: String) = database.mealDao().checkForMealExist(name)

    fun getAll() = database.mealDao().getAll()

    fun getAllLocal() = database.mealDao().getAllLocal()

    fun getAllPaging() = database.mealDao().getAllPaging()

    fun getMeal(id: Int) = database.mealDao().getMeal(id)

    fun getLastId() = database.mealDao().getLastId()

    suspend fun insert(meal: Meal) = database.mealDao().insert(meal)

    suspend fun update(meal: Meal) = database.mealDao().update(meal)

    suspend fun delete(meal: Meal) = database.mealDao().delete(meal)

    //pmj section

    fun getProductsForMeal(name: String) = database.productMealJoinDao().getProductsForMeal(name)

    fun isProductInMeal(name: String) = database.productMealJoinDao().isProductInMeal(name)

    fun getPMJbyMealName(name: String) = database.productMealJoinDao().getPMJoinByMealName(name)

    suspend fun renamePMJMealName(meal: Meal, oldName: String, newName: String) =
        database.productMealJoinDao().renamePMJMealName(meal, oldName, newName)

    suspend fun insertPMJoin(productMealJoin: ProductMealJoin) =
        database.productMealJoinDao().insert(productMealJoin)

    suspend fun deletePMJoin(name: String) = database.productMealJoinDao().deleteByMealName(name)
}

