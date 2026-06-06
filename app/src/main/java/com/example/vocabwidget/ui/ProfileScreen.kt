package com.example.vocabwidget.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocabwidget.VocabViewModel
import com.example.vocabwidget.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: VocabViewModel,
    onBack: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onExport: () -> Unit
) {
    val masteredCount = viewModel.masteredWords.size
    val learnedCount = viewModel.learnedWords.size
    val xp = viewModel.xp.intValue

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(BluePrimary.copy(alpha = 0.1f)),
                Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = BluePrimary, modifier = Modifier.size(50.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("David Miller", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                StatItem(xp.toString(), "XP Earned")
                StatItem(masteredCount.toString(), "Mastered")
                StatItem(learnedCount.toString(), "Learning")
            }
            Spacer(Modifier.height(32.dp))
            Text("Weekly Activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(16.dp))
            ActivityChart()
            Spacer(Modifier.height(32.dp))
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                Column {
                    ProfileLink(Icons.Default.History, "Study History") { onNavigateToHistory() }
                    ProfileLink(Icons.Default.FileDownload, "Export Progress") { onExport() }
                    ProfileLink(Icons.Default.Settings, "Settings") { onNavigateToSettings() }
                }
            }
        }
    }
}

@Composable
fun ActivityChart() {
    val data = listOf(0.4f, 0.7f, 0.2f, 0.9f, 0.5f, 0.8f, 0.6f)
    Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
        val width = size.width / 7
        data.forEachIndexed { i, value ->
            drawRoundRect(
                color = BluePrimary.copy(alpha = if(i == 6) 1f else 0.3f),
                topLeft = Offset(i * width + 10.dp.toPx(), size.height - (value * size.height)),
                size = Size(width - 20.dp.toPx(), value * size.height),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
        }
    }
}

@Composable
fun StatItem(v: String, l: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(v, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
        Text(l, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun ProfileLink(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = BluePrimary)
        Spacer(Modifier.width(16.dp))
        Text(text, fontWeight = FontWeight.Medium)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}
