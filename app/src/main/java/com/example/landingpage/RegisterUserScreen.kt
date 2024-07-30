package com.example.landingpage.com.example.landingpage


import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landingpage.MainViewModel
import java.io.File

@Composable
fun RegisterUserScreen(
    bitmap: Bitmap,
    onSave: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var employeeName by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }
    val viewModel: MainViewModel = viewModel()  // Get the ViewModel instance
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Register User", style = MaterialTheme.typography.headlineMedium)

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Captured photo",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        TextField(
            value = employeeName,
            onValueChange = { employeeName = it },
            label = { Text("Employee Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = employeeId,
            onValueChange = { employeeId = it },
            label = { Text("Employee ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) {
                Text("Back")
            }
            Button(
                onClick = {
                    if (employeeName.isNotBlank() && employeeId.isNotBlank()) {
                        val photoPath = saveBitmapToFile(context, bitmap, "$employeeId.jpg")
                        onSave(employeeName, employeeId, photoPath)
                    }
                },
                enabled = employeeName.isNotBlank() && employeeId.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }
}
fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String): String {
    val file = File(context.filesDir, filename)
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return file.absolutePath
}