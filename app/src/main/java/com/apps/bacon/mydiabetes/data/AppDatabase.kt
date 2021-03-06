package com.apps.bacon.mydiabetes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.dao.*
import com.apps.bacon.mydiabetes.data.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Product::class,
        Tag::class,
        Image::class,
        Meal::class,
        ProductMealJoin::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun tagDao(): TagDao
    abstract fun imageDao(): ImageDao
    abstract fun mealDao(): MealDao
    abstract fun productMealJoinDao(): ProductMealJoinDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private const val DATABASE_NAME: String = "products_database"

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val preTagData = listOf(
                                Tag(1, context.getString(R.string.meat)),
                                Tag(2, context.getString(R.string.fish)),
                                Tag(3, context.getString(R.string.protein)),
                                Tag(4, context.getString(R.string.bread)),
                                Tag(5, context.getString(R.string.vegetables_and_fruits)),
                                Tag(6, context.getString(R.string.sweets_and_snacks)),
                                Tag(7, context.getString(R.string.drinks)),
                                Tag(8, context.getString(R.string.nuts)),
                                Tag(9, context.getString(R.string.others))
                            )
                            CoroutineScope(Dispatchers.Default).launch {
                                for (tag in preTagData)
                                    instance!!.tagDao().insert(tag)
                            }
                        }
                    })
                    .allowMainThreadQueries()
                    .build()
            return instance!!
        }
    }
}