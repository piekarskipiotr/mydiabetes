package com.apps.bacon.mydiabetes.ui.stable.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.repositories.ImageRepository
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import com.apps.bacon.mydiabetes.data.repositories.TagRepository
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaticProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val imageRepository: ImageRepository,
    private val tagRepository: TagRepository
) : ViewModel() {
    fun getProduct(productId: Int) = productRepository.getProduct(productId)

    var urls: MutableLiveData<List<String>>? = null

    fun getURL(
        storageReference: StorageReference,
        type: String,
        name: String
    ): LiveData<List<String>>? {
        urls = imageRepository.getImageURLS(storageReference, type, name)
        return urls
    }

    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.Default) {
        productRepository.update(product)
    }

    fun getTagById(tagId: Int) = tagRepository.getTagById(tagId)
}