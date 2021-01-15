package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.data.SaveProductRepository
import com.apps.bacon.mydiabetes.data.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveProductViewModel constructor(
    private val repository: SaveProductRepository) : ViewModel() {

        fun getAllTags() = repository.getAllTag()

        fun getTagById(id: Int) = repository.getTagById(id)

        fun insertTag(tag: Tag) = CoroutineScope(Dispatchers.Main).launch {
            repository.insertTag(tag)
        }

        fun insertProduct(product: Product) = CoroutineScope(Dispatchers.Main).launch {
            repository.insertProduct(product)
        }

        fun deleteTagById(id: Int) = CoroutineScope(Dispatchers.Main).launch {
            repository.deleteTagById(id)
        }
}