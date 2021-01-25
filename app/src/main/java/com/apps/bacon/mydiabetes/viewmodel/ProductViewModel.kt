package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.data.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel constructor(
    private val repository: ProductRepository
) : ViewModel(){

    fun getProduct(id: Int) = repository.getProduct(id)

    fun getTag(id: Int) = repository.getTag(id)

    fun getProductsInPlate() = repository.getProductsInPlate()

    fun deleteProduct(product: Product) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteProduct(product)
    }

    fun updateProduct(product: Product) = CoroutineScope(Dispatchers.Main).launch {
        repository.updateProduct(product)
    }

}