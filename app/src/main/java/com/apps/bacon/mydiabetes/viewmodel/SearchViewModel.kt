package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.SearchRepository
import javax.inject.Named

class SearchViewModel @ViewModelInject
constructor(
    @Named("search_repository")
    private val repository: SearchRepository
) : ViewModel(){

    fun getAll() = repository.getAll()

    fun getProductByBarcode(barcode: String) = repository.getProductByBarcode(barcode)

}