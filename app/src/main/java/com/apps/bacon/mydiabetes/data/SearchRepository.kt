package com.apps.bacon.mydiabetes.data

class SearchRepository constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.productDao().getAll()

    fun getProductByBarcode(barcode: String) = database.productDao().getProductByBarcode(barcode)
}