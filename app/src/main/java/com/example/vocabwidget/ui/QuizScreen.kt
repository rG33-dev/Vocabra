package com.example.vocabwidget.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vocabwidget.VocabRepository
import com.example.vocabwidget.VocabViewModel
import com.example.vocabwidget.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: VocabViewModel,
    onBack: () -> Unit
) {
    val words = remember { VocabRepository.getAllWords().shuffled() }
    var idx by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var done by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Vocabulary Quiz") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        if (done) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.EmojiEvents, null, Modifier.size(100.dp), Color(0xFFFFD700))
                Text("Quiz Finished!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Earned: ${score * 10} XP", color = BluePrimary)
                Button(
                    onClick = {
                        viewModel.addXP(score * 10)
                        onBack()
                    },
                    Modifier.padding(top = 24.dp)
                ) {
                    Text("Finish")
                }
            }
        } else {
            val word = words[idx]
            val opts = remember(idx) {
                (VocabRepository.getAllWords().filter { it != word }.shuffled().take(3) + word).shuffled()
            }
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { (idx + 1).toFloat() / words.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.height(64.dp))
                Text("Define:", color = Color.Gray)
                Text(word.term, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(48.dp))
                opts.forEach { opt ->
                    OutlinedButton(
                        onClick = {
                            if (opt == word) {
                                score++
                                viewModel.markAsMastered(word.term)
                            }
                            if (idx < words.size - 1) idx++ else done = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(opt.definition, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}
