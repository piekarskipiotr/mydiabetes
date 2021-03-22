package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.entities.Image
import com.apps.bacon.mydiabetes.data.repositories.ImageRepository
import com.google.firebase.storage.StorageReference
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

    fun getImageByMealId(id: Int) = repository.getImageByMealId(id)

    fun insert(image: Image) = CoroutineScope(Dispatchers.Default).launch {
        repository.insert(image)
    }

    fun update(image: Image) = CoroutineScope(Dispatchers.Default).launch {
        repository.update(image)
    }

    fun delete(image: Image) = CoroutineScope(Dispatchers.Default).launch {
        repository.delete(image)
    }

    var urls: MutableLiveData<List<String>>? = null

    fun getURL(storageReference: StorageReference,
               type: String,
               id: Int): LiveData<List<String>>? {
        urls = repository.getImageURLS(storageReference, type, id)

        return urls
    }
}