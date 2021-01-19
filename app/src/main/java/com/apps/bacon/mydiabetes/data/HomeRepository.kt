package com.apps.bacon.mydiabetes.data

class HomeRepository constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.productDao().getAll()

    fun getProductsByTag(id: Int) = database.productDao().getAll(id)

    fun getAllTags() = database.tagDao().getAll()
}