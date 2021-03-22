package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.entities.Tag
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
    @Named("tag_repository")
    private val repository: TagRepository
) : ViewModel() {
    fun checkForTagExist(name: String) = repository.checkForTagExist(name)

    fun getAll() = repository.getAll()

    fun getLastId() = repository.getLastId()

    fun getTagById(id: Int) = repository.getTagById(id)

    fun insert(tag: Tag) = CoroutineScope(Dispatchers.Default).launch {
        repository.insert(tag)
    }

    fun update(tag: Tag) = CoroutineScope(Dispatchers.Default).launch {
        repository.update(tag)
    }

    fun delete(tag: Tag) = CoroutineScope(Dispatchers.Default).launch {
        repository.delete(tag)
    }

    fun deleteById(id: Int) = CoroutineScope(Dispatchers.Default).launch {
        repository.deleteById(id)
    }
}
