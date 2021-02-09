package com.apps.bacon.mydiabetes.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    var id: Int,

    @ColumnInfo(name = "product_id")
    var productId: Int,

    @ColumnInfo(name = "image_uri")
    var image: Uri

)
