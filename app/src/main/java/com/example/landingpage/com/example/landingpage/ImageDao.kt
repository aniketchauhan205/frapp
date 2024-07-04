package com.example.landingpage.com.example.landingpage.com.example.landingpage
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.landingpage.com.example.landingpage.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(image: ImageEntity)

    @Query("SELECT * FROM images")
    fun getAllImages(): Flow<List<ImageEntity>>
}