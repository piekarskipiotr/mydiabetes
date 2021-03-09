package com.apps.bacon.mydiabetes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    @ColumnInfo(name = "tag_id")
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "tag_name")
    var name: String
)