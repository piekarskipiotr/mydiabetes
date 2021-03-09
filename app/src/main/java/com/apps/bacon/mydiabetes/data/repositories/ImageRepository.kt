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

    private val imageURLs = MutableLiveData<List<String>>()

    fun getImageURLS(
        storageReference: StorageReference,
        type: String,
        id: Int
    ): MutableLiveData<List<String>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                storageReference.child("${type}_images/$id").listAll().addOnCompleteListener {
                    for (image in it.result.items) {
                        image.downloadUrl.addOnCompleteListener { u ->
                            imageURLs.value = imageURLs.value.orEmpty() + u.result.toString()
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