package com.apps.bacon.mydiabetes.ui.save.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import com.apps.bacon.mydiabetes.data.repositories.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val tagRepository: TagRepository
) : ViewModel(){
    fun getAllTags() = tagRepository.getAll()

    fun getTagById(tagId: Int) = tagRepository.getTagById(tagId)

    fun deleteById(tagId: Int) = viewModelScope.launch(Dispatchers.Default) {
        tagRepository.deleteById(tagId)
    }

    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.Default) {
        productRepository.insert(product)
    }
}