package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.HomeRepository
import javax.inject.Named

class HomeViewModel @ViewModelInject
constructor(
    @Named("home_repository")
    private val repository: HomeRepository
) : ViewModel(){
    var currentTag = MutableLiveData<Int>()

    fun getAll() = repository.getAll()

    fun isSomethingInFoodPlate() = repository.isSomethingInFoodPlate()

    fun getProductsByTag(id: Int) = repository.getProductsByTag(id)

    fun getAllTags() = repository.getAllTags()

}