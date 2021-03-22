package com.apps.bacon.mydiabetes.data.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "product_meal_join",
    indices = [
        Index("product_name"),
        Index("meal_name")
              ],
    primaryKeys = ["product_name", "meal_name"],
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = ["product_name"],
        childColumns = ["product_name"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = Meal::class,
        parentColumns = ["meal_name"],
        childColumns = ["meal_name"],
        onDelete = CASCADE
    )]
)
data class ProductMealJoin(

    @ColumnInfo(name = "product_name")
    var productName: String,

    @ColumnInfo(name = "meal_name")
    var mealName: String
)