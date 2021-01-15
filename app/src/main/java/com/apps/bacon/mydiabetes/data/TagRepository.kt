package com.apps.bacon.mydiabetes.data

class TagRepository constructor(
    private val database: AppDatabase){
    fun getAll() = database.tagDao().getAll()

    suspend fun insert(tag: Tag) = database.tagDao().insert(tag)

    suspend fun delete(tag: Tag) = database.tagDao().delete(tag)

}