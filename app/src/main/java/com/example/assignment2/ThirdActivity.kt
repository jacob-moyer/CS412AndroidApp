package com.example.assignment2

import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.assignment2.ui.theme.Assignment2Theme
import java.io.File

class ThirdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CaptureImageScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Creates a temp file in cache/images, returns the uri of the image
private fun createImageUri(context: Context): Uri {
    val imageDir = File(context.cacheDir, "images").apply { mkdirs() }
    val photoFile = File.createTempFile("captured_", ".jpg", imageDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        photoFile
    )
}

//inSampleSize used to save memory for devices reducing size to about 1/16 of the size
private fun loadBitmap(context: Context, uri: Uri): Bitmap? =
    context.contentResolver.openInputStream(uri)?.use { stream ->
        BitmapFactory.decodeStream(
            stream, null,
            BitmapFactory.Options().apply { inSampleSize = 4 }
        )
    }

@Composable
fun CaptureImageScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // rememberSaveable keeps the Uri from the camera and the Uri of the taken picture if the activity is recreated while the camera is open.
    var pendingUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var capturedUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val bitmap = remember(capturedUri) {
        capturedUri?.let { loadBitmap(context, it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) capturedUri = pendingUri
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val uri = createImageUri(context)
            pendingUri = uri
            try {
                cameraLauncher.launch(uri)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "No camera app available", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Capture Image")
        }

        Spacer(modifier = Modifier.height(24.dp))

        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Captured image",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}