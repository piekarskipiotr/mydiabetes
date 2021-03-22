package com.apps.bacon.mydiabetes.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.entities.Image
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getAll() = database.imageDao().getAll()

    fun getImageByProductId(id: Int) = database.imageDao().getImageByProductId(id)

    fun getImageByMealId(id: Int) = database.imageDao().getImageByMealId(id)

    suspend fun insert(image: Image) = database.imageDao().insert(image)

    suspend fun update(image: Image) = database.imageDao().update(image)

    suspend fun delete(image: Image) = database.imageDao().delete(image)

   fun getImageURLS(
        storageReference: StorageReference,
        type: String,
        name: String
    ): MutableLiveData<List<String>> {
        val imageURLs = MutableLiveData<List<String>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                storageReference.child("${type}_images/$name/").listAll().addOnSuccessListener {
                    for (image in it.items) {
                        image.downloadUrl.addOnSuccessListener { url ->
                            imageURLs.value = imageURLs.value.orEmpty() + url.toString()
                        }
                    }

                }
            } catch (e: Exception) {
                Log.e("FirebaseStorage:", e.toString())
            }
        }

        return imageURLs
    }
}