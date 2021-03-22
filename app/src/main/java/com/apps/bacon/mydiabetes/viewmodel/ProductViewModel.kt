package com.apps.bacon.mydiabetes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Named

class ProductViewModel @ViewModelInject
constructor(
    @Named("product_repository")
    private val repository: ProductRepository
) : ViewModel() {
    private val myPagingConfig = Config(
        pageSize = 15,
        prefetchDistance = 10,
        enablePlaceholders = true
    )

    fun checkForProductExist(name: String) = repository.checkForProductExist(name)

    fun getAll() = repository.getAll()

    fun getAllLocal() = repository.getAllLocal()

    fun getPagingListOfProducts(): LiveData<PagedList<Product>> {
        return LivePagedListBuilder(repository.getAllPaging(), myPagingConfig).build()
    }

    fun getAllByTag(tagId: Int) = repository.getAllByTag(tagId)

    fun getPagingListOfProductsByTag(tagId: Int): LiveData<PagedList<Product>> {
        return LivePagedListBuilder(repository.getAllByTagPaging(tagId), myPagingConfig).build()
    }

    fun getProduct(id: Int) = repository.getProduct(id)

    fun getProductByBarcode(barcode: String) = repository.getProductByBarcode(barcode)

    fun getProductsInPlate() = repository.getProductsInPlate()

    fun insert(product: Product) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(product)
    }

    fun update(product: Product) = CoroutineScope(Dispatchers.Default).launch {
        repository.update(product)
    }

    fun delete(product: Product) = CoroutineScope(Dispatchers.Default).launch {
        repository.delete(product)
    }
}
