package com.apps.bacon.mydiabetes.ui.change.meal.name

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeMealNameViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {
    fun checkForMealExist(name: String, currentName: String?): Boolean {
        return if (currentName.equals(name, ignoreCase = true))
            false
        else
            mealRepository.checkForMealExist(name)
    }
}