package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.TagRepository

@Suppress("UNCHECKED_CAST")
class TagViewModelFactory constructor(
    private val repository: TagRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TagViewModel(repository) as T
    }
}