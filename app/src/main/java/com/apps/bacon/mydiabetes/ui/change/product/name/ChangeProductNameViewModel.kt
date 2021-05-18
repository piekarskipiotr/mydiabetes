package com.apps.bacon.mydiabetes.ui.change.product.name

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeProductNameViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    fun checkForProductExist(name: String, currentName: String?): Boolean {
        return if (currentName.equals(name, ignoreCase = true))
            false
        else
            productRepository.checkForProductExist(name)
    }
}