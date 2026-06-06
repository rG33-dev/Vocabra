package com.example.vocabwidget.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocabwidget.VocabRepository
import com.example.vocabwidget.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(onBack: () -> Unit) {
    val words = remember { VocabRepository.getAllWords().shuffled() }
    var cur by remember { mutableIntStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    val rot by animateFloatAsState(if (flipped) 180f else 0f, label = "flip")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Flashcards") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { p ->
        Column(
            Modifier
                .padding(p)
                .fillMaxSize()
                .padding(24.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .graphicsLayer {
                        rotationY = rot
                        cameraDistance = 12 * density
                    }
                    .clickable { flipped = !flipped },
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (flipped) MaterialTheme.colorScheme.secondaryContainer else BluePrimary
                )
            ) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    if (rot <= 90f) {
                        Text(
                            text = words[cur].term,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = words[cur].definition,
                            modifier = Modifier
                                .graphicsLayer { rotationY = 180f }
                                .padding(24.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
            Button(
                onClick = {
                    flipped = false
                    cur = (cur + 1) % words.size
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Next Word")
            }
        }
    }
}
