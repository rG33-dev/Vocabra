package com.example.vocabwidget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocabwidget.VocabViewModel
import com.example.vocabwidget.Word
import com.example.vocabwidget.ui.theme.BlueDark
import com.example.vocabwidget.ui.theme.BluePrimary

@Composable
fun DetailScreen(
    viewModel: VocabViewModel,
    word: Word,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onShare: (Word) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val isFavorite = viewModel.favorites.contains(word.term)
    val isLearned = viewModel.learnedWords.contains(word.term) || viewModel.masteredWords.contains(word.term)
    val isMastered = viewModel.masteredWords.contains(word.term)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth().height(440.dp).background(Brush.verticalGradient(colors = listOf(BlueDark, BluePrimary)))) {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 56.dp, start = 24.dp, end = 24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = onBack, modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.2f))) { 
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) 
                    }
                    IconButton(onClick = { viewModel.toggleFavorite(word.term) }, modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.2f))) { 
                        Icon(if(isFavorite) Icons.Default.Favorite else Icons.Outlined.BookmarkBorder, null, tint = if(isFavorite) Color.Red else Color.White) 
                    }
                }
                
                Column(modifier = Modifier.align(Alignment.Center).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(word.term, style = MaterialTheme.typography.displayMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(word.pronunciation, color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp)
                        IconButton(onClick = { onSpeak(word.term) }) { 
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, null, tint = Color.White) 
                        }
                    }
                }

                Card(modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 24.dp).offset(y = 45.dp).fillMaxWidth(), shape = RoundedCornerShape(28.dp)) {
                    Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(word.term, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(word.origin, color = Color.Gray)
                        }
                        Text(word.level, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = BluePrimary)
                    }
                }
            }

            Column(modifier = Modifier.padding(top = 64.dp, start = 24.dp, end = 24.dp)) {
                TabRow(selectedTabIndex = selectedTab, containerColor = Color.Transparent, contentColor = BluePrimary, divider = {}) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) { Text("Overview", modifier = Modifier.padding(vertical = 12.dp)) }
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) { Text("Details", modifier = Modifier.padding(vertical = 12.dp)) }
                }
                Spacer(Modifier.height(24.dp))
                if (selectedTab == 0) {
                    Text(word.definition, style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp))
                    Spacer(Modifier.height(16.dp))
                    Text("Example: \"${word.example}\"", fontStyle = FontStyle.Italic, color = Color.Gray)
                } else {
                    Text("Origin: ${word.origin}", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Level: ${word.level}")
                    // Add synonyms/antonyms here if available
                }
                Spacer(Modifier.height(120.dp))
            }
        }
        
        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp)) {
            Button(
                onClick = { viewModel.markAsLearned(word.term) }, 
                enabled = !isLearned, 
                modifier = Modifier.fillMaxWidth().height(60.dp), 
                shape = RoundedCornerShape(20.dp), 
                colors = ButtonDefaults.buttonColors(containerColor = if(isLearned) Color.Gray else Color.Black)
            ) {
                Text(if(isMastered) "Mastered ✓" else if(isLearned) "Learning..." else "Add to Journey", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
