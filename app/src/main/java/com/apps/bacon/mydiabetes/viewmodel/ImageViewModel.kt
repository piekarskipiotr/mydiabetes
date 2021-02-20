package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.Image
import com.apps.bacon.mydiabetes.data.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Named

class ImageViewModel @ViewModelInject
constructor(
    @Named("image_repository")
    private val repository: ImageRepository
) : ViewModel() {

    fun getAll() = repository.getAll()

    fun getImageByProductId(id: Int) = repository.getImageByProductId(id)

    fun insert(image: Image) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(image)
    }

    fun update(image: Image) = CoroutineScope(Dispatchers.Main).launch {
        repository.update(image)
    }

    fun delete(image: Image) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(image)
    }

}