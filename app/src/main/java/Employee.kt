package com.example.landingpage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val employeeId: String,
    val photoPath: String // Store the file path instead of the actual image
)
