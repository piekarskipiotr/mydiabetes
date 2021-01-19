package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.SearchRepository

class SearchViewModel constructor(
    private val repository: SearchRepository
) : ViewModel(){

    fun getAll() = repository.getAll()

}