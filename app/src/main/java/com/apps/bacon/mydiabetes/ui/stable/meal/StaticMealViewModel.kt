package com.apps.bacon.mydiabetes.ui.stable.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.repositories.ImageRepository
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StaticMealViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {
    fun getMeal(mealId: Int) = mealRepository.getMeal(mealId)

    var urls: MutableLiveData<List<String>>? = null

    fun getURL(
        storageReference: StorageReference,
        type: String,
        name: String
    ): LiveData<List<String>>? {
        urls = imageRepository.getImageURLS(storageReference, type, name)
        return urls
    }

    fun getProductsForMeal(mealName: String) = mealRepository.getProductsForMeal(mealName)


}