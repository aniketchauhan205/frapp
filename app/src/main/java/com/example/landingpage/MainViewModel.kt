// MainViewModel.kt
package com.example.landingpage.com.example.landingpage

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.landingpage.com.example.landingpage.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "image_database"
    ).build()

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps: StateFlow<List<Bitmap>> = _bitmaps.asStateFlow()

    init {
        viewModelScope.launch {
            database.imageDao().getAllImages().collect { imageEntities ->
                _bitmaps.value = imageEntities.map { byteArrayToBitmap(it.image) }
            }
        }
    }

    fun onTakePhoto(bitmap: Bitmap) {
        viewModelScope.launch {
            val byteArray = bitmapToByteArray(bitmap)
            database.imageDao().insertImage(ImageEntity(image = byteArray))
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
