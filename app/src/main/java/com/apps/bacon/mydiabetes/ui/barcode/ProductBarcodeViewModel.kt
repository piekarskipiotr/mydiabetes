package com.apps.bacon.mydiabetes.ui.barcode

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductBarcodeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    fun checkForBarcodeExist(barcode: String, currentBarcode: String?): Boolean {
        return if (barcode == currentBarcode)
            false
        else
            productRepository.checkForBarcodeExist(barcode)
    }
}