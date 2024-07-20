// MainViewModel.kt
package com.example.landingpage

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.example.landingpage.com.example.landingpage.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

//    fun saveEmployee(name: String, employeeId: String, photoPath: String) {
//        viewModelScope.launch {
//            val employee = Employee(0, name, employeeId, photoPath)
//            employeeDao.insertEmployee(employee)
//            loadEmployees()
//        }
//    }
//
//    fun loadEmployees() {
//        viewModelScope.launch {
//            _employees.value = employeeDao.getAllEmployees()
//        }
//    }
//
//    fun deleteEmployee(employee: Employee) {
//        viewModelScope.launch {
//            employeeDao.deleteEmployee(employee)
//            loadEmployees()
//        }
//    }
//this all is the part of database
}