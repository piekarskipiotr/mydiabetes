package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
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

    fun insert(tag: Tag) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(tag)
    }

    fun delete(tag: Tag) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(tag)
    }

}
