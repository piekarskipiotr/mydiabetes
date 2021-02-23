package com.apps.bacon.mydiabetes.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "product_meal_join",
    primaryKeys = ["productId", "mealId"],
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = ["product_id"],
        childColumns = ["productId"],
    ), ForeignKey(
        entity = Meal::class,
        parentColumns = ["meal_id"],
        childColumns = ["mealId"]
    )]
)
data class ProductMealJoin(
    val productId: Int,
    val mealId: Int
)