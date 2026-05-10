package com.example.weathersnap.ui.createreport

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weathersnap.domain.model.WeatherData
import com.example.weathersnap.ui.theme.*
import com.example.weathersnap.ui.weather.WeatherCard
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(
    weatherData: WeatherData,
    onNavigateToCamera: () -> Unit,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: CreateReportViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showPermissionRationale by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onNavigateToCamera()
        } else {
            showPermissionRationale = true
        }
    }

    LaunchedEffect(weatherData) {
        viewModel.setWeatherData(weatherData)
    }

    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Camera Permission Required") },
            text = { Text("We need camera access to take weather evidence photos for your report.") },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionRationale = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionRationale = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(OliveHeader)
                .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Create Report",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkGreenText
                    )
                    Text(
                        text = "Capture, compress, annotate",
                        style = MaterialTheme.typography.labelMedium,
                        color = DarkGreenText.copy(alpha = 0.7f)
                    )
                }
                OutlinedButton(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(50),
                    border = ButtonDefaults.outlinedButtonBorder(true).copy(width = 1.dp)
                ) {
                    Text("Back", color = DarkGreenText)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Weather Summary
            WeatherCard(data = weatherData, onCreateReport = {}) 
            
            Spacer(modifier = Modifier.height(24.dp))

            // Image Preview Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ImagePlaceholder)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Crossfade(targetState = uiState.compressionResult, label = "ImagePreview") { result ->
                        if (result != null && File(result.compressedPath).exists()) {
                            AsyncImage(
                                model = File(result.compressedPath),
                                contentDescription = "Compressed Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("Photo preview", color = Color.White.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            if (uiState.compressionResult != null) {
                val res = uiState.compressionResult!!
                Text(
                    text = "Original: ${res.originalSize / 1024} KB → Compressed: ${res.compressedSize / 1024} KB",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        onNavigateToCamera()
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = OliveFill, contentColor = DarkGreenText),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (uiState.compressionResult == null) "Capture Photo" else "Retake Photo")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Notes Section
            Text(
                text = "Field Notes",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = uiState.notes,
                onValueChange = viewModel::onNotesChanged,
                modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
                label = { Text("Notes") },
                placeholder = { Text("How is the weather feeling?") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.saveReport(onSaveSuccess) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OliveFill, contentColor = DarkGreenText),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState.compressionResult != null && uiState.notes.trim().length >= 2
            ) {
                Text("Save Report", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
