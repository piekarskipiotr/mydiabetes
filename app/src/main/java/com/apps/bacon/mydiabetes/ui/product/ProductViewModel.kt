package com.apps.bacon.mydiabetes.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.apps.bacon.mydiabetes.data.entities.Image
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.repositories.ImageRepository
import com.apps.bacon.mydiabetes.data.repositories.MealRepository
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import com.apps.bacon.mydiabetes.data.repositories.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProductViewModel @Inject
constructor(
    private val productRepository: ProductRepository,
    private val imageRepository: ImageRepository,
    private val tagRepository: TagRepository,
    private val mealRepository: MealRepository

) : ViewModel() {
    private val myPagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        prefetchDistance = 5
    )

    fun getLastId() = tagRepository.getLastId()

    fun getImageByProductId(productId: Int) = imageRepository.getImageByProductId(productId)

    fun getTagById(productTagId: Int) = tagRepository.getTagById(productTagId)

    fun isProductInMeal(productName: String) = mealRepository.isProductInMeal(productName)

    fun checkForProductExist(name: String, currentName: String?): Boolean {
        return if (currentName.equals(name, ignoreCase = true))
            false
        else
            productRepository.checkForProductExist(name)
    }

    fun checkForBarcodeExist(barcode: String, currentBarcode: String?): Boolean {
        return if (barcode == currentBarcode)
            false
        else
            productRepository.checkForBarcodeExist(barcode)
    }

    fun getAll() = productRepository.getAll()

    fun getAllLocal() = productRepository.getAllLocal()

    fun getPagingListOfProducts() = Pager(
        config = myPagingConfig, pagingSourceFactory = { productRepository.getAllPaging() }
    ).flow.cachedIn(viewModelScope)

    fun getAllByTag(tagId: Int) = productRepository.getAllByTag(tagId)

    fun getPagingListOfProductsByTag(tagId: Int) = Pager(
        config = myPagingConfig, pagingSourceFactory = { productRepository.getAllByTagPaging(tagId) }
    ).flow.cachedIn(viewModelScope)

    fun getProduct(id: Int) = productRepository.getProduct(id)

    fun getProductByBarcode(barcode: String) = productRepository.getProductByBarcode(barcode)

    fun getProductsInPlate() = productRepository.getProductsInPlate()

    fun renamePMJProductName(product: Product, oldName: String, newName: String) =
        CoroutineScope(Dispatchers.Default).launch {
            productRepository.renamePMJProductName(product, oldName, newName)
        }

    fun insert(product: Product) = viewModelScope.launch(Dispatchers.Default) {
        productRepository.insert(product)
    }

    fun update(product: Product) = viewModelScope.launch(Dispatchers.Default) {
        productRepository.update(product)
    }

    fun delete(product: Product) = viewModelScope.launch(Dispatchers.Default) {
        productRepository.delete(product)
    }

    fun insert(image: Image) = viewModelScope.launch(Dispatchers.Default) {
        imageRepository.insert(image)
    }

    fun delete(image: Image) = viewModelScope.launch(Dispatchers.Default) {
        imageRepository.delete(image)
    }
}

