package com.apps.bacon.mydiabetes.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "product_meal_join",
    primaryKeys = ["productId", "mealId"],
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = ["product_id"],
        childColumns = ["productId"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = Meal::class,
        parentColumns = ["meal_id"],
        childColumns = ["mealId"],
        onDelete = CASCADE
    )]
)
data class ProductMealJoin(
    val productId: Int,
    val mealId: Int
)