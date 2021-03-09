package com.apps.bacon.mydiabetes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "static_products")
data class StaticProduct(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var id: Int,

    @ColumnInfo(name = "product_name")
    var name: String,

    @ColumnInfo(name = "weight")
    var weight: Double?,

    @ColumnInfo(name = "pieces")
    var pieces: Int?,

    @ColumnInfo(name = "carbohydrates")
    var carbohydrates: Double,

    @ColumnInfo(name = "calories")
    var calories: Double?,

    @ColumnInfo(name = "protein")
    var protein: Double?,

    @ColumnInfo(name = "fat")
    var fat: Double?,

    @ColumnInfo(name = "carbohydrateExchangers")
    var carbohydrateExchangers: Double,

    @ColumnInfo(name = "proteinFatExchangers")
    var proteinFatExchangers: Double,

    @ColumnInfo(name = "product_tag")
    var tag: Int?,

    @ColumnInfo(name = "barcode")
    var barcode: String?,

    @ColumnInfo(name = "in_food_plate")
    var inFoodPlate: Boolean,

    @ColumnInfo(name = "icon")
    var icon: String?
)