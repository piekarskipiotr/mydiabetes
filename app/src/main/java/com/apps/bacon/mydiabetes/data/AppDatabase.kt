package com.apps.bacon.mydiabetes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

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
                    .build()
            return instance!!

        }

        private val roomCallback = object : Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(database: AppDatabase){
            val tagDao = database.tagDao()
            val listOfTags = arrayListOf(
                "Mięsa",
                "Ryby",
                "Nabiał",
                "Pieczywo",
                "Warzywa i owoce",
                "Słodycze i przekąski",
                "Napoje",
                "Orzechy",
                "Inne"
            )

            for (i in 0 until listOfTags.size){
                tagDao.insert(Tag(i.inc(), listOfTags[i]))
            }

        }

    }

}