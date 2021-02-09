package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.Image
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.data.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Named

class ProductViewModel @ViewModelInject
constructor(
    @Named("product_repository")
    private val repository: ProductRepository
) : ViewModel(){

    fun getProduct(id: Int) = repository.getProduct(id)

    fun getTag(id: Int) = repository.getTag(id)

    fun getLastId() = repository.getLastId()

    fun getProductsInPlate() = repository.getProductsInPlate()

    fun getProductByBarcode(barcode: String) = repository.getProductByBarcode(barcode)

    fun getImagesByProductId(productId: Int) = repository.getImagesByProductId(productId)

    fun deleteProduct(product: Product) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteProduct(product)
    }

    fun updateProduct(product: Product) = CoroutineScope(Dispatchers.Main).launch {
        repository.updateProduct(product)
    }

    fun insertImage(image: Image) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertImage(image)
    }

    fun deleteImage(image: Image) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteImage(image)
    }

}