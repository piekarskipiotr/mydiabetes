package com.apps.bacon.mydiabetes.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "static_product_meal_join",
    primaryKeys = ["productId", "mealId"],
    foreignKeys = [ForeignKey(
        entity = StaticProduct::class,
        parentColumns = ["product_id"],
        childColumns = ["productId"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = StaticMeal::class,
        parentColumns = ["meal_id"],
        childColumns = ["mealId"],
        onDelete = CASCADE
    )]
)
data class StaticProductMealJoin(
    val productId: Int,
    val mealId: Int
)