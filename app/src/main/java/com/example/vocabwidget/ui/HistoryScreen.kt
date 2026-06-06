package com.example.vocabwidget.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vocabwidget.VocabRepository
import com.example.vocabwidget.Word

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    history: List<String>,
    onWordClick: (Word) -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit
) {
    val words = history.mapNotNull { term -> VocabRepository.getAllWords().find { it.term == term } }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = onClear) {
                        Icon(Icons.Default.DeleteSweep, null)
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (words.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No history yet.", color = androidx.compose.ui.graphics.Color.Gray)
                }
            }
            words.forEach { w ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable { onWordClick(w) }
                ) {
                    Text(w.term, Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
