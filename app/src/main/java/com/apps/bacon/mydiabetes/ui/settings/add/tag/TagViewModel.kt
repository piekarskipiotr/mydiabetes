package com.apps.bacon.mydiabetes.ui.settings.add.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.Tag
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import com.apps.bacon.mydiabetes.data.repositories.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TagViewModel @Inject
constructor(
    private val tagRepository: TagRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    fun checkForTagExist(name: String) = tagRepository.checkForTagExist(name)

    fun getAll() = tagRepository.getAll()

    fun getAllByTag(tagId: Int) = productRepository.getAllByTag(tagId)

    fun update(product: Product) = viewModelScope.launch(Dispatchers.Default) {
        productRepository.update(product)
    }

    fun insert(tag: Tag) = viewModelScope.launch(Dispatchers.Default) {
        tagRepository.insert(tag)
    }

    fun delete(tag: Tag) = viewModelScope.launch(Dispatchers.Default) {
        tagRepository.delete(tag)
    }
}
