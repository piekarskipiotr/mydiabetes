package com.apps.bacon.mydiabetes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class, Tag::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun tagDao(): TagDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null
        private const val DATABASE_NAME: String = "products_database"

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build()
            return instance!!

        }

        private val roomCallback = object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.Main).launch{
                    for (i in preTagData)
                        instance!!.tagDao().insert(i)
                }
            }
        }

        private val preTagData = listOf(
            Tag(1, "Mięsa"),
            Tag(2, "Ryby"),
            Tag(3, "Nabiał"),
            Tag(4, "Pieczywo"),
            Tag(5, "Warzywa i owoce"),
            Tag(6, "Słodycze i przekąski"),
            Tag(7, "Napoje"),
            Tag(8, "Orzechy"),
            Tag(9, "Inne"),
        )
    }

}