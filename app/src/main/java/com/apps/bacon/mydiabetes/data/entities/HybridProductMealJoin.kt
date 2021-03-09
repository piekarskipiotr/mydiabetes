package com.apps.bacon.mydiabetes.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "hybrid_product_meal_join",
    primaryKeys = ["productId", "mealId"],
    foreignKeys = [ForeignKey(
        entity = StaticProduct::class,
        parentColumns = ["product_id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Meal::class,
        parentColumns = ["meal_id"],
        childColumns = ["mealId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HybridProductMealJoin(
    val productId: Int,
    val mealId: Int
)