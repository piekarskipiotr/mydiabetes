package com.apps.bacon.mydiabetes.data

import javax.inject.Inject

class TagRepository @Inject constructor(
    private val database: AppDatabase
){
    fun getAll() = database.tagDao().getAll()

    fun getLastId() = database.tagDao().getLastId()

    fun getTagById(id: Int) = database.tagDao().getTagById(id)

    suspend fun insert(tag: Tag) = database.tagDao().insert(tag)

    suspend fun update(tag: Tag) = database.tagDao().update(tag)

    suspend fun delete(tag: Tag) = database.tagDao().delete(tag)

    suspend fun deleteById(id: Int) = database.tagDao().deleteById(id)

}