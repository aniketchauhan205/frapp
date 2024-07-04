package com.example.landingpage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "images.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "images"
        private const val COLUMN_ID = "id"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_IMAGE BLOB)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertImage(bitmap: Bitmap) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMAGE, bitmapToByteArray(bitmap))
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllImages(): List<Bitmap> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_IMAGE), null, null, null, null, null)
        val images = mutableListOf<Bitmap>()
        if (cursor.moveToFirst()) {
            do {
                val byteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                images.add(bitmap)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return images
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}

