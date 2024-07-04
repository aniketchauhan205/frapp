@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.landingpage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.landingpage.ui.theme.LandingPageTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.landingpage.camerafunc as camerafunc
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.rememberCoroutineScope
import com.example.landingpage.com.example.landingpage.MainViewModel
import com.example.landingpage.com.example.landingpage.PhotoBottomSheetContent
import kotlinx.coroutines.launch
import android.graphics.BitmapFactory
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream


//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

// import kotlin.coroutines.jvm.internal.CompletedContinuation.context


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }
        enableEdgeToEdge()
        setContent {
            //RegistrationScreen(onBack = { /* Handle back action here if needed */ })
            // LoginScreen()
            LandingPageTheme {
            //     MainScreen()
                    RegistrationScreen()
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}

@Composable
fun MainScreen() {
    var showRegistration by remember { mutableStateOf(false) }

    //if (showRegistration) {
      //RegistrationScreen(onBack = { showRegistration = false })
    //} else {
        LoginScreen(onRegister = { showRegistration = true })
    //}
}

@Composable
fun LoginScreen(onRegister: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Login Screen")
                // Add your login form here
                Button(onClick = onRegister) {
                    Text("Go to Registration")
                }
            }
        }
    )
}

@Composable
fun RegistrationScreen() {
    var showCamera by remember { mutableStateOf(false) }

    if (showCamera) {
        camerafunc(onBack = { showCamera = false })
    } else {
        RegistrationContent(onOpenCamera = { showCamera = true })
    }
}

@Composable
fun RegistrationContent(onOpenCamera: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register Here", modifier = Modifier.padding(bottom = 20.dp))
        Button(onClick = onOpenCamera) {
            Text("Open Camera")
        }
    }
}

fun takePhoto(context: Context,
                controller: LifecycleCameraController,
              onPhotoTaken: (Bitmap) -> Unit
) {
    val dbHelper = DatabaseHelper(context)
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )
                val byteArray = bitmapToByteArray(rotatedBitmap)
                dbHelper.insertImage(rotatedBitmap)
                onPhotoTaken(rotatedBitmap)
            }

//            override fun onError(exception: ImageCaptureException) {
//                super.onError(exception)
//                Log.e("Camera", "Couldn't take photo: ", exception)
//            }
        }
    )
}

@Composable
@kotlin.OptIn(ExperimentalMaterial3Api::class)
fun camerafunc(onBack: () -> Unit) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val controller = remember{
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
        }

    }
    val scope = rememberCoroutineScope()
    BackHandler {
        onBack()
    }

    val viewModel = viewModel<MainViewModel>()
    val bitmaps by viewModel.bitmaps.collectAsState()

    // Retrieve images from database
//    val dbHelper = DatabaseHelper(context)
//    val images = dbHelper.getAllImages()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 10.dp,
        sheetContent ={
            PhotoBottomSheetContent(
                bitmaps = bitmaps,
                modifier = Modifier
                    .fillMaxWidth())

        } ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CamPrev(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
            )

            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch Camera"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "Open gallery"
                    )
                }
                IconButton(
                    onClick = {
                        takePhoto(
                            context = context,
                            controller = controller,
                            onPhotoTaken = viewModel::onTakePhoto
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Take photo"
                    )
                }
            }

        }
    }
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}