package com.apps.bacon.mydiabetes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.mydiabetes.data.entities.Tag

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    fun getAll(): LiveData<List<Tag>>

    @Query("SELECT MAX(tag_id) FROM tags")
    fun getLastId(): Int

    @Query("SELECT * FROM tags WHERE tag_id == :id")
    fun getTagById(id: Int): Tag

    @Insert
    suspend fun insert(tag: Tag)

    @Update
    suspend fun update(tag: Tag)

    @Delete
    suspend fun delete(tag: Tag)

    @Query("DELETE FROM tags WHERE tag_id == :id")
    suspend fun deleteById(id: Int)
}