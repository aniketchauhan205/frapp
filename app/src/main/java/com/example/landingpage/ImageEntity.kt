package com.example.landingpage.com.example.landingpage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val image: ByteArray
)