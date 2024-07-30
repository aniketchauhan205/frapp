// MainViewModel.kt
package com.example.landingpage

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.landingpage.com.example.landingpage.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

//class MainViewModel: ViewModel() {
//
//    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
//    val bitmaps = _bitmaps.asStateFlow()
//
//    fun onTakePhoto(bitmap: Bitmap) {
//        _bitmaps.value += bitmap
//    }
//
//
//}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val employeeDao = database.employeeDao()

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _employees.asStateFlow()

    private val _photoPaths= MutableStateFlow<List<String>>(emptyList())
    val photoPaths = _photoPaths.asStateFlow()

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()
    fun onTakePhoto(bitmap : Bitmap) {
        _bitmaps.value += bitmap
    }

    init {
        loadEmployees()
    }

    fun saveEmployee(name: String, employeeId: String, photoPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val employee = Employee(0, name, employeeId, photoPath)
            val file = File(getApplication<Application>().filesDir, "employees.json")
            file.appendText("${employee.name},${employee.employeeId},${employee.photoPath}\n")
            loadEmployees()
        }
    }

    fun loadEmployees() {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(getApplication<Application>().filesDir, "employees.json")
            if (file.exists()) {
                val employeesList = file.readLines().map {
                    val parts = it.split(",")
                    Employee(0, parts[0], parts[1], parts[2])
                }
                _employees.value = employeesList
            }

        }
    }

    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(getApplication<Application>().filesDir, "employees.txt")
            if (file.exists()) {
                val updatedEmployees = file.readLines().filterNot {
                    val parts = it.split(",")
                    parts[1] == employee.employeeId
                }
                file.writeText(updatedEmployees.joinToString("\n"))
                loadEmployees()
                Log.d("MainViewModel", "Employee deleted: $employee")
                Log.d("MainViewModel", "Updated employees: $updatedEmployees")
            }
        }
    }

}