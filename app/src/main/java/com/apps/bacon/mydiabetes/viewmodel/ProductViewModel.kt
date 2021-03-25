package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProductViewModel @Inject
constructor(
    @Named("product_repository")
    private val repository: ProductRepository
) : ViewModel() {
    private val myPagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        prefetchDistance = 5
    )

    fun checkForProductExist(name: String, currentName: String?): Boolean {
        return if (currentName.equals(name, ignoreCase = true))
            false
        else
            repository.checkForProductExist(name)
    }

    fun checkForBarcodeExist(barcode: String, currentBarcode: String?): Boolean {
        return if (barcode == currentBarcode)
            false
        else
            repository.checkForBarcodeExist(barcode)
    }

    fun getAll() = repository.getAll()

    fun getAllLocal() = repository.getAllLocal()

    fun getPagingListOfProducts() = Pager(
        config = myPagingConfig, pagingSourceFactory = { repository.getAllPaging() }
    ).flow.cachedIn(viewModelScope)

    fun getAllByTag(tagId: Int) = repository.getAllByTag(tagId)

    fun getPagingListOfProductsByTag(tagId: Int) = Pager(
        config = myPagingConfig, pagingSourceFactory = { repository.getAllByTagPaging(tagId) }
    ).flow.cachedIn(viewModelScope)

    fun getProduct(id: Int) = repository.getProduct(id)

    fun getProductByBarcode(barcode: String) = repository.getProductByBarcode(barcode)

    fun getProductsInPlate() = repository.getProductsInPlate()

    fun renamePMJProductName(product: Product, oldName: String, newName: String) =
        CoroutineScope(Dispatchers.Default).launch {
            repository.renamePMJProductName(product, oldName, newName)
        }

    fun insert(product: Product) = CoroutineScope(Dispatchers.Default).launch {
        repository.insert(product)
    }

    fun update(product: Product) = CoroutineScope(Dispatchers.Default).launch {
        repository.update(product)
    }

    fun delete(product: Product) = CoroutineScope(Dispatchers.Default).launch {
        repository.delete(product)
    }
}

