package com.example.vocabwidget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocabwidget.VocabRepository
import com.example.vocabwidget.VocabViewModel
import com.example.vocabwidget.Word
import com.example.vocabwidget.ui.theme.BlueDark
import com.example.vocabwidget.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: VocabViewModel,
    onWordClick: (Word) -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToFlashcards: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val allWords = remember { VocabRepository.getAllWords() }
    val categories = remember { VocabRepository.getCategories() }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    
    val learnedCount = viewModel.learnedWords.size + viewModel.masteredWords.size
    val xp = viewModel.xp.intValue
    val isDarkMode = viewModel.isDarkMode.value
    val level = (xp / 500) + 1
    val progress = (xp % 500) / 500f

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = true, 
                    onClick = {}, 
                    icon = { Icon(Icons.Default.Home, null) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(selected = false, onClick = onNavigateToFlashcards, icon = { Icon(Icons.Default.Style, null) })
                NavigationBarItem(selected = false, onClick = onNavigateToFavorites, icon = { Icon(Icons.Default.FavoriteBorder, null) })
                NavigationBarItem(selected = false, onClick = onNavigateToProfile, icon = { Icon(Icons.Outlined.PersonOutline, null) })
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 24.dp).verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("LEVEL $level", color = BluePrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Hi, David 👋", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { viewModel.toggleDarkMode() }) { 
                    Icon(if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode, null) 
                }
            }
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape), color = BluePrimary, trackColor = BluePrimary.copy(alpha = 0.1f))
            
            Spacer(Modifier.height(28.dp))
            OutlinedTextField(
                value = searchQuery, onValueChange = { searchQuery = it },
                placeholder = { Text("Search word...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                trailingIcon = { Icon(Icons.Default.Tune, null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )

            Spacer(Modifier.height(28.dp))
            Text("Popular Places", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(if (isSelected) Color.Black else Color.Transparent).clickable { selectedCategory = category }.padding(horizontal = 20.dp, vertical = 10.dp)) {
                        Text(text = category, color = if (isSelected) Color.White else Color.Gray, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(allWords.filter { (selectedCategory == "All" || it.category == selectedCategory) && it.term.contains(searchQuery, true) }) { word ->
                    WordCard(word = word, onClick = { onWordClick(word) })
                }
            }
            
            Spacer(Modifier.height(32.dp))
            Text("Practice Hub", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PracticeCard(modifier = Modifier.weight(1f), title = "Quiz", icon = Icons.Default.Psychology, color = BluePrimary) { onNavigateToQuiz() }
                PracticeCard(modifier = Modifier.weight(1f), title = "Cards", icon = Icons.Default.Style, color = Color(0xFFE91E63)) { onNavigateToFlashcards() }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun WordCard(word: Word, onClick: () -> Unit) {
    Card(modifier = Modifier.width(220.dp).height(300.dp).clickable(onClick = onClick), shape = RoundedCornerShape(32.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize().background(Brush.linearGradient(colors = listOf(BluePrimary, BlueDark))))
            Column(Modifier.align(Alignment.BottomStart).padding(24.dp)) {
                Text(word.term, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(word.level, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun PracticeCard(modifier: Modifier, title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(modifier = modifier.height(100.dp).clickable { onClick() }, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
            Icon(icon, null, tint = color)
            Spacer(Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
