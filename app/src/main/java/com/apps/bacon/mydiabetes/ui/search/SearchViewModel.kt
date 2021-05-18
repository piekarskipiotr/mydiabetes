package com.apps.bacon.mydiabetes.ui.search

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject
constructor(
   private val productRepository: ProductRepository
) : ViewModel() {
    fun getAll() = productRepository.getAll()

    fun getProductByBarcode(barcode: String) = productRepository.getProductByBarcode(barcode)
}