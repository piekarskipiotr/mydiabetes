package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    fun getAll(): LiveData<List<Tag>>

    @Query("SELECT * FROM tags WHERE tag_id == :id")
    fun getTagById(id: Int): Tag

    @Insert
    fun insert(tag: Tag)

    @Update
    fun update(tag: Tag)

    @Delete
    fun delete(tag: Tag)

    @Query("DELETE FROM tags WHERE tag_id == :id")
    fun deleteById(id: Int)


}