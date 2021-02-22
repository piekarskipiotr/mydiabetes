package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class MealRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.mealDao().getAll()

    fun getMeal(id: Int) = database.mealDao().getMeal(id)

    suspend fun insert(meal: Meal) = database.mealDao().insert(meal)

    suspend fun update(meal: Meal) = database.mealDao().update(meal)

    suspend fun delete(meal: Meal) = database.mealDao().delete(meal)
}