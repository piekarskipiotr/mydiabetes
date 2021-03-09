package com.apps.bacon.mydiabetes.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.repositories.ProductRepository
import com.apps.bacon.mydiabetes.data.entities.StaticProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Named

class ProductViewModel @ViewModelInject
constructor(
    @Named("product_repository")
    private val repository: ProductRepository
) : ViewModel() {
    fun checkForProductExist(name: String) = repository.checkForProductExist(name)

    fun getAll() = repository.getAll()

    fun getAllByTag(tagId: Int) = repository.getAllByTag(tagId)

    fun getProduct(id: Int) = repository.getProduct(id)

    fun getProductByBarcode(barcode: String) = repository.getProductByBarcode(barcode)

    fun getProductsInPlate() = repository.getProductsInPlate()

    fun insert(product: Product) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(product)
    }

    fun update(product: Product) = CoroutineScope(Dispatchers.IO).launch {
        repository.update(product)
    }

    fun delete(product: Product) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(product)
    }

    /*
   * Below is section of statics functions
   * */

    fun checkForStaticProductExist(name: String) = repository.checkForStaticProductExist(name)

    fun getAllStatics() = repository.getAllStatics()

    fun getAllStaticsByTag(tagId: Int) = repository.getAllStaticsByTag(tagId)

    fun getStaticProduct(id: Int) = repository.getStaticProduct(id)

    fun getStaticProductByBarcode(barcode: String) = repository.getStaticProductByBarcode(barcode)

    fun getStaticProductsInPlate() = repository.getStaticProductsInPlate()

    fun insert(staticProduct: StaticProduct) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(staticProduct)
    }

    fun update(staticProduct: StaticProduct) = CoroutineScope(Dispatchers.IO).launch {
        repository.update(staticProduct)
    }
}