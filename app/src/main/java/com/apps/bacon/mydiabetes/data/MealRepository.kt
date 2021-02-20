package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class MealRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.mealDao().getAll()

    suspend fun insert(meal: Meal) = database.mealDao().insert(meal)

    suspend fun update(meal: Meal) = database.mealDao().update(meal)

    suspend fun delete(meal: Meal) = database.mealDao().delete(meal)
}