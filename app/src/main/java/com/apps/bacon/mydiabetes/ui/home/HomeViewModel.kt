package com.apps.bacon.mydiabetes.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.apps.bacon.mydiabetes.data.entities.Tag
import com.apps.bacon.mydiabetes.data.repositories.MainRepository
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import com.apps.bacon.mydiabetes.data.repositories.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tagRepository: TagRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    var currentTag = MutableLiveData<Int>()

    private val myPagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        prefetchDistance = 5
    )

    fun getAll() = tagRepository.getAll()

    fun getProductsInPlate() = productRepository.getProductsInPlate()

    fun getPagingListOfProducts() = Pager(
        config = myPagingConfig, pagingSourceFactory = { productRepository.getAllPaging() }
    ).flow.cachedIn(viewModelScope)

    fun getPagingListOfProductsByTag(tagId: Int) = Pager(
        config = myPagingConfig, pagingSourceFactory = { productRepository.getAllByTagPaging(tagId) }
    ).flow.cachedIn(viewModelScope)

    fun getTagById(tagId: Int) = tagRepository.getTagById(tagId)

    private fun updateTag(tag: Tag) = viewModelScope.launch(Dispatchers.Default) {
        tagRepository.update(tag)
    }

    fun translateTags(list: List<String>) {
        for (i in 1..9) {
            val tag = getTagById(i)
            tag.name = list[i.dec()]
            updateTag(tag)
        }
    }
}