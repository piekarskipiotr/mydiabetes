package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.HomeRepository

class HomeViewModel constructor(
    private val repository: HomeRepository
) : ViewModel(){
    var currentTag = MutableLiveData<Int>()

    fun getAll() = repository.getAll()

    fun getProductsByTag(id: Int) = repository.getProductsByTag(id)

    fun getAllTags() = repository.getAllTags()

}