package com.example.vocabwidget.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    speechRate: Float,
    onSpeechRateChange: (Float) -> Unit,
    onBack: () -> Unit,
    onReset: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { p ->
        Column(modifier = Modifier.padding(p).padding(24.dp)) {
            Text("Audio Settings", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
            Text(String.format(Locale.getDefault(), "Speech Rate: %.1fx", speechRate))
            Slider(value = speechRate, onValueChange = onSpeechRateChange, valueRange = 0.5f..2.0f)
            
            Spacer(Modifier.height(40.dp))
            Text("Data Management", fontWeight = FontWeight.Bold, color = Color.Red)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset All Data")
            }
        }
    }
}
