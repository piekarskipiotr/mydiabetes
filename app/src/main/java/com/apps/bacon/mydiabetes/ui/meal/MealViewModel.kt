package com.apps.bacon.mydiabetes.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.mydiabetes.data.entities.Image
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.data.repositories.ImageRepository
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {
    fun getMeal(mealId: Int) = mealRepository.getMeal(mealId)

    fun getPMJbyMealName(mealName: String) = mealRepository.getPMJbyMealName(mealName)

    fun deletePMJoin(mealName: String) = viewModelScope.launch(Dispatchers.Default) {
        mealRepository.deletePMJoin(mealName)
    }

    fun getImageByMealId(mealId: Int) = imageRepository.getImageByMealId(mealId)

    fun deleteImage(image: Image) = viewModelScope.launch(Dispatchers.Default) {
        imageRepository.delete(image)
    }

    fun deleteMeal(meal: Meal) = viewModelScope.launch(Dispatchers.Default) {
        mealRepository.delete(meal)
    }

    fun getProductsForMeal(mealName: String) = mealRepository.getProductsForMeal(mealName)

    fun updateMeal(meal: Meal) = viewModelScope.launch(Dispatchers.Default) {
        mealRepository.update(meal)
    }

    fun renamePMJMealName(meal: Meal, oldName: String, newName: String) = viewModelScope.launch(Dispatchers.Default) {
        mealRepository.renamePMJMealName(meal, oldName, newName)
    }

    fun insertImage(image: Image) = viewModelScope.launch(Dispatchers.Default) {
        imageRepository.insert(image)
    }
}