package com.example.landingpage.com.example.landingpage


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.landingpage.com.example.landingpage.com.example.landingpage.ImageDao

@Database(entities = [ImageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
