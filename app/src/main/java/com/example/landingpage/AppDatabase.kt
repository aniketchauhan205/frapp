package com.example.landingpage.com.example.landingpage
//
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.landingpage.Employee
//import com.example.landingpage.EmployeeDao
//
//@Database(entities = [Employee::class], version = 1)
//abstract class ApppDatabase : RoomDatabase(){
//    abstract fun employeeDao(): EmployeeDao
//    companion object{
//        @Volatile
//        private var INSTANCE: ApppDatabase? = null
//
//        fun getDatabase(context: Context): ApppDatabase {
//            return INSTANCE ?: synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    ApppDatabase::class.java,
//                    "employee_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.landingpage.Employee
import com.example.landingpage.EmployeeDao

@Database(entities = [Employee::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "employee_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}