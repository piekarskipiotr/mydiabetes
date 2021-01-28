package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.productDao().getAll()

    fun getProductByBarcode(barcode: String) = database.productDao().getProductByBarcode(barcode)
}