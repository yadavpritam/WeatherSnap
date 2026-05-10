package com.example.weathersnap.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weathersnap.domain.model.WeatherReport
import com.example.weathersnap.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val reports by viewModel.reports.collectAsState()

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
                        text = "Saved Reports",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkGreenText
                    )
                    Text(
                        text = "${reports.size} report(s) stored locally",
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

        if (reports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = null, 
                        modifier = Modifier.size(64.dp).background(ChipBackground, RoundedCornerShape(50)).padding(16.dp),
                        tint = ChipText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No reports yet", style = MaterialTheme.typography.headlineMedium, color = DarkGreenText)
                    Text("Your saved weather reports will appear here.", style = MaterialTheme.typography.bodyMedium, color = MutedGray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(reports) { report ->
                    ReportItemCard(report = report)
                }
            }
        }
    }
}

@Composable
fun ReportItemCard(report: WeatherReport) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column {
            if (report.imagePath.isNotBlank() && File(report.imagePath).exists()) {
                AsyncImage(
                    model = File(report.imagePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(ImagePlaceholder)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.White.copy(alpha = 0.5f))
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${report.cityName}, ${report.condition}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreenText
                        )
                        Text(
                            text = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(report.savedAt)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedGray
                        )
                    }
                    Text(
                        text = "${report.temperature.toInt()}°C",
                        style = MaterialTheme.typography.headlineMedium,
                        color = DarkGreenText
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ReportSizeChip(label = "Original: ${report.originalSize / 1024} KB")
                    ReportSizeChip(label = "Compressed: ${report.compressedSize / 1024} KB")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = report.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedGray
                )
            }
        }
    }
}

@Composable
fun ReportSizeChip(label: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = ChipBackground
    ) {
        Text(
            text = label, 
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = ChipText,
            fontSize = 10.sp
        )
    }
}
