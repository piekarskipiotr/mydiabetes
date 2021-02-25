package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.data.Tag
import com.apps.bacon.mydiabetes.data.TagRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Named

class TagViewModel @ViewModelInject
constructor(
    @Named("tag_repository")
    private val repository: TagRepository
) : ViewModel() {
    fun getAll() = repository.getAll()

    fun getLastId() = repository.getLastId()

    fun getTagById(id: Int) = repository.getTagById(id)

    fun insert(tag: Tag) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(tag)
    }

    fun update(tag: Tag) = CoroutineScope(Dispatchers.IO).launch {
        repository.update(tag)
    }

    fun delete(tag: Tag) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(tag)
    }

    fun deleteById(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        repository.deleteById(id)
    }

}
