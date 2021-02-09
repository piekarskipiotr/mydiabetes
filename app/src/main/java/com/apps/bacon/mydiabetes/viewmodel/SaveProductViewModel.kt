package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.Image
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.data.SaveProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Named

class SaveProductViewModel @ViewModelInject
constructor(
    @Named("save_product_repository")
    private val repository: SaveProductRepository
    ) : ViewModel() {

        fun checkForProductExist(name: String) = repository.checkForProductExist(name)

        fun getProduct(name: String) = repository.getProduct(name)

        fun getAllTags() = repository.getAllTag()

        fun getTagById(id: Int) = repository.getTagById(id)

        fun getImagesByProductId(productId: Int) = repository.getImagesByProductId(productId)

        fun insertProduct(product: Product) = CoroutineScope(Dispatchers.Main).launch {
            repository.insertProduct(product)
        }

        fun deleteTagById(id: Int) = CoroutineScope(Dispatchers.Main).launch {
            repository.deleteTagById(id)
        }

        fun insertImage(image: Image) = CoroutineScope(Dispatchers.Main).launch {
            repository.insertImage(image)
        }

        fun deleteImage(image: Image) = CoroutineScope(Dispatchers.Main).launch {
            repository.deleteImage(image)
        }
}