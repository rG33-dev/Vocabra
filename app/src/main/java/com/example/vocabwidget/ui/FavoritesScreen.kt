package com.example.vocabwidget.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun FavoritesScreen(
    favorites: List<String>,
    onWordClick: (Word) -> Unit,
    onBack: () -> Unit
) {
    val words = favorites.mapNotNull { term -> VocabRepository.getAllWords().find { it.term == term } }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Saved Words") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { p ->
        Column(modifier = Modifier.padding(p).padding(16.dp)) {
            if (words.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("No saved words yet.")
                }
            }
            words.forEach { w ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable { onWordClick(w) }
                ) {
                    Text(
                        text = w.term,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
